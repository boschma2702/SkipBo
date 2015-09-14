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
        super(BossInfo.BOSS1_NAME, cardpile, stockPileAmount, playPiles);
    }

    @Override
    public void makeMove(GameController controller) {
        waitBeforePlay();
        Player next = controller.getGame().getNextPlayer();
        playing = true;

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
                if(cardsneeded.size()<getSkipboAmount()){
                    for(int x =0; x<cardsneededDub.size(); x++){
                        int[] pos = getAvailableCardAndPosition(cardsneededDub.get(x));
                        if(pos[0]!=-1){
                            playCard(pos, i);
                        }else{
                            if(getSkipboAmount()==0){
                                Log.e("NORMALPLAYER", "This shouldn't occur often, no problem");
                                playing = false;
                                break;
                            }else{
                                playCard(getAvailableCardAndPosition(0),i);
                            }
                        }
                    }
                    playStockPile();
                    toSkip = true;
                    break;
                }
            }
            if(toSkip){
                continue;
            }
            //check if fuck other player
            for(int i=0; i<playPiles.length; i++){
                List<Integer> cardsneeded = this.getCardsNeeded(next, i);
                List<Integer> cardsneededDub = new ArrayList<>();
                cardsneededDub.addAll(cardsneeded);
                cardsneeded.removeAll(this.getAllAvailableCards());
                if(cardsneeded.size()<getSkipboAmount()){
                    for(int x =0; x<cardsneededDub.size(); x++){
                        int[] pos = getAvailableCardAndPosition(cardsneededDub.get(x));
                        if(pos[0]!=-1){
                            playCard(pos, i);
                        }else{
                            if(getSkipboAmount()==0){
                                Log.e("NORMALPLAYER", "This shouldn't occur often, no problem");
                                playing = false;
                                break;
                            }else{
                                playCard(getAvailableCardAndPosition(0),i);
                            }
                        }
                    }
                    toSkip = true;
                    break;
                }
            }
            if(toSkip){
                continue;
            }

            //check if hand can be played empty

            playing = false;
        }
        playBestToPutawayPile();
        waitBeforePlay();
        fillHand();

    }
}
