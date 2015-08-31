package com.skipbo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skipbo.GameController;
import com.skipbo.R;
import com.skipbo.menus.MainActivity;
import com.skipbo.model.players.BossInfo;

/**
 * Created by reneb_000 on 15-8-2015.
 */
public class BossView extends LinearLayout {

    public BossView(final MainActivity context, Bitmap png, final String name, String description){
        super(context);
        //LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //params.gravity = Gravity.CENTER;
        //this.setLayoutParams(params);

        this.setOrientation(VERTICAL);
        ImageView v = new ImageView(context);
        v.setImageBitmap(png);
        this.addView(v);

        TextView title = new TextView(context);
        title.setText(name);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        TextView des = new TextView(context);
        des.setText(description);
        des.setTextColor(Color.parseColor("#FFFFFF"));
        des.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

        this.addView(title);
        this.addView(des);

        Button button = new Button(context);
        button.setText("Play");
        button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GameController controller = new GameController(new String[]{"You", name}, context, context.getScreenWidht(), context.getScreenHeight(), 30);
                context.setContentView(controller.getView());
                return false;
            }
        });
        this.addView(button);
        int padding = context.getDPasPixels(30);
        this.setPadding(padding, padding, padding, padding);
    }



}
