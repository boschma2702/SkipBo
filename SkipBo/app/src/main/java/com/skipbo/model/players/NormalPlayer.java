package com.skipbo.model.players;

import android.util.Log;

import com.skipbo.GameController;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 5-9-2015.
 */
public class NormalPlayer extends ComputerPlayer {

    private boolean playing;
    private boolean toSkip;

    public NormalPlayer(LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles){
        super(BossInfo.BOSS4_NAME, cardpile, stockPileAmount, playPiles);
    }

    @Override
    public boolean makeMove(GameController controller) {
        waitBeforePlay();
        Player next = controller.getGame().getNextPlayer();
        playing = true;
        printDebug();
        while(playing&&!hasWon()) {
            toSkip = false;
            //if stockpile playable, play stockpile then check again
            if(playStockPile()){
                waitBeforePlay();
                continue;
            }
            //check if stockpile playabable with help of putaway and handcards
            for(int i=0; i<playPiles.length; i++){
                List<Integer> cardsneeded = this.getCardsNeeded(i);
                List<Integer> cardsneededDub = new ArrayList<>();
                cardsneededDub.addAll(cardsneeded);
                cardsneeded.removeAll(this.getAllAvailableCards());
                printCardsneeded(cardsneededDub, getAllAvailableCards(), cardsneeded);
                if(cardsneeded.size()<=getSkipboAmount()){
                    cardLoop:
                    for(int x =0; x<cardsneededDub.size(); x++){
                        List<int[]> list = getAvailableCardAndPosition(cardsneededDub.get(i));
                        for(int z=0;z<list.size();z++) {
                            int[] pos = list.get(z);
                            if (pos[0] != -1) {
                                playCard(pos, i);
                                break;
                            } else if(!(z+1<list.size())) {
                                if (getSkipboAmount() == 0) {
                                    Log.e("NORMALPLAYER", "This shouldn't occur often, no problem");
                                    playing = false;
                                    break cardLoop;
                                } else {
                                    playSkipboFromHand(i);
                                    break;
                                }
                            }
                        }
                    }
                    playStockPile();

                }
            }

            //check if fuck other player
            for(int i=0; i<playPiles.length; i++){
                List<Integer> cardsneeded = this.getCardsNeeded(next, i);
                List<Integer> cardsneededDub = new ArrayList<>();
                cardsneededDub.addAll(cardsneeded);
                cardsneeded.removeAll(this.getAllAvailableCards());
                if(cardsneeded.size()<getSkipboAmount()){
                    cardLoop:
                    for(int x =0; x<cardsneededDub.size(); x++){
                        List<int[]> list = getAvailableCardAndPosition(cardsneededDub.get(i));
                        for(int z=0;z<list.size();z++) {
                            int[] pos = list.get(z);
                            if (pos[0] != -1) {
                                playCard(pos, i);
                                break;
                            } else if(!(z+1<list.size())) {
                                if (getSkipboAmount() == 0) {
                                    Log.e("NORMALPLAYER", "This shouldn't occur often, no problem");
                                    playing = false;
                                    break cardLoop;
                                } else {
                                    playSkipboFromHand(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            }


            //check if hand can be played empty
            //play as much as possible

            playing = false;
        }
        playBestToPutawayPile();
        waitBeforePlay();
        fillHand();
        return hasWon();
    }
}
