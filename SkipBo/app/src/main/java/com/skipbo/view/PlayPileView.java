package com.skipbo.view;

import com.skipbo.model.PlayPile;

/**
 * Created by reneb_000 on 9-3-2015.
 */
public class PlayPileView extends Card{

    private PlayPile p;
    private int number;

    public PlayPileView(PlayPile p, int number, int width, int height, CardDecoder decoder){
        super(((number%2)*(PADDING+WIDTH)),((int)(number/2))*(HEIGHT+PADDING),number, decoder, p.getTeller());
        this.p = p;
        this.number = number;
        //this.currentValue = p.getTeller();
        setMoveAble();

    }


    @Override
    public int getCurrentValue() {
        return p.getTeller();
    }

    @Override
    public boolean setMoveAble() {
        moveable = false;
        return moveable;
    }
}