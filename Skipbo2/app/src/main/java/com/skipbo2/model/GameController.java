package com.skipbo2.model;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.skipbo2.view.Card;
import com.skipbo2.view.HandCardView;
import com.skipbo2.view.MainView;
import com.skipbo2.view.MoveableCard;
import com.skipbo2.view.PlayPileView;
import com.skipbo2.view.PutAwayPileView;
import com.skipbo2.view.StockPileView;

/**
 * Created by reneb_000 on 18-7-2015.
 */
public class GameController implements View.OnTouchListener {

    private Game g;
    //private MainView view;
    MoveableCard m;
    Card gotten;

    public GameController(Game g){
        this.g = g;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        synchronized (this) {
            MainView view = (MainView)v;
            int x = 0;
            int y = 0;

            if (e.getAction() == MotionEvent.ACTION_DOWN && m == null) {
                x = (int) e.getX();
                y = (int) e.getY();
                m = view.getMoveableCard(x, y);
                if (m != null) {
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

                    if (gotten != null) {
                        if (playMove(m, gotten, g.getCurrentPlayer())) {
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

    public boolean playMove(MoveableCard onClick, Card onRelease, Player p) {
        //HumanPlayer p = g.getCurrentPlayer();
        if (onClick.getNumber() != -1) {
            if (onRelease instanceof PlayPileView) {
                if (onClick instanceof HandCardView) {
                    return p.playHandToPlayPile(onClick.getNumber(), onRelease.getNumber());
                } else if (onClick instanceof StockPileView) {
                    return p.playStockPile(onRelease.getNumber());
                } else if (onClick instanceof PutAwayPileView) {
                    return p.playPutawayToPlayPile(onClick.getNumber(), onRelease.getNumber());
                }
            } else if (onRelease instanceof HandCardView) {
                if (onClick instanceof HandCardView) {
                    return p.swichtCard(onClick.getNumber(), onRelease.getNumber());
                }
            } else if (onRelease instanceof PutAwayPileView) {
                if (onClick instanceof HandCardView) {
                    return p.playHandToPutAway(onClick.getNumber(), onRelease.getNumber());
                }
            }
        }
        return false;
    }

    public void makeMove() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}