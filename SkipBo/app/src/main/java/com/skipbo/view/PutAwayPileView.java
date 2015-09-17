package com.skipbo.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.skipbo.model.Player;
import com.skipbo.model.PutAwayPile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 16-3-2015.
 */
public class PutAwayPileView extends MoveableCard {

    private PutAwayPile p;
    private List<Integer> previousList;
    private List<Bitmap> bitmaps;
    private Player humanPlayer;

    private CardDecoder decoder;
    private boolean sideView;
    private Paint font;

    private float scale = (float) 0.125;

    private boolean expanded = false;

    public PutAwayPileView(PutAwayPile p, int number, CardDecoder decoder) {
        super(number*(PADDING+decoder.getCardWidth())+((decoder.getCardWidth()+PADDING)*2+PADDING*8), 0, number, decoder, p.getCard());
        moveable = false;
        this.p = p;
        setMoveAble();
        previousList = new ArrayList<Integer>();
        bitmaps = new ArrayList<Bitmap>();
        this.decoder = decoder;
        sideView = false;
    }

    public PutAwayPileView(Player humanPlayer, PutAwayPile p, int number, CardDecoder resizedDecoder){
        super(0,0, number, resizedDecoder, p.getCard());
        moveable = false;
        this.p = p;
        setMoveAble();
        previousList = new ArrayList<Integer>();
        bitmaps = new ArrayList<Bitmap>();
        this.decoder = resizedDecoder;
        sideView = true;
        this.humanPlayer = humanPlayer;
        font = new Paint();
        Typeface typeface = Typeface.create("Serif", Typeface.BOLD);
        font.setTextSize(30);
        font.setTypeface(typeface);
        font.setColor(Color.WHITE);
    }


    public void onDraw(Canvas c, int drawx, int drawy){
        if(sideView &&getNumber()==0){
            c.drawText(humanPlayer.getName(),drawx, drawy-5,font);
        }
        c.drawBitmap(emptyPNG,drawx,drawy,null);
        List<Integer> l = p.getCards();
        if(l.size()!=0){
            if(!(previousList.equals(l))) {
                updatePNG();
            }

            if(!expanded) {
                if (bitmaps.size() >= 5) {
                    int start = bitmaps.size() - 5;
                    for (int i = bitmaps.size() - 5; i < bitmaps.size() - 1; i++) {
                        c.drawBitmap(bitmaps.get(i), drawx, drawy + (decoder.getCardHeight() * (i - start) * scale), null);
                    }
                    if(sideView) {
                        c.drawBitmap(bitmaps.get(bitmaps.size() - 1), drawx, drawy + (decoder.getCardHeight() * (bitmaps.size() - 1 - start) * scale), null);
                    }else{
                        c.drawBitmap(bitmaps.get(bitmaps.size() - 1), x, y + (decoder.getCardHeight() * (bitmaps.size() - 1 - start) * scale), null);
                    }
                } else {
                    for (int i = 0; i < bitmaps.size() - 1; i++) {
                        c.drawBitmap(bitmaps.get(i), drawx, drawy + (decoder.getCardHeight() * i * scale), null);
                    }
                    if(sideView) {
                        c.drawBitmap(bitmaps.get(bitmaps.size() - 1), drawx, drawy + (decoder.getCardHeight() * (bitmaps.size() - 1) * scale), null);
                    }else{
                        c.drawBitmap(bitmaps.get(bitmaps.size() - 1), x, y + (decoder.getCardHeight() * (bitmaps.size() - 1) * scale), null);
                    }
                }
            }else{
                for (int i = 0; i < bitmaps.size() - 1; i++) {
                    c.drawBitmap(bitmaps.get(i), drawx, drawy + (decoder.getCardHeight() * i * scale), null);
                }
                if(sideView) {
                    c.drawBitmap(bitmaps.get(bitmaps.size() - 1), drawx, drawy + (decoder.getCardHeight() * (bitmaps.size() - 1) * scale), null);
                }else{
                    c.drawBitmap(bitmaps.get(bitmaps.size() - 1), x, y + (decoder.getCardHeight() * (bitmaps.size() - 1) * scale), null);
                }
            }

        }
    }

    @Override
    public void onDraw(Canvas c){
        //float scale = calcScale();
        onDraw(c, startx, starty);
    }
/*
    private float calcScale() {
        if(bitmaps.size()<=5){
            return (float) 0.125;
        }else{
            return (float) (0.2-(0.1-0.05*Math.pow(0.5,bitmaps.size()-5)));
        }

    }*/

    @Override
    public void updatePNG(){
        previousList.clear();
        previousList.addAll(p.getCards());
        bitmaps.clear();
        for(int i =0; i<previousList.size(); i++){
            Bitmap b = this.getB(previousList.get(i));

            bitmaps.add(b);
        }
    }

    @Override
    public int getCurrentValue() {
        return p.getCard();
    }

    @Override
    public boolean contains(int x, int y){
        //float scale = calcScale();
        boolean xcompare = (x>=this.x)&&(x<=decoder.getCardWidth()+this.x);
        boolean ycompare;
        if(!expanded) {
            if(bitmaps.size()>=5) {
                ycompare = (y >= this.y + (decoder.getCardHeight() * 4 * scale)) && (y <= decoder.getCardHeight() + this.y + (decoder.getCardHeight() * 4 * scale));
            }else{
                ycompare = (y >= this.y + (decoder.getCardHeight() * (bitmaps.size() - 1) * scale)) && (y <= decoder.getCardHeight() + this.y + (decoder.getCardHeight() * (bitmaps.size() - 1) * scale));

            }
        }else {
            ycompare = (y >= starty && y <=starty+decoder.getCardHeight()+(decoder.getCardHeight()*scale*bitmaps.size()));
        }
        return xcompare&&ycompare;
    }

    @Override
    public boolean setMoveAble() {
        moveable = p.getCards().size()>0;
        return moveable;
    }

    public void toggleExpanded(){
        if(expanded){
            expanded = false;
        }else{
            expanded = true;
        }
    }
}
