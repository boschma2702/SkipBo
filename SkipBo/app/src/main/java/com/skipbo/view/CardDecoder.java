package com.skipbo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

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

    public CardDecoder(Context context, float scale){
        this.context = context;
        matrix = new Matrix();
        matrix.setScale(scale, scale);
        fillStandardCards();
        this.scale = scale;
    }

    /**
     * 0 = skipbo, 1....12 = cards, 13 = empty, 14 = back.
     */
    private void fillStandardCards() {
        standardCards = new Bitmap[15];
        Bitmap b;
        for(int i = 0;i<=14; i++){
            b=getBitMap(i);
            standardCards[i] = Bitmap.createBitmap(b, 0,0,b.getWidth(),b.getHeight(),matrix,false);
        }

    }

    public Bitmap getStandardBitmap(int card){
        return standardCards[card];
    }


    public Bitmap getBitMap(int c){
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

    public float getScale() {
        return scale;
    }
}
