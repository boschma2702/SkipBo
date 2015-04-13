package com.skipbo;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.skipbo.model.Player;
import com.skipbo.view.GameView;
import com.skipbo.view.Card;
import com.skipbo.view.HandCardView;
import com.skipbo.view.MoveableCard;
import com.skipbo.view.PlayPileView;
import com.skipbo.view.PutAwayPileView;
import com.skipbo.view.StockPileView;

/**
 * Created by reneb_000 on 20-2-2015.
 */
public class GameController implements View.OnTouchListener {

    private Game g;
    private GameView view;
    MoveableCard m;
    Card gotten;

    public GameController(String[] players, Context context, int screenwidht, int screenheight) {
   
        g = new Game(players, 15, this);
        view = new GameView(g, context, screenwidht, screenheight);
        m=null;
    }



    @Override
    public boolean onTouch(View v, MotionEvent e) {
        synchronized (this) {

            int x = 0;
            int y = 0;

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
                        if(playMove(m, gotten)){
                            notifyAll();
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
    }

    public boolean playMove(MoveableCard onClick, Card onRelease){
        Player p = g.getCurrentPlayer();
        if(onClick.getNumber()!=-1){
            if(onRelease instanceof PlayPileView){
                if(onClick instanceof HandCardView){
                    return p.playHandToPlayPile(onClick.getNumber(), onRelease.getNumber());
                }else if(onClick instanceof StockPileView){
                    return p.playStockPile(onRelease.getNumber());
                }else if(onClick instanceof PutAwayPileView){
                    return p.playPutawayToPlayPile(onClick.getNumber(), onRelease.getNumber());
                }
            }
            else if(onRelease instanceof HandCardView){
                if(onClick instanceof HandCardView){
                    return p.swichtCard(onClick.getNumber(),onRelease.getNumber());
                }
            }
            else if(onRelease instanceof PutAwayPileView){
                if(onClick instanceof HandCardView){
                    return p.playHandToPutAway(onClick.getNumber(), onRelease.getNumber());
                }
            }
        }




        /*
        if(number!=-1) {
            boolean var = false;
            if (card instanceof PutAwayPileView) {
                var = g.getCurrentPlayer().playHandToPutAway(number, card.getNumber());
                card.setMoveAble();
                Log.e("","Gotten is putawaypile");
            } else if (card instanceof PlayPileView) {
                var = g.getCurrentPlayer().playHandToPlayPile(number, card.getNumber());
                card.setMoveAble();
                Log.e("","Gotten is playpile");
            } else if (card instanceof HandCardView) {
                var = g.getCurrentPlayer().swichtCard(number, card.getNumber());
                card.setMoveAble();
                Log.e("","Gotten is handcard");
            }
            card.setSelected(false);
            return var;
        }*/
        return false;
    }

    public void Draw(Canvas c) {
        view.onDraw(c);
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
}
