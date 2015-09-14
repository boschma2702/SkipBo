package com.skipbo.model.players;

import android.util.Log;

import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;
import com.skipbo.model.PutAwayPile;
import com.skipbo.model.StockPile;
import com.skipbo.network.Server.InterperterClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by reneb_000 on 15-8-2015.
 */
public abstract class ComputerPlayer extends Player {


    public ComputerPlayer(String name, LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles){
        super(playPiles, new StockPile(cardpile, stockPileAmount), name, cardpile);
    }



    public void playRandomHandToPutAwayPile(){
        Random r = new Random();
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<handcards.size(); i++){
            if(handcards.get(i)!=-1){
                list.add(i);
            }
        }
        playHandToPutAway(list.get(r.nextInt(list.size())), r.nextInt(4));
    }

    public void waitBeforePlay(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean stockpilePlayable(int number){
        if(playPiles[number].isPlaceable(stockpile.getCard())){
            return true;
        }
        return false;
    }

    public boolean putawayToPlayPilePlayable(int from, int to){
        if(playPiles[to].isPlaceable(putAwayPiles[from].getCard())){
            return true;
        }
        return false;
    }

    public boolean handToPutAwayPlayable(int hand, int pile){
        if(putAwayPiles[pile].addCard(handcards.get(hand))) {
            return true;
        }
        return false;
    }

    public boolean handToPlayPilePlayable(int hand, int pile){
        if(playPiles[pile].isPlaceable(handcards.get(hand))){
            return true;
        }
        return false;
    }

    public void playBestToPutawayPile(){
        Log.e("TEST","PLAY BEST");
        int[] highestPair = getHighestPair();
        if(handContainsCard(12)!=-1){
            if(putawayContainsCard(12)!=-1){
                playHandToPutAway(handContainsCard(12), putawayContainsCard(12));
            }else if(isEmptyPutawaypile()!=-1){
                playHandToPutAway(handContainsCard(12), isEmptyPutawaypile());
            }
        }else if(highestPair[0]!=-1){
            playHandToPutAway(highestPair[0],highestPair[1]);
        }else if(isEmptyPutawaypile()!=-1){
            playHighestToPutawayPile(isEmptyPutawaypile());
        }else{
            playHighestToPutawayPile(3);
        }



    }

    /**
     * plays the highest card in your hand to playpile 4
     */
    public void playHighestToPutawayPile(int pile){
        int highest = 0;
        for(int i=0; i<handcards.size(); i++){
            if(handcards.get(i)>handcards.get(highest)){
                highest = i;
            }
        }
        playHandToPutAway(highest, pile);
    }

    /**
     * Checks if handcards contains the card, if so returns the position of the card
     * @param card value needs to be checked
     * @return the position if card is available, if not -1
     */
    public int handContainsCard(int card){
        for(int i=0; i<handcards.size(); i++){
            if(handcards.get(i)==card){
                return i;
            }
        }
        return -1;
    }

    /**
     * Plays if possible the stockpile card to the first location
     * @return true if stockpile card was played
     */
    public boolean playStockPile(){
        if(stockpile.getCard()!=0) {
            for (int i = 0; i < playPiles.length; i++) {
                if (stockpilePlayable(i)) {
                    playStockPile(i);
                    return true;
                }
            }
        }else{
            playStockPile(new Random().nextInt(putAwayPiles.length));
            return true;
        }
        return false;
    }

    /**
     * Checks if putawaypiles contains the card, if so returns the position of the card
     * @param card value needs to be checked
     * @return the position if card is available, if not -1
     */
    public int putawayContainsCard(int card){
        for(int i=0; i<putAwayPiles.length; i++){
            if(putAwayPiles[i].getCard()==card){
                return i;
            }
        }
        return -1;
    }

    public int isEmptyPutawaypile(){
        for(int i=0; i<putAwayPiles.length;i++){
            if(putAwayPiles[i].getCard()==-1){
                return i;
            }
        }
        return -1;
    }

    /**
     * checks for all handcards if there is a putawaypile with the current card one higher then the
     * handcard
     * @return array with at position 0 the handcard and position 1 the putawaypile. If above
     * statement is false for all then both positions are -1
     */
    public int[] getHighestPair(){
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;
        for(int i=0; i<handcards.size(); i++){
            int currentHand = handcards.get(i);
            if(currentHand!=-1) {
                for (int x = 0; x < putAwayPiles.length; x++) {
                    if (putAwayPiles[x].getCard() == currentHand + 1) {
                        if(result[0]==-1) {
                            result[0] = i;
                            result[1] = x;
                        }else{
                            if(handcards.get(i)>handcards.get(result[0])){
                                result[0] = i;
                                result[1] = x;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<Integer> getAllAvailableCards(){
        List<Integer> cards = new ArrayList<>();
        for(int i=0; i<putAwayPiles.length; i++){
            cards.add(putAwayPiles[i].getCard());
            cards.addAll(this.getPutawayBehindFirst(putAwayPiles[i]));
        }
        cards.addAll(handcards);
        cards.add(stockpile.getCard());
        return cards;
    }

    /**
     * returns the position of the given card
     * @param card needed
     * @return the position of the given card. -1 = not present, 0 = stockpile, 1 = handcard,
     * 2 = putawaypile
     */
    public int[] getAvailableCardAndPosition(int card){
        int[] result = new int[2];
        result[0] = -1;
        if(stockpile.getCard()==card){
            result[0] = 0;
            return result;
        }
        if(putawayContainsCard(card)!=-1) {
            for (int i = 0; i < putAwayPiles.length; i++) {
                result[0] = 2;
                result[1] = putawayContainsCard(card);
                return result;
            }
        }
        if(handcards.contains(card)){
            result[0] = 1;
            result[1] = handContainsCard(card);
        }
        return result;
    }


    public void playCard(int[] array, int place){
        waitBeforePlay();
        switch (array[0]){
            case 0:
                playStockPile(place);
                break;
            case 1:
                playHandToPlayPile(array[1],place);
                break;
            case 2:
                playPutawayToPlayPile(array[1],place);
                break;
        }
    }

    public int getSkipboAmount(){
        int counter = 0;
        for(int i=0; i<handcards.size(); i++){
            if(handcards.get(i)==0){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Returns a list containing the int values needed to play the stockpile given a putawaypile
     * @param putawayPile the putawaypile needed to be checked
     * @return list containing all the cards needed
     */
    public List<Integer> getCardsNeeded(int putawayPile){
        List<Integer> list = new ArrayList<>();
        int stock = this.getStockpile().getCard();
        int put = putAwayPiles[putawayPile].getCard();
        int counter = put;
        if(stock>put){
            while(counter<stock){
                list.add(counter);
                counter++;
            }
        }else{
            while(counter!=stock){
                list.add(counter);
                counter = (counter+1)%13;
            }
        }
        return list;
    }

    /**
     * function retrieves all the cards that are 1 appart in the given putawaypile
     * @param p the putawaypile to be checked
     * @return list containing all the cards
     */
    public List<Integer> getPutawayBehindFirst(PutAwayPile p){
        List<Integer> list = new ArrayList<>();
        int card = p.getCard();
        if(card!=-1) {
            for (int i = p.getCards().size()-2; i > 0; i--) {
                if(card+1==p.getCards().get(i)){
                    list.add(p.getCards().get(i));
                    card++;
                }else {
                    break;
                }
            }
        }
        return list;
    }



}
