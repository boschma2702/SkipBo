package com.skipbo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.skipbo.R;

/**
 * Created by reneb_000 on 5-4-2015.
 */
public class CardDecoder {

    private Context context;
    private Bitmap[] standardCards;
    private Bitmap[] resizedCards;

    private Matrix matrix;
    private float scale;

    private int screenWidth;
    private int screenHeight;

    private int cardWidth;
    private int cardHeight;

    public final static int BACK = 14;
    public final static int SKIPBO = 0;
    public static final int empty = 13;

    public CardDecoder(Context context, float scale, int screenWidht, int screenHeight){
        this.context = context;
        matrix = new Matrix();
        matrix.setScale(scale, scale);
        fillStandardCards();
        this.scale = scale;
        this.screenWidth = screenWidht;
        this.screenHeight = screenHeight;
    }

    /**
     * 0 = card_skipbo, 1....12 = cards, 13 = card_empty, 14 = back.
     */
    private void fillStandardCards() {
        standardCards = new Bitmap[15];
        Bitmap b;
        for(int i = 0;i<=14; i++){
            b=getBitMap(i);
            Log.e("TEST", "bmp width: "+b.getWidth()+" height: "+b.getHeight());
            Log.e("SIZE","Scale "+scale);
            standardCards[i] = Bitmap.createBitmap(b, 0,0,b.getWidth(),b.getHeight(),matrix,false);
        }
        cardHeight = standardCards[0].getHeight();
        cardWidth = standardCards[0].getWidth();

    }

    public Bitmap getStandardBitmap(int card){
        return standardCards[card];
    }


    private Bitmap getBitMap(int c){
        switch(c){
            case(1):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_een);
            case(2):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_twee);
            case(3):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_drie);
            case(4):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_vier);
            case(5):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_vijf);
            case(6):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_zes);
            case(7):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_zeven);
            case(8):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_acht);
            case(9):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_negen);
            case(10):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_tien);
            case(11):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_elf);
            case(12):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_twaalf);
            case(13):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_empty);
            case(0):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_skipbo);
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.card_back);
    }

    public float getScale() {
        return scale;
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

    public int getCardWidth(){
        return cardWidth;
    }

    public int getCardHeight(){
        return cardHeight;
    }
}
