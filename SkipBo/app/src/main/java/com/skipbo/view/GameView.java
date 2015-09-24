package com.skipbo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.skipbo.menus.MainActivity;
import com.skipbo.model.Game;
import com.skipbo.model.HumanPlayer;
import com.skipbo.model.Player;
import com.skipbo.model.players.ComputerPlayer;
import com.skipbo.network.Client.StubPlayer;
import com.skipbo.network.Server.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reneb_000 on 20-2-2015.
 */
public class GameView extends SurfaceView implements Runnable {

    private static final int initW = 1920;

    private PlayPileView[] playpiles;
    private Game g;
    private Map<Player, HandCardView[]> handcardsMap;
    private Map<Player, PutAwayPileView[]> putawaypilesMap;
    private Map<Player, PutAwayPileView[]> otherPutAwayPiles;
    private Map<Player, StockPileView> otherStockPiles;
    private Map<Player, StockPileView> stockpilesMap;

    private Player[] humanPlayers;

    SurfaceHolder holder;
    boolean running;

    private boolean currentPlaying;
    private CardDecoder resizedDecoder;

    private int screenWidht, screenHeight;

    private int padding;

    public GameView(Game g, Context context, int width, int height){
        super(context);
        this.g = g;
        running = true;
        holder = getHolder();
        handcardsMap = new HashMap<Player, HandCardView[]>();
        putawaypilesMap = new HashMap<Player, PutAwayPileView[]>();
        stockpilesMap = new HashMap<Player, StockPileView>();
        otherPutAwayPiles = new HashMap<Player, PutAwayPileView[]>();
        otherStockPiles = new HashMap<Player, StockPileView>();
        screenWidht = width;
        screenHeight = height;
        padding = (int) CardDecoder.PADDING;


        //CardDecoder decoder = new CardDecoder(context, (float)width/initW, width, height);
        CardDecoder decoder = MainActivity.cardDecoder;

        humanPlayers = g.getPlayers();
        for(int x = 0; x< humanPlayers.length; x++) {
            //fill handcards
            HandCardView[] handCards = new HandCardView[humanPlayers[x].getHandcards().size()];
            for (int i = 0; i < handCards.length; i++) {
                handCards[i] = new HandCardView(humanPlayers[x].getHandcards().get(i), i, humanPlayers[x], decoder);
            }
            handcardsMap.put(humanPlayers[x],handCards);
            //fill putawaypiles
            PutAwayPileView[] putAwayPiles = new PutAwayPileView[humanPlayers[x].getPutAwayPiles().length];
            for(int i=0; i<putAwayPiles.length;i++){
                putAwayPiles[i] = new PutAwayPileView(humanPlayers[x].getPutAwayPiles()[i], i, decoder);
            }
            putawaypilesMap.put(humanPlayers[x],putAwayPiles);

            stockpilesMap.put(humanPlayers[x],new StockPileView(humanPlayers[x].getStockpile(), decoder, false));

        }
        playpiles = new PlayPileView[g.getPlayPiles().length];
        for(int i=0; i<playpiles.length;i++){
            playpiles[i] = new PlayPileView(g.getPlayPiles()[i], i, decoder);
        }

        setupSidePutawayPiles(context, width, height);
        new Thread(this).start();
    }

    private void setupSidePutawayPiles(Context context, int width, int height) {

        float w = (float)width/(float)2;
        float winit = initW;
        float result = w/winit;
        //resizedDecoder = new CardDecoder(context,result,width,height);
        resizedDecoder = MainActivity.resizedCardDecoder;
        Player[] humanPlayers = g.getPlayers();
        for(int x=0; x< humanPlayers.length;x++){
            //int y = x*cd.getCardHeight()+50;//50 is padding
            otherPutAwayPiles.put(humanPlayers[x], new PutAwayPileView[putawaypilesMap.get(humanPlayers[x]).length]);
            for(int i=0; i<putawaypilesMap.get(humanPlayers[x]).length;i++){
                otherPutAwayPiles.get(humanPlayers[x])[i] = new PutAwayPileView(humanPlayers[x], humanPlayers[x].getPutAwayPiles()[i], i, resizedDecoder);
            }
            otherStockPiles.put(humanPlayers[x], new StockPileView(humanPlayers[x].getStockpile(),resizedDecoder, true));
        }
    }

    public void onDraw(Canvas c){
        //c.drawBitmap(getResizedBitmap(getBitMap(-1), 100,125),0,0,null);
        Player p = g.getCurrentPlayer();
        currentPlaying = !(p instanceof ClientHandler || p instanceof StubPlayer || p instanceof ComputerPlayer);
        //currentPlaying = true;
        Card selectedCard = null;

        for(int i =0; i<playpiles.length; i++){
                playpiles[i].onDraw(c);
        }

        PutAwayPileView[] putAwayPiles = putawaypilesMap.get(p);
        for(int i=0; i<putAwayPiles.length;i++){
            //putAwayPiles[i] = new PutAwayPileView(g.getCurrentPlayer().getPutAwayPiles()[i], i);
            if(!putAwayPiles[i].getSelected()) {
                putAwayPiles[i].onDraw(c);
            }else{
                selectedCard=putAwayPiles[i];
            }
        }

        HandCardView[] handCards = handcardsMap.get(p);
        for(int i =0; i<handCards.length; i++){
            if(currentPlaying) {
                if (!handCards[i].getSelected()) {
                    handCards[i].onDraw(c);
                } else {
                    selectedCard = handCards[i];
                }
            }else{
                handCards[i].onDrawEmpty(c);
            }
        }
        StockPileView stockpileview = stockpilesMap.get(p);
        if(!stockpileview.getSelected()){
            stockpileview.onDraw(c);
        }else{
            selectedCard = stockpileview;
        }
        /*
        for(Player humanPlayer :otherPutAwayPiles.keySet()){
            if(!humanPlayer.equals(g.getCurrentPlayer())) {
                for (int i = 0; i < otherPutAwayPiles.get(humanPlayer).length; i++) {
                    otherPutAwayPiles.get(humanPlayer)[i].onDraw(c);
                }
                otherStockPiles.get(humanPlayer).onDraw(c);
            }
        }*/
        int sideStart = screenWidht - 5*(resizedDecoder.getCardWidth()+(int)CardDecoder.PADDING);
        int counter = 0;

        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        int current = g.getCurrent();
        for(int x=0; x< humanPlayers.length;x++){
            if(x<current){
                second.add(x);
            }else if(x>current){
                first.add(x);
            }
        }
        first.addAll(second);

        for(int x=0; x< first.size();x++){
            if(!humanPlayers[first.get(x)].equals(g.getCurrentPlayer())) {
                int y = counter*(resizedDecoder.getCardHeight()+resizedDecoder.getCardHeight()/2+30)+padding*3;
                for (int i = 0; i < otherPutAwayPiles.get(humanPlayers[first.get(x)]).length; i++) {
                    otherPutAwayPiles.get(humanPlayers[first.get(x)])[i].onDraw(c, i*(padding+resizedDecoder.getCardWidth())+sideStart,y);
                }
                otherStockPiles.get(humanPlayers[first.get(x)]).onDraw(c, screenWidht-resizedDecoder.getCardWidth(),y);
                counter++;
            }
        }

        if(selectedCard!=null){
            selectedCard.onDraw(c);
        }

    }


    public Card getObject(int x, int y){
        for(int i =0; i<playpiles.length; i++){
            if(playpiles[i].contains(x,y)&&!(playpiles[i].getSelected())){
                return playpiles[i];
            }
        }

        Player p = g.getCurrentPlayer();
        PutAwayPileView[] putAwayPiles = putawaypilesMap.get(p);
        for(int i=0; i<putAwayPiles.length;i++){
            //if(putAwayPiles[i].contains(x,y)&&!(putAwayPiles[i].getSelected())){
            if(putAwayPiles[i].contains(x,y)){
                return putAwayPiles[i];
            }
        }
        HandCardView[] handCards = handcardsMap.get(p);
        for(int i =0; i<handCards.length; i++){
            if(handCards[i].contains(x,y)&&!(handCards[i].getSelected())){
                return handCards[i];
            }
        }
        StockPileView stockpileview = stockpilesMap.get(p);
        if(stockpileview.contains(x,y)&&!(stockpileview.getSelected())){
            stockpileview.setSelected(true);
            return stockpileview;
        }
        return null;
    }

    public MoveableCard getMoveableCard(int x, int y){
        if(currentPlaying) {
            Player p = g.getCurrentPlayer();
            PutAwayPileView[] putAwayPiles = putawaypilesMap.get(p);
            HandCardView[] handCards = handcardsMap.get(p);
            StockPileView stockpileview = stockpilesMap.get(p);

            for (int i = 0; i < putAwayPiles.length; i++) {
                if (putAwayPiles[i].contains(x, y) && putAwayPiles[i].setMoveAble() && !(putAwayPiles[i].getSelected())) {
                    putAwayPiles[i].setSelected(true);
                    return putAwayPiles[i];
                }
            }
            for (int i = 0; i < handCards.length; i++) {
                if (handCards[i].contains(x, y) && handCards[i].setMoveAble() && !(handCards[i].getSelected())) {
                    handCards[i].setSelected(true);
                    return handCards[i];
                }
            }
            if (stockpileview.contains(x, y) && !(stockpileview.getSelected())) {
                stockpileview.setSelected(true);
                return stockpileview;
            }
        }
        return null;
    }

    @Override
    public void run() {
        while(running){
            if(!holder.getSurface().isValid()){
                continue;
            }
            Canvas c = holder.lockCanvas();
            if(c!=null) {
                c.drawARGB(255, 255, 50, 50);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean b){
        running = b;
    }

}
