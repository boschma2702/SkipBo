package com.skipbo.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.skipbo.model.StockPile;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by reneb_000 on 3-4-2015.
 */
public class StockPileView extends MoveableCard implements Observer {

    StockPile stockpile;
    private Bitmap backPNG;
    private int cardsLeft;
    private Paint font;
    private boolean sideView;

    public StockPileView(StockPile p, CardDecoder decoder, boolean sideView){
        super((decoder.getCardWidth()+PADDING)*6,decoder.getScreenHeight()-decoder.getCardHeight(), 1, decoder, p.getCard());
        stockpile = p;
        stockpile.addObserver(this);
        backPNG = decoder.getStandardBitmap(14);
        cardsLeft = stockpile.getAmount();
        font = new Paint();
        font.setColor(Color.WHITE);
        font.setTextSize(30*decoder.getScale());
        Typeface typeface = Typeface.create("Serif", Typeface.BOLD);
        font.setTypeface(typeface);
        this.sideView = sideView;
    }

    /*
    public StockPileView(StockPile p, CardDecoder resizedDecoder, int x, int y){
        super(x,y,1,resizedDecoder, p.getCard());
        stockpile = p;
        stockpile.addObserver(this);
        backPNG = decoder.getStandardBitmap(14);
        cardsLeft = stockpile.getAmount();
        font = new Paint();
        font.setColor(Color.WHITE);
        font.setTextSize(25*decoder.getScale());
        Typeface typeface = Typeface.create("Serif", Typeface.BOLD);
        font.setTypeface(typeface);
    }*/

    public void onDraw(Canvas c, int drawx, int drawy){
        c.drawText("Left: "+cardsLeft, drawx, drawy-5, font);
        if(stockpile.getAmount()<=1) {
            c.drawBitmap(emptyPNG, drawx, drawy, null);
        }else{
            c.drawBitmap(backPNG, drawx, drawy, null);
        }
        if(sideView){
            c.drawBitmap(png, drawx, drawy, null);
        }else {
            c.drawBitmap(png, x, y, null);
        }
    }

    @Override
    public void onDraw(Canvas c){
        onDraw(c, startx, starty);
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

    @Override
    public void update(Observable observable, Object data) {
        updatePNG();
    }
}
