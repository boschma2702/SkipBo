package com.skipbo.model;

import android.util.Log;

import com.skipbo.GameController;

import java.util.ArrayList;
import java.util.List;

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
	
	
	public void makeMove(GameController gameController){
        gameController.makeMove();
        //Log.e("test", "move ended");
        fillHand();
	}
	

}
