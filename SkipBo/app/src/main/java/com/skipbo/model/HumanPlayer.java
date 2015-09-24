package com.skipbo.model;

import com.skipbo.GameController;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name){
        this.name = name;
    }

	public HumanPlayer(String name, LocalCardPile cardpile, PlayPile[] playPiles){
        this(name, cardpile,30, playPiles);
	}
	
	public HumanPlayer(String name, LocalCardPile cardpile, int stockPileAmount, PlayPile[] playPiles){
		super(playPiles, new StockPile(cardpile, stockPileAmount), name, cardpile);
	}
	
	
	public boolean makeMove(GameController gameController){
        gameController.makeMove();
        //Log.e("test", "move ended");
        fillHand();
        return hasWon();
	}
	

}
