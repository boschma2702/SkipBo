package com.skipbo.model.players;

import com.skipbo.GameController;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;

/**
 * Created by reneb_000 on 15-8-2015.
 */
public class IdiotPlayer extends ComputerPlayer {


    private boolean playing;

    public IdiotPlayer(LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles){
        super(BossInfo.BOSS1_NAME, cardpile, stockPileAmount, playPiles);
    }

    @Override
    public boolean makeMove(GameController controller) {
        waitBeforePlay();
        playing = true;
        while(playing&&!hasWon()) {
            int prevCard = stockpile.getCard();
            for (int i = 0; i < playPiles.length; i++) {
                if(stockpilePlayable(i)) {
                    playStockPile(i);
                    waitBeforePlay();
                }
            }
            if(prevCard==stockpile.getCard()&&stockpile.getCard()!=0){
                playing = false;
                playRandomHandToPutAwayPile();
            }
        }
        waitBeforePlay();
        fillHand();
        return hasWon();

    }
}
