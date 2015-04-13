package com.skipbo.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;

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

    public PutAwayPileView(PutAwayPile p, int number, int widht, int height, CardDecoder decoder) {
        super(number*(PADDING+WIDTH)+500, 0, number, decoder, p.getCard());
        moveable = false;
        this.p = p;
        setMoveAble();
        previousList = new ArrayList<Integer>();
        bitmaps = new ArrayList<Bitmap>();
    }


    @Override
    public void onDraw(Canvas c){
        //c.drawBitmap(decoder.getBitMap(13), startx, starty, null);
        c.drawBitmap(emptyPNG,startx,starty,null);
        List<Integer> l = p.getCards();
        /*
        if(l.size()>0) {
            for (int i = 0; i < l.size()-1; i++) {
                c.drawBitmap(getB(l.get(i)), startx, starty + (HEIGHT*decoder.getScale()* i/5), null);
            }
            c.drawBitmap(getB(l.get(l.size()-1)),x,y+(HEIGHT*decoder.getScale()*(l.size()-1)/5),null);
        }*/


        if(l.size()!=0){
            if(!(previousList.equals(l))) {
                updatePNG();
            }
            for (int i = 0; i < bitmaps.size() - 1; i++) {
                c.drawBitmap(bitmaps.get(i), startx, starty + (HEIGHT * i/5), null);
            }
            c.drawBitmap(bitmaps.get(bitmaps.size()-1), x, y+(HEIGHT * (bitmaps.size()-1)/5), null);


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
        boolean xcompare = (x>=this.x)&&(x<=cardWidth+this.x);
        boolean ycompare = (y>=this.y+(cardHeight*(bitmaps.size()-1)/5))&&(y<=cardHeight+this.y+(cardHeight*(bitmaps.size()-1)/5));
        return xcompare&&ycompare;
    }

    @Override
    public boolean setMoveAble() {
        moveable = p.getCards().size()>0;
        //Log.e("","playpile moveable is "+moveable);
        return moveable;
    }
}
