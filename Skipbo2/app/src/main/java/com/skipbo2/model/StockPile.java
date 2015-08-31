package com.skipbo2.model;

import java.util.ArrayList;
import java.util.List;

public class StockPile {
	
	private List<Integer> cards;
	
	public StockPile(CardPile a){
		cards = new ArrayList<Integer>();
        cards.addAll(a.get30Cards());
	}

    public StockPile(List<Integer> array){
        cards = new ArrayList<>();
        cards.addAll(array);
    }

	public StockPile(CardPile a, int cardCount){
		cards = a.getCards(cardCount);
	}
	
	public int getCard(){
		if(cards.size()!=0){
			return cards.get(cards.size()-1);
		}return -1;
	}
	/*
	public boolean playCard(int value){
		if(getCard()==value||getCard()==0){
			cards.remove(cards.size()-1);
			return true;
		}else{
			return false;
		}
	}
	*/
	public int playCard(){
		int card = getCard();
		cards.remove(cards.size()-1);
		return card;
	}

	public int getAmount(){
		return cards.size();
	}
}
