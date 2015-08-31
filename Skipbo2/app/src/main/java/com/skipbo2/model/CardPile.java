package com.skipbo2.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CardPile {


    private List<Integer> pile = new ArrayList<Integer>();

    public CardPile(){
        this(12);
    }

    public CardPile(int aantal){
        for(int i = 0; i<=12; i++){
            for(int x = 0; x<aantal; x++){
                pile.add(i);
            }
        }
    }

    public int getRandomCard(){
        int random = (int)(Math.random() * pile.size());
        int returnCard = pile.get(random);
        pile.remove(random);
        return returnCard;
    }

    public List<Integer> get5Cards(){
        List<Integer> l = new ArrayList<Integer>();
        for(int i =0; i<5; i++){
            l.add(getRandomCard());
        }
        return l;
    }

    public List<Integer> get30Cards(){
        List<Integer> l = new ArrayList<Integer>();
        for(int i =0; i<30; i++){
            l.add(getRandomCard());
        }
        return l;
    }

    public List<Integer> getCards(int amount){
        List<Integer> l = new ArrayList<Integer>();
        for(int i =0; i<amount; i++){
            l.add(getRandomCard());
        }
        return l;
    }

    public List<Integer> getPile(){
        return pile;
    }

    public void add(List<Integer> cards) {
        pile.addAll(cards);
    }


}
