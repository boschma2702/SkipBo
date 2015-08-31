package com.skipbo.model.players;

import android.util.Log;

import com.skipbo.GameController;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.network.Server.InterperterClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by reneb_000 on 16-8-2015.
 */
public class BabyPlayer extends ComputerPlayer {

    private int stockpilechance = 25;
    private int playChance = 50;
    private Random random = new Random();
    private boolean playing;

    public BabyPlayer(LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles) {
        super(BossInfo.BOSS2_NAME, cardpile, stockPileAmount, playPiles);
    }

    @Override
    public void makeMove(GameController controller) {
        waitBeforePlay();
        playing = true;
        while(playing) {
            int newRandom = random.nextInt(100) + 1;
            Log.e("TEST", "Random is: " + newRandom);
            if (newRandom <= stockpilechance) {
                playStockPile(random.nextInt(4));
                waitBeforePlay();
            } else if (newRandom <= playChance + stockpilechance) {
                playHandToPlayPile(getRandomAvailableHand(), random.nextInt(4));
                waitBeforePlay();
            } else {
                playing = false;
                playRandomHandToPutAwayPile();
                waitBeforePlay();
            }
        }
        fillHand();
    }

    public boolean playStockPile(int number){
        playPiles[number].addCard(stockpile.playCard());
        if(hasWon()){
            return true;
        }
        return false;
    }

    public boolean playPutawayToPlayPile(int from, int to){
        playPiles[to].addCard(putAwayPiles[from].getCard());
        putAwayPiles[from].deleteCard();
        return false;
    }

    public int getRandomAvailableHand(){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<handcards.size(); i++){
            if(handcards.get(i)!=-1){
                list.add(i);
            }
        }
        return list.get(random.nextInt(list.size()));
    }

    public boolean playHandToPlayPile(int hand, int pile){
        playPiles[pile].addCard(handcards.get(hand));
        handcards.set(hand, -1);
        if(isHandEmpty()){
            fillHand();
        }
        return false;
    }

}
