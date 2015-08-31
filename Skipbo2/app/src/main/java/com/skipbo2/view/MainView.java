package com.skipbo2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.skipbo2.model.Game;
import com.skipbo2.model.GameController;
import com.skipbo2.model.Player;

import java.util.HashMap;

/**
 * Created by reneb_000 on 18-7-2015.
 */
public class MainView extends SurfaceView implements Runnable{

    private boolean running = true;
    private SurfaceHolder holder;
    private GameController controller;
    private Game game;

    private static final int DEFAULT_STOCKAMOUNT = 30;

    private HashMap<Player, Card[]> playerHandCards = new HashMap<>();
    //private HashMap<Player, Card[]> playerPutawayPile = new HashMap<>();
    //private HashMap<Player, Card[]> playerHandCards = new HashMap<>();


    public MainView(Context context, String[] players) {
        super(context);
        holder = getHolder();
        controller = new GameController();
        game = new Game(players, DEFAULT_STOCKAMOUNT, controller);
    }

    public Card getCard(int x, int y){
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
                //controller.onDraw(c);
            }
            holder.unlockCanvasAndPost(c);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public MoveableCard getMoveableCard(int x, int y) {
    return null;
    }

    public Card getObject(int x, int y) {
        return null;
    }
}
