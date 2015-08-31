package com.skipbo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skipbo.R;
import com.skipbo.menus.MainActivity;
import com.skipbo.network.Server.ClientHandler;

/**
 * Created by reneb_000 on 22-7-2015.
 */
public class LobbyEntry extends LinearLayout {

    private String name;
    private ClientHandler clientHandler;

    public LobbyEntry(final MainActivity context, String name, boolean deletable, final ClientHandler clientHandler) {
        super(context);
        this.name = name;
        this.clientHandler = clientHandler;
        TextView text = new TextView(context);
        text.setTextColor(Color.parseColor("#000000"));
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        text.setText(name);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(text);
        this.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.getBackground().setAlpha(200);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.RIGHT;
        //this.setLayoutParams(params);
        View v = new View(context);
        v.setLayoutParams(params);
        this.addView(v);

        if(deletable) {
            TextView x = new TextView(context);
            x.setText("X");
            x.setTextColor(Color.parseColor("#000000"));
            x.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            x.setTypeface(null, Typeface.BOLD);

            x.setPadding(0, 0, 60, 0);

            this.addView(x);
            if(clientHandler==null) {
                x.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.localRemoveFromLobby(LobbyEntry.this);
                    }
                });
            }else{
                x.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clientHandler.getServer().deleteFromLobby(LobbyEntry.this);
                    }
                });
            }
        }

    }

    public String getName(){
        return name;
    }

    public ClientHandler getClientHandler(){
        return clientHandler;
    }
}
