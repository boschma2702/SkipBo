package com.skipbo.tests;

import android.test.AndroidTestCase;

import com.skipbo.Utils.ListMan;
import com.skipbo.model.CardPile;
import com.skipbo.model.LocalCardPile;
import com.skipbo.model.PlayPile;
import com.skipbo.model.PutAwayPile;
import com.skipbo.model.players.ComputerPlayer;
import com.skipbo.model.players.IdiotPlayer;
import com.skipbo.model.players.NormalPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 18-9-2015.
 */
public class ComputerPlayerTest extends AndroidTestCase {

    NormalPlayer player;
    PlayPile[] playPiles = new PlayPile[4];
    PutAwayPile[] put = new PutAwayPile[4];
    LocalCardPile cardPile = new LocalCardPile();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        playPiles[1] = new PlayPile(cardPile);
        player = new NormalPlayer(cardPile, 30, playPiles);
        put[0] = new PutAwayPile(getList(new Integer[]{3,2,1}));
        put[1] = new PutAwayPile(getList(new Integer[]{8,6,5}));
        put[2] = new PutAwayPile(getList(new Integer[]{9}));
        put[3] = new PutAwayPile(getList(new Integer[]{3,2,1,10}));
        player.setPutAwayPiles(put);
        player.setHand(getList(new Integer[]{10,10,10,10,10}));
        //test();

    }

    public void test(){
        assertEquals("List mirroring", getList(new Integer[]{1,2,3}), ListMan.mirrorList(getList(new Integer[]{3, 2, 1})));
        assertEquals("Available putaway", getList(new Integer[]{2,3}),player.getPutawayBehindFirst(put[0]));
        assertEquals("Available putaway", getList(new Integer[]{6}),player.getPutawayBehindFirst(put[1]));
        assertEquals("Available putaway", getList(new Integer[]{}),player.getPutawayBehindFirst(put[2]));
        assertEquals("Available putaway", getList(new Integer[]{}),player.getPutawayBehindFirst(put[3]));
    }





    private List<Integer> getList(Integer[] array){
        List<Integer> result = new ArrayList<>();
        for(int i=0; i<array.length;i++){
            result.add(array[i]);
        }
        return result;
    }
}
