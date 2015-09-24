package com.skipbo.model;

import android.view.View;

import com.skipbo.GameController;
import com.skipbo.menus.MainActivity;
import com.skipbo.model.CardPile;
import com.skipbo.model.Game;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;
import com.skipbo.model.players.ComputerPlayer;
import com.skipbo.network.Client.StubPlayer;
import com.skipbo.network.Server.ClientHandler;

/**
 * Created by reneb_000 on 24-7-2015.
 */
public class Game implements Runnable{

    private Player[] players;
    private PlayPile[] playPiles = new PlayPile[4];
    //private CardPile cardPile;
    private int current;
    private boolean running = true;
    private boolean gameover = false;
    private int playercount;
    private GameController gameController;
    private boolean vibrate;


    public Game(Player[] players, CardPile cardPile, GameController gameController, PlayPile[] playPiles){
        this.players = players;
        vibrate = MainActivity.settings.getVibrate();
        //this.cardPile = cardPile;
        /*
        playPiles[0] = new PlayPile(cardPile);
        playPiles[1] = new PlayPile(cardPile);
        playPiles[2] = new PlayPile(cardPile);
        playPiles[3] = new PlayPile(cardPile);*/
        this.playPiles = playPiles;
        current = 0;
        playercount = players.length;
        this.gameController = gameController;
        new Thread(this).start();
    }





    public Player getCurrentPlayer() {
        return players[current];
    }

    public Player[] getPlayers() {
        return players;
    }

    public PlayPile[] getPlayPiles() {
        return playPiles;
    }

    public void run() {
        running = true;
        gameover = false;
        while (running) {
            //reset();
            play();
        }
    }

    private void reset() {
        gameover = false;
    }



    private void play() {
        while (!gameover) {
            if(players[current].makeMove(gameController)){
                gameover = false;
                running = false;
                gameController.showPopup(players[current].getName());
            }
            current = (current + 1) % playercount;
            if(!(players[current] instanceof ClientHandler || players[current] instanceof StubPlayer || players[current] instanceof ComputerPlayer)){
                if(vibrate) {
                    gameController.vibrate();
                }
            }
        }
    }

    public Player getNextPlayer(){
        return players[(current + 1) % playercount];
    }

    public void setGameover(boolean b){
        gameover = b;
    }

    public void setRunning(boolean b){
        running = b;
    }

    public int getCurrent(){
        return current;
    }
}
