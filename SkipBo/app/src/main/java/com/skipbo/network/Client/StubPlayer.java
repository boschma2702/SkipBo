package com.skipbo.network.Client;

import com.skipbo.GameController;
import com.skipbo.model.Player;

/**
 * Created by reneb_000 on 27-7-2015.
 */
public class StubPlayer extends Player {


    public StubPlayer(String name){
        this.name = name;
    }

    @Override
    public boolean playHandToPutAway(int hand, int pile) {
        if(putAwayPiles[pile].addCard(handcards.get(hand))) {
            handcards.set(hand, -1);
            synchronized (this){
                notifyAll();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean playHandToPlayPile(int hand, int pile) {
        if(playPiles[pile].isPlaceable(handcards.get(hand))){
            playPiles[pile].addCard(handcards.get(hand));
            handcards.set(hand, -1);
            if(isHandEmpty()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fillHand();
                    }
                }).start();
            }
        }
        return false;
    }

    @Override
    public boolean makeMove(GameController controller) {
        synchronized (this){
            try {
                wait();
                fillHand();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return hasWon();
    }
}
