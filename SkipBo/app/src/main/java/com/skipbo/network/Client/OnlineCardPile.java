package com.skipbo.network.Client;

import android.util.Log;

import com.skipbo.model.CardPile;
import com.skipbo.network.Client.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by reneb_000 on 27-7-2015.
 */
public class OnlineCardPile implements CardPile {

    List<Integer> toAdd = new ArrayList<>();
    private boolean waiting = true;
    @Override
    public List<Integer> getCards(int amount) {
        Log.e("CARDPILE", "PLAYER ASKS NEW CARDS");
        synchronized (this){
            try {
                Log.e("CARDPILE", "BEFORE WAITING");
                waiting = true;
                wait();
                waiting = false;
                Log.e("CARDPILE", "AFTER WAITING, (SOMETHING RECEIVED IN CARDPILE)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return toAdd;
    }

    @Override
    public void add(List<Integer> cards) {

    }
    //TODO deadlock happens hear (with great certainly)
    //
    public void received(String[] list){
        Log.e("TEST", "NEW CARDS RECEIVED");
        List<Integer> l = new ArrayList<>();
        for(int i=1;i<list.length; i++){
            l.add(Integer.parseInt(list[i]));
            toAdd = l;
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("CARDPILE", "BEFORE SYNC BLOCK OF CARDPILE");
        //while(waiting){
            synchronized (this){
                Log.e("CARDPILE", "ABOUT TO NOTIFY ");
                notifyAll();
            }

        //}



    }
}
