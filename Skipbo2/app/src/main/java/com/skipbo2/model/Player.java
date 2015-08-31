package com.skipbo2.model;



import java.util.ArrayList;
import java.util.List;

public class Player  {

    private String name;
    private CardPile cardpile;
    private List<Integer> handcards;
    private StockPile stockpile;
    private boolean turnEnd;
    private PutAwayPile[] putAwayPiles = new PutAwayPile[4];
    private GameController gameController;
    private PlayPile[] playPiles;

    public Player(String name, CardPile cardpile, GameController c, PlayPile[] playPiles){
        this(name, cardpile,30, c, playPiles);
    }

    public Player(String name, CardPile cardpile, int stockPileAmount, GameController c, PlayPile[] playPiles){
        this.name = name;
        this.cardpile = cardpile;
        handcards = new ArrayList<Integer>();
        handcards.addAll(cardpile.get5Cards());
        this.cardpile = cardpile;
        stockpile = new StockPile(cardpile, stockPileAmount);
        this.gameController = c;


        putAwayPiles[0] = new PutAwayPile();
        putAwayPiles[1] = new PutAwayPile();
        putAwayPiles[2] = new PutAwayPile();
        putAwayPiles[3] = new PutAwayPile();
        this.playPiles = playPiles;

    }


    public void makeMove(){
        gameController.makeMove();
        //Log.e("test", "move ended");
        if(handcards.contains(-1)) {
            fillHand();
            /*
            for(int i =0; i<handcards.size(); i++){
               Log.e("","handcards size is "+handcards.size());
               if(handcards.get(i)==-1){
                   //handcards.add(i, cardpile.getRandomCard());
                    handcards.remove(i);
               }
           }
        }
        while(handcards.size()<=5){
            handcards.add(cardpile.getRandomCard());
        }*/
        }
        //return false;
    }

    private boolean checkEmptyHand(){
        for(int i=0; i<handcards.size();i++){
            if(handcards.get(i)!=-1){
                return false;
            }
        }
        return true;
    }

    private void fillEmptyHand(){
        if(checkEmptyHand()){
            fillHand();
        }
    }

    private void fillHand(){
        List<Integer> l = new ArrayList<Integer>();
        for(int i=0; i<handcards.size(); i++){
            if(handcards.get(i)!=-1){
                l.add(handcards.get(i));
            }
        }
        while(l.size()<5){
            l.add(cardpile.getRandomCard());
        }
        handcards = l;
    }

    public boolean playStockPile(int number){
        if(playPiles[number].isPlaceable(stockpile.getCard())){
            playPiles[number].addCard(stockpile.playCard());
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
            fillEmptyHand();
        }
        return false;
    }

    public boolean swichtCard(int firstcard, int secondcard){
        int tussenvar = handcards.get(firstcard);
        handcards.set(firstcard, handcards.get(secondcard));
        handcards.set(secondcard, tussenvar);
        return false;
    }

    public void checkHand(){
        if(handcards.size()==0){
            while(handcards.size()<5){
                handcards.add(cardpile.getRandomCard());
            }}
    }

    public String getName(){
        return name;
    }

    public List<Integer> getHandcards(){
        return handcards;
    }

    public void playCard(int card, PutAwayPile p){
        p.addCard(card);
    }

    public PutAwayPile[] getPutAwayPiles(){
        return putAwayPiles;
    }

    public StockPile getStockPile(){
        return stockpile;
    }

}
