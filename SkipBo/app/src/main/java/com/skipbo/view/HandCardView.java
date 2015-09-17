package com.skipbo.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.skipbo.model.HumanPlayer;
import com.skipbo.model.Player;

/**
 * Created by reneb_000 on 9-3-2015.
 */
public class HandCardView extends MoveableCard {

    private int positionNumber;
    //private int value;
    private Player humanPlayer;
    private Bitmap backCard;

    public HandCardView(int value, int positionNumber, Player humanPlayer, CardDecoder decoder) {
        super(positionNumber*(PADDING + decoder.getCardWidth()), (decoder.getScreenHeight()-decoder.getCardHeight()), positionNumber, decoder, value);
        //this.currentValue = value;
        this.humanPlayer = humanPlayer;
        this.positionNumber = positionNumber;
        backCard = decoder.getStandardBitmap(CardDecoder.BACK);
        setMoveAble();
    }

    @Override
    public int getCurrentValue() {
        //Log.e("waarde", "Handcard waarde is "+player.getHandcards().get(positionNumber));
        return humanPlayer.getHandcards().get(positionNumber);
    }
    /*
    public void setValue(int value){
        previousValue = currentValue;
        this.currentValue = value;
    }*/



    @Override
    public boolean setMoveAble() {
        moveable = getCurrentValue()!=-1;
        //Log.e("MOVEABLE","movable is "+moveable);
        return moveable;
    }


    public void onDrawEmpty(Canvas c){
        if(getCurrentValue()==-1){
            c.drawBitmap(emptyPNG, startx, starty, null);
        }else {
            c.drawBitmap(backCard, startx, starty, null);
        }
    }

    /*
    @Override
    public void onDraw(Canvas c) {
       c.drawBitmap(emptyPNG, startx, starty, null);
       if(previousValue==currentValue){
           c.drawBitmap(png, x,y,null);
       }else{
           updatePNG();
           c.drawBitmap(png,x,y,null);
       }
    }*/


}
