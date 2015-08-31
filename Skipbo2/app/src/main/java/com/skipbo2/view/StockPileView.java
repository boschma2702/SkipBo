package com.skipbo2.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.skipbo.model.StockPile;

/**
 * Created by reneb_000 on 3-4-2015.
 */
public class StockPileView extends MoveableCard {

    StockPile stockpile;
    private Bitmap backPNG;
    private int cardsLeft;
    private Paint font;

    public StockPileView(StockPile p, CardDecoder decoder){
        super((decoder.getCardWidth()+PADDING)*6,decoder.getScreenHeight()-decoder.getCardHeight(), 1, decoder, p.getCard());
        stockpile = p;
        backPNG = decoder.getStandardBitmap(14);
        cardsLeft = stockpile.getAmount();
        font = new Paint();
        font.setColor(Color.WHITE);
        font.setTextSize(20);
    }

    public StockPileView(StockPile p, CardDecoder resizedDecoder, int x, int y){
        super(x,y,1,resizedDecoder, p.getCard());
        stockpile = p;
        backPNG = decoder.getStandardBitmap(14);
        cardsLeft = stockpile.getAmount();
        font = new Paint();
        font.setColor(Color.WHITE);
        font.setTextSize(20*decoder.getScale());
    }

    @Override
    public void onDraw(Canvas c){
        c.drawText("Cards Left: "+cardsLeft, startx, starty-20, font);
        if(stockpile.getAmount()<=1) {
            c.drawBitmap(emptyPNG, startx, starty, null);
        }else{
            c.drawBitmap(backPNG, startx, starty, null);
        }
        if(previousValue==getCurrentValue()){
            c.drawBitmap(png, x,y,null);
        }else{
            updatePNG();
            c.drawBitmap(png,x,y,null);
        }
    }

    @Override
    public void updatePNG(){
        cardsLeft = stockpile.getAmount();
        currentValue = getCurrentValue();
        png = getB(currentValue);
        previousValue = currentValue;
    }

    @Override
    public int getCurrentValue() {
        return stockpile.getCard();
    }

    @Override
    public boolean setMoveAble() {
        return stockpile.getAmount()>0;
    }

    public StockPile getStockpile(){
        return stockpile;
    }
}
