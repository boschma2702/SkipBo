package com.skipbo.model;

import android.util.Log;

import com.skipbo.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 20-4-2015.
 */
public abstract class Player {

    protected PlayPile[] playPiles;
    protected PutAwayPile[] putAwayPiles = new PutAwayPile[4];
    protected StockPile stockpile;
    protected CardPile cardPile;
    protected String name;


    protected List<Integer> handcards;
    //private StockPile stockPile;


    public Player(){

    }

    public Player(PlayPile[] playPiles, StockPile stockPile, String name, CardPile cardPile){
        this.playPiles = playPiles;
        //this.putAwayPiles = putAwayPiles;
        this.stockpile = stockPile;
        this.name = name;
        putAwayPiles = new PutAwayPile[4];
        this.cardPile = cardPile;
        handcards = cardPile.getCards(5);
        for (int i=0; i<putAwayPiles.length;i++){
            putAwayPiles[i] = new PutAwayPile();
        }
    }

    public void initPlayer(PlayPile[] playPiles, List<Integer> handcards, List<Integer> stockPile, CardPile cardPile){
        this.playPiles = playPiles;
        this.handcards = new ArrayList<>(handcards);
        this.stockpile = new StockPile(stockPile);
        this.cardPile = cardPile;
        for (int i=0; i<putAwayPiles.length;i++){
            putAwayPiles[i] = new PutAwayPile();
        }
        Log.e("TEST", "Player geinit, handcards value: "+this.handcards+"\nStockpile value: "+this.stockpile+"\nCardpile value: "+cardPile+"\nName: "+this.name);
    }

    public void initPlayer(PlayPile[] playPiles, StockPile stockPile, String name, CardPile cardPile){
        this.playPiles = playPiles;
        //this.putAwayPiles = putAwayPiles;
        this.stockpile = stockPile;
        this.name = name;
        this.cardPile = cardPile;
        handcards = cardPile.getCards(5);
        for (int i=0; i<putAwayPiles.length;i++){
            putAwayPiles[i] = new PutAwayPile();
        }
    }

    public abstract void makeMove(GameController controller);

    public String getName(){
        return name;
    }


    public boolean playStockPile(int number){
        if(playPiles[number].isPlaceable(stockpile.getCard())){
            playPiles[number].addCard(stockpile.playCard());
            if(hasWon()){
                return true;
            }
        }
        return false;
    }

    public boolean playPutawayToPlayPile(int from, int to){
        if(playPiles[to].isPlaceable(putAwayPiles[from].getCard())){
            playPiles[to].addCard(putAwayPiles[from].getCard());
            putAwayPiles[from].deleteCard();
        }
        return false;
    }

    public boolean playHandToPutAway(int hand, int pile){
        if(putAwayPiles[pile].addCard(handcards.get(hand))) {
            handcards.set(hand, -1);
            return true;
        }
        return false;
    }

    public boolean playHandToPlayPile(int hand, int pile){
        if(playPiles[pile].isPlaceable(handcards.get(hand))){
            playPiles[pile].addCard(handcards.get(hand));
            handcards.set(hand, -1);
            if(isHandEmpty()){
                fillHand();
            }
        }
        return false;
    }

    protected void fillHand(){
        if(handcards.contains(-1)) {
            List<Integer> newHand = new ArrayList<>();
            for(int i=0;i<handcards.size();i++){
                if(handcards.get(i)!=-1){
                    newHand.add(handcards.get(i));
                }
            }
            newHand.addAll(cardPile.getCards(5-newHand.size()));
            handcards = newHand;
        }
    };

    protected void refillHand(){
        if(isHandEmpty()){
           handcards = cardPile.getCards(5);
        }
    }

    protected boolean isHandEmpty(){
        for(int i=0;i<handcards.size();i++){
            if(handcards.get(i)!=-1){
                return false;
            }
        }
        return true;
    }

    public boolean switchCard(int firstcard, int secondcard){
        int tussenvar = handcards.get(firstcard);
        handcards.set(firstcard, handcards.get(secondcard));
        handcards.set(secondcard, tussenvar);
        return false;
    }


    public PlayPile[] getPlayPiles() {
        return playPiles;
    }

    public PutAwayPile[] getPutAwayPiles() {
        return putAwayPiles;
    }

    public StockPile getStockpile() {
        return stockpile;
    }

    public List<Integer> getHandcards() {
        return handcards;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean hasWon(){
        return (stockpile.getAmount()==0);
    }
}
