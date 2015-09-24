package com.skipbo;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.skipbo.menus.MainActivity;
import com.skipbo.model.CardPile;
import com.skipbo.model.Game;
import com.skipbo.model.HumanPlayer;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;
import com.skipbo.model.StockPile;
import com.skipbo.model.players.BossInfo;
import com.skipbo.model.players.ComputerPlayer;
import com.skipbo.network.Client.OnlineCardPile;
import com.skipbo.view.GameView;
import com.skipbo.view.Card;
import com.skipbo.view.HandCardView;
import com.skipbo.view.MoveableCard;
import com.skipbo.view.PlayPileView;
import com.skipbo.view.PutAwayPileView;
import com.skipbo.view.StockPileView;

import java.util.List;


/**
 * Created by reneb_000 on 20-2-2015.
 */
public class GameController implements View.OnTouchListener {

    private Game g;
    private GameView view;
    MoveableCard m;
    Card gotten;
    CardPile cardPile;
    private Context context;
    private int stockpileAmount;

    public GameController(String[] players, Context context, int screenwidht, int screenheight, int stockpileAmount) {
        this.context = context;
        this.stockpileAmount = stockpileAmount;
        CardPile cardPile = new LocalCardPile();
        Player p[] = new Player[players.length];
        PlayPile[] playPiles = new PlayPile[4];
        for(int i=0; i<playPiles.length; i++){
            playPiles[i] = new PlayPile(cardPile);
        }
        for (int i=0; i<players.length;i++){
            ComputerPlayer cp = BossInfo.getComputerPlayer(players[i],cardPile, playPiles);
            if(cp!=null) {
                p[i] = cp;
            }else{
                p[i] = new HumanPlayer(players[i], (LocalCardPile) cardPile, stockpileAmount, playPiles);
            }
        }
        g = new Game(p, cardPile, this, playPiles);
        view = new GameView(g, context, screenwidht, screenheight);
        view.setOnTouchListener(this);
        m=null;
    }

    /**
     *
     * @param players to play the game, need to initialize
     * @param context
     * @param screenwidht width of the screen
     * @param screenheight height of the screen
     */
    public GameController(Player[] players, Context context, int screenwidht, int screenheight, CardPile cardPile, int stockpileAmount){
        //CardPile cardPile = new LocalCardPile();
        this.context = context;
        Player p[] = players;
        PlayPile[] playPiles = new PlayPile[4];
        for(int i=0; i<playPiles.length; i++){
            playPiles[i] = new PlayPile(cardPile);
        }
        g = new Game(p, cardPile, this, playPiles);
        for(int i=0; i<players.length; i++){
            players[i].initPlayer(playPiles, new StockPile(cardPile, stockpileAmount), players[i].getName(), cardPile);
        }
        view = new GameView(g, context, screenwidht, screenheight);
        view.setOnTouchListener(this);
        m=null;
    }

    public GameController(Player[] players, final Context context, final int screenwidht, final int screenheight, List<List<Integer>> handcards, List<List<Integer>> stockpile){
        this.context = context;
        Player p[] = players;
        PlayPile[] playPiles = new PlayPile[4];
        cardPile = new OnlineCardPile();
        for(int i=0; i<playPiles.length; i++){
            playPiles[i] = new PlayPile(cardPile);
        }
        g = new Game(p, cardPile, this, playPiles);
        for(int i=0; i<players.length; i++){
            players[i].initPlayer(playPiles, handcards.get(i), stockpile.get(i), cardPile);
        }
        Handler handler = new Handler(context.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                view = new GameView(g, context, screenwidht, screenheight);
                view.setOnTouchListener(GameController.this);
            }
        };
        handler.post(runnable);


        m=null;
    }

    public SurfaceView getView(){
        return view;
    }


    @Override
    public boolean onTouch(View v, MotionEvent e) {
            int x;
            int y;

            if (e.getAction() == MotionEvent.ACTION_DOWN&&m==null) {
                x = (int) e.getX();
                y = (int) e.getY();
                m = view.getMoveableCard(x, y);
                if (m!=null) {
                    m.setCord(x, y, false);
                }
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                if (m != null) {
                    m.setCord((int) e.getX(), (int) e.getY(), true);
                }
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                if (m != null) {
                    x = (int) e.getX();
                    y = (int) e.getY();
                    gotten = view.getObject(x, y);

                    if(gotten !=null) {
                        if(playMove(m, gotten, g.getCurrentPlayer())){
                            synchronized (this) {
                                notifyAll();
                            }
                        }
                        gotten.setSelected(false);
                        gotten.setMoveAble();
                    }
                    m.resetPos();
                    m.setSelected(false);
                    m.setMoveAble();

                    gotten = null;
                    m = null;
                }
            }

            return true;

    }

    public boolean playMove(MoveableCard onClick, Card onRelease, Player p){
        //HumanPlayer p = g.getCurrentPlayer();
        if(onClick.getNumber()!=-1){
            if(onRelease instanceof PlayPileView){
                if(onClick instanceof HandCardView){
                    return p.playHandToPlayPile(onClick.getNumber(), onRelease.getNumber());
                }else if(onClick instanceof StockPileView){
                    if(p.playStockPile(onRelease.getNumber())){
                        //showPopup();
                    }
                    return false;
                    //return p.playStockPile(onRelease.getNumber());
                }else if(onClick instanceof PutAwayPileView){
                    return p.playPutawayToPlayPile(onClick.getNumber(), onRelease.getNumber());
                }
            }
            else if(onRelease instanceof HandCardView){
                if(onClick instanceof HandCardView){
                    return p.switchCard(onClick.getNumber(), onRelease.getNumber());
                }
            }
            else if(onRelease instanceof PutAwayPileView){
                if(onClick instanceof HandCardView){
                    return p.playHandToPutAway(onClick.getNumber(), onRelease.getNumber());
                }
                if(onClick instanceof PutAwayPileView){
                    ((PutAwayPileView) onClick).toggleExpanded();
                }
            }
        }
        return false;
    }


    public void makeMove() {
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Game getGame(){
        return g;
    }

    public CardPile getCardPile(){
        return cardPile;
    }

    public void vibrate(){
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);
    }

    public void showPopup(String name){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_gameend);

        //dialog.setTitle("Title...");

        final MainActivity a = (MainActivity) context;

        ((TextView)dialog.findViewById(R.id.popupTitle)).setText(name+" Won");

        Button replay = (Button) dialog.findViewById(R.id.popupPlayAgain);
        replay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //TODO reset current game.
                dialog.dismiss();
                return false;
            }
        });
        Button exit = (Button) dialog.findViewById(R.id.popupExit);
        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                shutdown();
                a.toMainMenu(v);
                return false;
            }
        });
        dialog.show();

    }


    public void shutdown(){
        g.setGameover(true);
        g.setRunning(false);
        view.setRunning(false);
    }
}
