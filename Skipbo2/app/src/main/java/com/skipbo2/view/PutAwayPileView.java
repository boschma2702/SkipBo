package com.skipbo2.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.skipbo.model.Player;
import com.skipbo.model.PutAwayPile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 16-3-2015.
 */
public class PutAwayPileView extends MoveableCard {

    //private boolean moveable;
    private PutAwayPile p;
    private List<Integer> previousList;
    private List<Bitmap> bitmaps;
    private Player humanPlayer;

    private CardDecoder decoder;
    private boolean drawName;
    private Paint font;

    public PutAwayPileView(PutAwayPile p, int number, CardDecoder decoder) {
        super(number*(PADDING+WIDTH)+500, 0, number, decoder, p.getCard());
        moveable = false;
        this.p = p;
        setMoveAble();
        previousList = new ArrayList<Integer>();
        bitmaps = new ArrayList<Bitmap>();
        this.decoder = decoder;
        drawName = false;
    }

    public PutAwayPileView(Player humanPlayer, PutAwayPile p, int number, CardDecoder resizedDecoder, int x, int y){
        super(number*(PADDING+resizedDecoder.getCardWidth())+x, y, number, resizedDecoder, p.getCard());
        moveable = false;
        this.p = p;
        setMoveAble();
        previousList = new ArrayList<Integer>();
        bitmaps = new ArrayList<Bitmap>();
        this.decoder = resizedDecoder;
        drawName = true;
        this.humanPlayer = humanPlayer;
        font = new Paint();
        font.setTextSize(15);
        font.setColor(Color.WHITE);
    }



    @Override
    public void onDraw(Canvas c){
        //c.drawBitmap(decoder.getBitMap(13), startx, starty, null);
        if(drawName&&getNumber()==0){
            c.drawText(humanPlayer.getName(),startx, starty-20,font);
        }
        c.drawBitmap(emptyPNG,startx,starty,null);
        List<Integer> l = p.getCards();
        if(l.size()!=0){
            if(!(previousList.equals(l))) {
                updatePNG();
            }
            for (int i = 0; i < bitmaps.size() - 1; i++) {
                c.drawBitmap(bitmaps.get(i), startx, starty + (decoder.getCardHeight() * i/5), null);
            }
            c.drawBitmap(bitmaps.get(bitmaps.size()-1), x, y+(decoder.getCardHeight() * (bitmaps.size()-1)/5), null);
        }
    }

    @Override
    public void updatePNG(){
        previousList.clear();
        previousList.addAll(p.getCards());
        //Log.e("putawaypile","PreviousList is geupdate "+previousList);
        bitmaps.clear();
        for(int i =0; i<previousList.size(); i++){
            //Bitmap b = GameView.getBitMap(previousList.get(i));
            //Bitmap resizedBitmap = Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(), matrix,false);
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
        boolean xcompare = (x>=this.x)&&(x<=decoder.getCardWidth()+this.x);
        boolean ycompare = (y>=this.y+(decoder.getCardHeight()*(bitmaps.size()-1)/5))&&(y<=decoder.getCardHeight()+this.y+(decoder.getCardHeight()*(bitmaps.size()-1)/5));
        return xcompare&&ycompare;
    }

    @Override
    public boolean setMoveAble() {
        moveable = p.getCards().size()>0;
        //Log.e("","playpile moveable is "+moveable);
        return moveable;
    }
}
