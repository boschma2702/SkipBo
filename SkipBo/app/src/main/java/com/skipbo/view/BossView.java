package com.skipbo.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skipbo.GameController;
import com.skipbo.R;
import com.skipbo.menus.MainActivity;

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
        this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, context.getResources().getDisplayMetrics());
        ImageView v = new ImageView(context);
        v.setImageBitmap(png);
        v.setMaxWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, context.getResources().getDisplayMetrics()));
        v.setScaleType(ImageView.ScaleType.FIT_CENTER);
        v.setAdjustViewBounds(true);
        this.addView(v);

        TextView title = new TextView(context);
        title.setText(name);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        TextView des = new TextView(context);
        des.setText(description);
        des.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics())));
        des.setTextColor(Color.parseColor("#FFFFFF"));
        des.setMaxEms(25);
        des.setSingleLine(false);
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
        des.setPadding(pad, 0, pad, 0);
        des.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

        this.addView(title);
        this.addView(des);

        Button button = new Button(context);
        button.setBackgroundResource(R.drawable.roundedbuttonfull);
        button.setText("Play");
        button.setTypeface(null, Typeface.BOLD);
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
