package com.skipbo.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.skipbo.view.GameView;

/**
 * Created by reneb_000 on 9-3-2015.
 */
public abstract class Card {

    static final int WIDTH = 200;
    static final int HEIGHT = 320;
    //static final int PADDING = 10;
    static final float SCALE = (float) 1;



    protected int x, y;

    protected Bitmap png, emptyPNG;
    protected int currentValue;
    protected int previousValue;

    private int number;
    protected boolean selected;
    protected boolean moveable;

    protected CardDecoder decoder;
    protected int cardWidth, cardHeight;

    public Card(int x, int y, int number, CardDecoder decoder, int startValue){
        this.x = x;
        this.y = y;
        Bitmap b = decoder.getStandardBitmap(0);
        cardWidth = b.getWidth();
        cardHeight = b.getHeight();
        currentValue = startValue;
        previousValue = startValue;
        this.decoder = decoder;
        png = getB(startValue);
        emptyPNG = decoder.getStandardBitmap(13);


        this.number = number;
        this.selected = false;
        moveable = false;
    }

    public void updatePNG(){
        currentValue = getCurrentValue();
        png = getB(currentValue);
        previousValue = currentValue;
    }
    public void onDraw(Canvas c) {

        c.drawBitmap(emptyPNG, x,y,null);
        if(previousValue==getCurrentValue()){
            c.drawBitmap(png, x,y,null);
        }else{
            updatePNG();
            c.drawBitmap(png,x,y,null);
        }
    }

    public Bitmap getB(int i){
        if(i==-1){
            return decoder.getStandardBitmap(13);
        }else {
            return decoder.getStandardBitmap(i);
        }
    }
    public abstract int getCurrentValue();

    public boolean contains(int x, int y){
        boolean xcompare = (x>=this.x)&&(x<=cardWidth+this.x);
        boolean ycompare = (y>=this.y)&&(y<=cardHeight+this.y);
        return xcompare&&ycompare;
    }


    public int getNumber(){
        return number;
    }


    public boolean getSelected(){
        return selected;
    }

    public void setSelected(boolean value){
        selected  = value;
    }


    public abstract boolean setMoveAble();

    public boolean getMoveable(){
        return moveable;
    }

}
