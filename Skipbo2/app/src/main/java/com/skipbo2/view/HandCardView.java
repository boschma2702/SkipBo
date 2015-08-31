package com.skipbo2.view;


import com.skipbo2.model.Player;

/**
 * Created by reneb_000 on 9-3-2015.
 */
public class HandCardView extends MoveableCard {

    private int positionNumber;
    //private int value;
    private Player humanPlayer;

    public HandCardView(int value, int positionNumber, Player humanPlayer, CardDecoder decoder) {
        super(positionNumber*(PADDING + decoder.getCardWidth()), (int)(decoder.getScreenHeight()-decoder.getCardHeight()), positionNumber, decoder, value);
        //this.currentValue = value;
        this.humanPlayer = humanPlayer;
        this.positionNumber = positionNumber;
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
