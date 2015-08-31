package com.skipbo.network;

import android.util.Log;

import com.skipbo.model.CardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;
import com.skipbo.model.StockPile;
import com.skipbo.network.Server.CommandList;

/**
 * Created by reneb_000 on 25-7-2015.
 */
public abstract class NetworkPlayer extends Player {

    public NetworkPlayer(){

    }

    public NetworkPlayer(PlayPile[] playPiles, StockPile stockPile, String name, CardPile cardPile){
        super(playPiles, stockPile, name, cardPile);
    }



    @Override
    public boolean playStockPile(int number) {
        if(playPiles[number].isPlaceable(stockpile.getCard())) {
            sendMessage(CommandList.PLAY_STOCKPILE$+number);
        }
        return super.playStockPile(number);
    }

    @Override
    public boolean playPutawayToPlayPile(int from, int to) {
        if(playPiles[to].isPlaceable(putAwayPiles[from].getCard())){
            sendMessage(CommandList.PLAY_PUTAWAYPILE$+from+"$"+to);
        }
        return super.playPutawayToPlayPile(from, to);
    }

    @Override
    public boolean playHandToPutAway(int hand, int pile) {
        if(putAwayPiles[pile].addCard(handcards.get(hand))) {
            sendMessage(CommandList.PLAY_HAND_PUTAWAYPILE$+hand+"$"+pile);
            handcards.set(hand, -1);
            Log.e("TEST", name+" has played to putawaypile");
            return true;
        }
        return false;
    }

    @Override
    public boolean playHandToPlayPile(int hand, int pile) {
        if(playPiles[pile].isPlaceable(handcards.get(hand))){
            sendMessage(CommandList.PLAY_HAND_PLAYPILE$+hand+"$"+pile);
            playPiles[pile].addCard(handcards.get(hand));
            handcards.set(hand, -1);
            if(isHandEmpty()){
                Log.e("TEST", "Hand is card_empty");
                fillHand();
            }
        }
        return false;
    }

    @Override
    public boolean switchCard(int firstcard, int secondcard) {
        sendMessage(CommandList.PLAY_HAND_HAND$+firstcard+"$"+secondcard);
        return super.switchCard(firstcard, secondcard);
    }

    public abstract void sendMessage(String message);


}
