package com.skipbo.model.players;

import android.util.Log;

import com.skipbo.model.CardPile;
import com.skipbo.model.PlayPile;

/**
 * Created by reneb_000 on 16-8-2015.
 */
public class BossInfo {

    public static final String BOSS1_NAME = "Otto";
    public static final String BOSS1_DESC = "Not bright of a person";

    public static final String BOSS2_NAME = "Baby";
    public static final String BOSS2_DESC = "He doesn't know what is he is doing";

    public static final String BOSS3_NAME = "Evil";
    public static final String BOSS3_DESC = "Meneast guy on earth";

    public static ComputerPlayer getComputerPlayer(String name, CardPile cardPile, PlayPile[] playPiles){
        switch (name){
            case BOSS1_NAME:
                return new IdiotPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
            case BOSS2_NAME:
                return new BabyPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
            case BOSS3_NAME:
                return new AssPlayer((com.skipbo.model.LocalCardPile) cardPile, 30, playPiles);
        }
        return null;
    }

    //TODO normal player
    //TODO bad luck brian
    //TODO lucky brian




}
