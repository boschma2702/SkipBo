package com.skipbo.network.Server;

import android.util.Log;

import com.skipbo.network.Client.Client;

/**
 * Created by reneb_000 on 31-7-2015.
 */
public class InterperterClient implements Runnable {

    private Client client;
    private String[] s;

    public InterperterClient(Client client){
        this.client = client;
    }



    public void run(){
        Log.e("TEST", "COMMAND RECEIVED: " + s[0]);
        switch (s[0]){
            case CommandList.SEND_GAMESTRING:
                Log.e("TEST", "START COMMAND");
                client.startGame(s);
                break;
            case CommandList.SEND_CARDPILECARDS:
                if(client.isGameStarted()) {
                    client.getOnlineCardPile().received(s);
                }
                break;
            case CommandList.PLAY_HAND_HAND:
                if(client.isGameStarted()) {
                    client.getGame().getCurrentPlayer().switchCard(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
                break;
            case CommandList.PLAY_HAND_PLAYPILE:
                if(client.isGameStarted()) {
                    client.getGame().getCurrentPlayer().playHandToPlayPile(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
                break;
            case CommandList.PLAY_STOCKPILE:
                if(client.isGameStarted()) {
                    client.getGame().getCurrentPlayer().playStockPile(Integer.parseInt(s[1]));
                }
                break;
            case CommandList.PLAY_HAND_PUTAWAYPILE:
                if(client.isGameStarted()) {
                    client.getGame().getCurrentPlayer().playHandToPutAway(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                    synchronized (this) {
                        notifyAll();
                    }
                }
                break;
            case CommandList.PLAY_PUTAWAYPILE:
                if(client.isGameStarted()) {
                    client.getGame().getCurrentPlayer().playPutawayToPlayPile(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
                break;
        }
    }

}
