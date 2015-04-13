package com.skipbo;

import com.skipbo.view.GameView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;



public class FullscreenActivity extends Activity{

    OurView v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        v = new OurView(this);

        setContentView(v);
    }




    public class OurView extends SurfaceView implements Runnable {


        //Thread t;
        SurfaceHolder holder;
        boolean running;
        GameController controller;
        //public int SCREENWIDTH,SCREENHEIGHT;

        public OurView(Context context) {
            super(context);
            holder = getHolder();
            running = true;

            Display d = getWindowManager().getDefaultDisplay();
            Point p = new Point();
            d.getRealSize(p);
            //SCREENHEIGHT = p.y;
            //SCREENWIDTH = p.x;

            String[] players = new String[]{"Astrid", "René"};

            if(p.x<p.y){
                controller = new GameController(players, context, p.x, p.y);
            }else{
                controller = new GameController(players, context, p.y, p.x);
            }

            new Thread(this).start();
            setOnTouchListener(controller);




        }

        @Override
        public void run() {
            while(running){
                if(!holder.getSurface().isValid()){
                    continue;
                }
                Canvas c = holder.lockCanvas();
                c.drawARGB(255, 255, 50, 50);
                //RectF oval = new RectF(x,y,x+100,y+100);
                //c.drawOval(oval, new Paint(Color.BLACK));
                controller.Draw(c);
                holder.unlockCanvasAndPost(c);
            }
        }



    }





}
