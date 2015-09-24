package com.skipbo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by reneb_000 on 5-9-2015.
 */
public class Settings {

    private boolean firstLaunch;
    private boolean vibrate;
    private SharedPreferences sharedPreferences;


    public Settings(Activity activity){
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        firstLaunch = sharedPreferences.getBoolean("firstLaunch", true);
        vibrate = sharedPreferences.getBoolean("vibrate",true);
    }

    public void setFirstLaunch(){
        firstLaunch = false;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstLaunch",false);
        editor.commit();
    }

    public void setVibrate(boolean value){
        vibrate = value;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vibrate",value);
        editor.commit();
    }

    public boolean getFirstLaunch(){
        return firstLaunch;
    }

    public boolean getVibrate(){
        return vibrate;
    }


}
