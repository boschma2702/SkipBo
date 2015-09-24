package com.skipbo.model.players;

import android.util.Log;

import com.skipbo.model.CardPile;
import com.skipbo.model.PlayPile;

/**
 * Created by reneb_000 on 16-8-2015.
 */
public class BossInfo {

    public static final String BOSS1_NAME = "Otto";
    public static final String BOSS1_DESC = "Otto the stupid, IQ of 50";

    public static final String BOSS2_NAME = "Tom";
    public static final String BOSS2_DESC = "Tom the baby, only three months old and no idea what he is doing";

    public static final String BOSS3_NAME = "Baron";
    public static final String BOSS3_DESC = "Baron von Mustachewich, some say the evilness is in his mustache";

    public static final String BOSS4_NAME = "Joe";
    public static final String BOSS4_DESC = "Average Joe, ";

    public static ComputerPlayer getComputerPlayer(String name, CardPile cardPile, PlayPile[] playPiles){
        switch (name){
            case BOSS1_NAME:
                Log.e("TEST", "IDIOT PLAYER ADDED");
                return new IdiotPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
            case BOSS2_NAME:
                return new BabyPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
            case BOSS3_NAME:
                return new AssPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
            case BOSS4_NAME:
                Log.e("TEST", "nomral PLAYER ADDED");
                return new NormalPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
        }
        return null;
    }

    //TODO normal player
    //TODO bad luck brian
    //TODO lucky brian




}
