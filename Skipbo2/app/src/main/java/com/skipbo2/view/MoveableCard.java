package com.skipbo2.view;


import android.graphics.Canvas;

/**
 * Created by reneb_000 on 9-3-2015.
 */
public abstract class MoveableCard extends Card {

    private int initialx, initialy;
    protected int startx, starty;



    public MoveableCard(int x, int y, int number, CardDecoder decoder, int startValue){
        super(x,y, number, decoder, startValue);
        //setMoveAble();
        startx = x;
        starty = y;
    }



    public void setCord(int x, int y, boolean b){
        if(getCurrentValue()!=-1) {
            if (!b) {
                initialx = this.x - x;
                initialy = this.y - y;
            } else {
                this.x = x + (initialx);
                this.y = y + (initialy);
            }
        }
    }

    @Override
    public void onDraw(Canvas c){
        //c.drawBitmap(decoder.getBitMap(13), startx, starty, null);
        //c.drawBitmap(getB(getCurrentValue()),x,y,null);
        c.drawBitmap(emptyPNG, startx,starty,null);
        if(previousValue==getCurrentValue()){
            c.drawBitmap(png, x,y,null);
        }else{
            updatePNG();
            c.drawBitmap(png,x,y,null);
        }
    }

    public void resetPos(){
        x= startx;
        y=starty;
    }



}
