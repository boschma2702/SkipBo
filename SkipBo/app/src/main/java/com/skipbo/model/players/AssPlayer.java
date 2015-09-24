package com.skipbo.model.players;

import android.util.Log;

import com.skipbo.GameController;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 16-8-2015.
 */
public class AssPlayer extends ComputerPlayer {

    private boolean playing;
    private int thresHold = 4;

    public AssPlayer(LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles) {
        super(BossInfo.BOSS3_NAME, cardpile, stockPileAmount, playPiles);
    }

    @Override
    public boolean makeMove(GameController controller) {
        playing = true;
        waitBeforePlay();
        Player next = controller.getGame().getNextPlayer();
        int tobeat = next.getStockpile().getCard();
        while (playing&&!hasWon()){
            for(int i=0; i<playPiles.length; i++){

                if(tobeat - playPiles[i].getTeller()<=thresHold){
                    Log.e("TEST", "Hand: "+handcards);
                    List<Integer> cardsNeeded = new ArrayList<>();
                    List<Integer> cardsNeededDub = new ArrayList<>();

                    List<Integer> cardsAvailable = getAllAvailableCards();
                    int teller = playPiles[i].getTeller()+1;
                    if(teller==0){
                        teller=1;
                    }
                    while (teller<tobeat+1){
                        cardsNeeded.add(teller);
                        teller++;
                    }
                    cardsNeededDub.addAll(cardsNeeded);
                    Log.e("TEST","cardsneeded "+cardsNeeded + " cards Avaiable: "+cardsAvailable);
                    cardsNeeded.removeAll(cardsAvailable);
                    Log.e("TEST","cardsneeded "+cardsNeeded + "dub: "+cardsNeededDub);
                    if(cardsNeeded.size()==0){
                        for(int x=0; x<cardsNeededDub.size(); x++){
                            playCard(getAvailableCardAndPosition(cardsNeededDub.get(x)).get(0),i);
                        }
                    }else if(cardsNeeded.size()<=getSkipboAmount()){
                        for(int x=0; x<cardsNeededDub.size(); x++){
                            int[] pos = getAvailableCardAndPosition(cardsNeededDub.get(x)).get(0);
                            if(pos[0]!=-1) {
                                playCard(pos, i);
                            }else{
                                playHandToPlayPile(handContainsCard(0),i);
                            }
                        }
                    }
                }
            }
            playing = false;
            //TODO multiple loops
            //TODO use putawaycards more
        }
        playBestToPutawayPile();
        fillHand();
        return hasWon();

    }
}
