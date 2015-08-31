package com.skipbo2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.skipbo2.R;

/**
 * Created by reneb_000 on 18-7-2015.
 */
public class CardDecoder {

    private Bitmap[] normalCards;
    private Bitmap[] smallerCards;
    private Context context;
    private int screenWidth, screenHeight;
    private Matrix matrix;
    private Matrix smallMatrix;

    private static final int DEFAULT_WIDHT = 1920;
    private static final int DEFAULT_HEIGHT = 1080;
    private int cardHeight;
    private int cardWidth;

    public CardDecoder(Context context, int screenWidth, int screenHeight){
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        matrix = new Matrix();
        matrix.setScale(screenWidth/DEFAULT_WIDHT, screenHeight/DEFAULT_HEIGHT);
        smallMatrix = new Matrix();
        smallMatrix.setScale(screenWidth/DEFAULT_WIDHT/10, screenHeight/DEFAULT_HEIGHT/10);
        fillCardsArrays();
        cardHeight = normalCards[0].getHeight();
        cardWidth = normalCards[0].getWidth();
    }

    public void fillCardsArrays(){
        normalCards = new Bitmap[15];
        smallerCards = new Bitmap[15];
        for(int i =0; i<15; i++){
            Bitmap b = getBitMap(1);
            normalCards[i] = Bitmap.createBitmap(b, 0,0,b.getWidth(),b.getHeight(),matrix,false);
            smallerCards[i] = Bitmap.createBitmap(b, 0,0,b.getWidth(),b.getHeight(),smallMatrix,false);
        }
    }

    public Bitmap[] getNormalCards() {
        return normalCards;
    }

    public Bitmap[] getSmallerCards() {
        return smallerCards;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private Bitmap getBitMap(int c){
        switch(c){
            case(1):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.een);
            case(2):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.twee);
            case(3):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.drie);
            case(4):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.vier);
            case(5):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.vijf);
            case(6):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.zes);
            case(7):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.zeven);
            case(8):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.acht);
            case(9):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.negen);
            case(10):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.tien);
            case(11):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.elf);
            case(12):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.twaalf);
            case(13):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.empty);
            case(0):
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.skipbo);
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }
}
