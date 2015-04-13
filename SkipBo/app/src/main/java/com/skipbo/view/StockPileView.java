package com.skipbo.view;

import com.skipbo.model.StockPile;

/**
 * Created by reneb_000 on 3-4-2015.
 */
public class StockPileView extends MoveableCard {

    StockPile stockpile;

    public StockPileView(StockPile p, int width, int height, CardDecoder decoder){
        super(1500,500, 1, decoder, p.getCard());
        stockpile = p;

    }


    @Override
    public int getCurrentValue() {
        return stockpile.getCard();
    }

    @Override
    public boolean setMoveAble() {
        return stockpile.getAmount()>0;
    }
}
