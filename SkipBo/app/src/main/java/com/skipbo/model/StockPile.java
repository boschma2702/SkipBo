package com.skipbo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StockPile extends Observable {
	
	private List<Integer> cards = new ArrayList<Integer>();;
	
	public StockPile(CardPile a){
        cards.addAll(a.getCards(30));
	}

    public StockPile(List<Integer> array){
        cards = new ArrayList<>();
        cards.addAll(array);
    }

	public StockPile(CardPile a, int cardCount){
		cards.addAll(a.getCards(cardCount));
	}
	
	public int getCard(){
		if(cards.size()!=0){
			return cards.get(cards.size()-1);
		}return -1;
	}

	public int playCard(){
		int card = getCard();
		cards.remove(cards.size()-1);
        setChanged();
        notifyObservers();
		return card;
	}

	public int getAmount(){
		return cards.size();
	}

    public List<Integer> getCards(){
        return cards;
    }
}
