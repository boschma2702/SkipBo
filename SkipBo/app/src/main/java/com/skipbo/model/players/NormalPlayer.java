package com.skipbo.model.players;

import com.skipbo.GameController;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;

/**
 * Created by reneb_000 on 5-9-2015.
 */
public class NormalPlayer extends ComputerPlayer {

    private boolean playing;

    public NormalPlayer(LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles){
        super(BossInfo.BOSS1_NAME, cardpile, stockPileAmount, playPiles);
    }

    @Override
    public void makeMove(GameController controller) {
        waitBeforePlay();
        playing = true;
        while(playing&&!hasWon()) {
            //if stockpile playable, play stockpile then check again
            if(playStockPile()){
                waitBeforePlay();
                continue;
            }
            //check if stockpile playabable with help of putaway and handcards

            //check if hand can be played empty

            //check if fuck other player

            //play to putaway
        }
        waitBeforePlay();
        fillHand();

    }
}
