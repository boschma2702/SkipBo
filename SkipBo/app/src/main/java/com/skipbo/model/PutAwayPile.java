package com.skipbo.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PutAwayPile {

	
	private List<Integer> cards = new ArrayList<Integer>();

    public PutAwayPile(){};

    /**
     * Pure for testing
     * @param cards to be set onto the putawaypiles. The first one integer in the list is the highest
     *              card (not to be reached), the last integer is the first card on the putawaypile
     */
	public PutAwayPile(List<Integer> cards){
        this.cards = cards;
    }

	public boolean addCard(int card){
		if(card!=-1){
            cards.add(card);
            return true;
        }else{
            return false;
        }


	}


	public int getCard(){
		if(cards.size()==0){
            return -1;
        }else{
            return cards.get(cards.size()-1);
        }

	}
	
	public int deleteCard(){
		int tussenvar = cards.get(cards.size()-1);
		cards.remove(cards.size()-1);
		return tussenvar;
	}
	
	public List<Integer> getCards(){
        return cards;
	}
	
}
