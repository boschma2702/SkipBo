package com.skipbo.view;

import com.skipbo.model.Player;

/**
 * Created by reneb_000 on 9-3-2015.
 */
public class HandCardView extends MoveableCard {

    private int positionNumber;
    //private int value;
    private Player player;

    public HandCardView(int value, int positionNumber, Player player, int width, int height, CardDecoder decoder) {
        super(positionNumber*(PADDING + WIDTH)+200, (int)(width-(320*(decoder.getScale()))), positionNumber, decoder, value);
        //this.currentValue = value;
        this.player = player;
        this.positionNumber = positionNumber;
        setMoveAble();
    }

    @Override
    public int getCurrentValue() {
        //Log.e("waarde", "Handcard waarde is "+player.getHandcards().get(positionNumber));
        return player.getHandcards().get(positionNumber);
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
