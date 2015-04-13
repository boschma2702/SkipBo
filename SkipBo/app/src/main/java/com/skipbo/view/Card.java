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
    static final int PADDING = 10;
    static final float SCALE = (float) 1;

    //protected final int initWidth = 1080;

    protected int x, y;
    //protected final static int width = 100;
    //protected final static int height = 125;
    //protected Paint font;
    //protected Rect card;
    //protected Paint cardbox;
    protected Bitmap png, emptyPNG;
    protected int currentValue;
    protected int previousValue;
    //protected Matrix matrix;
    private int number;
    protected boolean selected;
    protected boolean moveable;
    //private int scale;
    protected CardDecoder decoder;
    protected int cardWidth, cardHeight;

    public Card(int x, int y, int number, CardDecoder decoder, int startValue){
        this.x = x;
        this.y = y;
        Bitmap b = decoder.getBitMap(0);
        cardWidth = b.getWidth();
        cardHeight = b.getHeight();
        currentValue = startValue;
        previousValue = startValue;
        this.decoder = decoder;
        //Bitmap b = GameView.getBitMap(currentValue);
        //png=Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(), matrix,false);
        //b=GameView.getBitMap(-1);
        //emptyPNG=Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(), matrix,false);
        png = getB(startValue);
        emptyPNG = decoder.getBitMap(13);


        this.number = number;
        this.selected = false;
        moveable = false;
    }

    public void updatePNG(){
        currentValue = getCurrentValue();
        //Bitmap b = GameView.getBitMap(currentValue);
        //png=Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(), matrix,false);
        png = getB(currentValue);
        previousValue = currentValue;
    }
    public void onDraw(Canvas c) {
        //c.drawBitmap(decoder.getBitMap(13), x, y, null);
        //c.drawBitmap(getB(getCurrentValue()),x,y,null);



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
            return decoder.getBitMap(13);
        }else {
            return decoder.getBitMap(i);
        }
    }
    public abstract int getCurrentValue();

    public boolean contains(int x, int y){
        boolean xcompare = (x>=this.x)&&(x<=cardWidth+this.x);
        boolean ycompare = (y>=this.y)&&(y<=cardHeight+this.y);
        return xcompare&&ycompare;
    }
    /*
    public void setValue(int value){
        this.currentValue = value;
    }*/

    //public abstract void setCurrentValue();

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
