package com.skipbo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import com.skipbo.Game;
import com.skipbo.R;
import com.skipbo.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by reneb_000 on 20-2-2015.
 */
public class GameView {

    private static final int initW = 1080;

    private PlayPileView[] playpiles;
    private Game g;
    private Map<Player, HandCardView[]> handcardsMap;
    private Map<Player, PutAwayPileView[]> putawaypilesMap;
    private Map<Player, StockPileView> stockpilesMap;

    public GameView(Game g, Context context, int width, int height){
        this.g = g;
        handcardsMap = new HashMap<Player, HandCardView[]>();
        putawaypilesMap = new HashMap<Player, PutAwayPileView[]>();
        stockpilesMap = new HashMap<Player, StockPileView>();

        CardDecoder decoder = new CardDecoder(context, width/initW);

        Player[] players = g.getPlayers();
        for(int x = 0; x< players.length; x++) {
            //fill handcards
            HandCardView[] handCards = new HandCardView[players[x].getHandcards().size()];
            for (int i = 0; i < handCards.length; i++) {
                handCards[i] = new HandCardView(players[x].getHandcards().get(i), i, players[x],width, height, decoder);
            }
            handcardsMap.put(players[x],handCards);
            //fill putawaypiles
            PutAwayPileView[] putAwayPiles = new PutAwayPileView[players[x].getPutAwayPiles().length];
            for(int i=0; i<putAwayPiles.length;i++){
                putAwayPiles[i] = new PutAwayPileView(players[x].getPutAwayPiles()[i], i, width, height, decoder);
            }
            putawaypilesMap.put(players[x],putAwayPiles);

            stockpilesMap.put(players[x],new StockPileView(players[x].getStockPile(),width, height, decoder));

        }
        playpiles = new PlayPileView[g.getPlayPiles().length];
        for(int i=0; i<playpiles.length;i++){
            playpiles[i] = new PlayPileView(g.getPlayPiles()[i], i, width,height, decoder);
        }
    }

    public void onDraw(Canvas c){
        //c.drawBitmap(getResizedBitmap(getBitMap(-1), 100,125),0,0,null);
        Player p = g.getCurrentPlayer();
        Card selectedCard = null;

        for(int i =0; i<playpiles.length; i++){
                playpiles[i].onDraw(c);
        }

        PutAwayPileView[] putAwayPiles = putawaypilesMap.get(p);
        for(int i=0; i<putAwayPiles.length;i++){
            //putAwayPiles[i] = new PutAwayPileView(g.getCurrentPlayer().getPutAwayPiles()[i], i);
            if(!putAwayPiles[i].getSelected()) {
                putAwayPiles[i].onDraw(c);
            }else{
                selectedCard=putAwayPiles[i];
            }
        }

        HandCardView[] handCards = handcardsMap.get(p);
        for(int i =0; i<handCards.length; i++){
            if(!handCards[i].getSelected()) {
                handCards[i].onDraw(c);
            }else{
                selectedCard = handCards[i];
            }
        }
        StockPileView stockpileview = stockpilesMap.get(p);
        if(!stockpileview.getSelected()){
            stockpileview.onDraw(c);
        }else{
            selectedCard = stockpileview;
        }

        //putAwayPiles = new PutAwayPileView[g.getCurrentPlayer().getPutAwayPiles().length];
        if(selectedCard!=null){
            selectedCard.onDraw(c);
        }
    }
    /*
    public HandCardView[] getHandCards(){
        return handCards;
    }*/

    public Card getObject(int x, int y){
        for(int i =0; i<playpiles.length; i++){
            if(playpiles[i].contains(x,y)&&!(playpiles[i].getSelected())){
                return playpiles[i];
            }
        }
        //putAwayPiles = new PutAwayPileView[g.getCurrentPlayer().getPutAwayPiles().length];
        Player p = g.getCurrentPlayer();
        PutAwayPileView[] putAwayPiles = putawaypilesMap.get(p);
        for(int i=0; i<putAwayPiles.length;i++){
            //putAwayPiles[i] = new PutAwayPileView(g.getCurrentPlayer().getPutAwayPiles()[i], i);
            if(putAwayPiles[i].contains(x,y)&&!(putAwayPiles[i].getSelected())){

                return putAwayPiles[i];
            }
        }
        HandCardView[] handCards = handcardsMap.get(p);
        for(int i =0; i<handCards.length; i++){
            if(handCards[i].contains(x,y)&&!(handCards[i].getSelected())){
                return handCards[i];
            }
        }
        StockPileView stockpileview = stockpilesMap.get(p);
        if(stockpileview.contains(x,y)&&!(stockpileview.getSelected())){
            stockpileview.setSelected(true);
            return stockpileview;
        }
        return null;
    }

    public MoveableCard getMoveableCard(int x, int y){
        Player p = g.getCurrentPlayer();
        PutAwayPileView[] putAwayPiles = putawaypilesMap.get(p);
        HandCardView[] handCards = handcardsMap.get(p);
        StockPileView stockpileview = stockpilesMap.get(p);
        //Log.e("value handcardssize","Size is "+ handCards[4].getCurrentValue());
        for(int i=0; i<putAwayPiles.length;i++){
            //putAwayPiles[i] = new PutAwayPileView(g.getCurrentPlayer().getPutAwayPiles()[i], i);
            if(putAwayPiles[i].contains(x,y)&&putAwayPiles[i].setMoveAble()&&!(putAwayPiles[i].getSelected())){
                putAwayPiles[i].setSelected(true);
                return putAwayPiles[i];
            }
        }
        for(int i =0; i<handCards.length; i++){
            if(handCards[i].contains(x,y)&&handCards[i].setMoveAble()&&!(handCards[i].getSelected())){
                handCards[i].setSelected(true);
                return handCards[i];
            }
        }
        if(stockpileview.contains(x,y)&&!(stockpileview.getSelected())){
            stockpileview.setSelected(true);
            return stockpileview;
        }

        return null;
    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
