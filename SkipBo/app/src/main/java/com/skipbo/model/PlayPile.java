package com.skipbo.model;

import java.util.ArrayList;
import java.util.List;

public class PlayPile {

	private List<Integer> cards = new ArrayList<Integer>();
	
	private int teller = 0;
	private CardPile cardpile;
	
	public PlayPile(CardPile p){
		cardpile = p;
	}
	
	public boolean isPlaceable(int card){
		if(teller+1==card||card==0){
			return true;
		}else{
			return false;
		}
	}
	
	public void addCard(int card){
		//if(isPlaceable(card)==true){
			cards.add(card);
			teller ++;
			if(teller == 12){
				resetPile();
			//}
		}
	}
	
	public void resetPile(){
		teller = 0;
		cardpile.add(cards);
		cards.clear();
	}
	
	public int getTeller(){
		if(teller==0){
            return -1;
        }else {
            return teller;
        }
	}
}
