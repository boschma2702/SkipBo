package com.skipbo.menus;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skipbo.GameController;
import com.skipbo.R;
import com.skipbo.model.players.BossInfo;
import com.skipbo.network.Client.Client;
import com.skipbo.network.Server.Server;
import com.skipbo.view.BossView;
import com.skipbo.view.LobbyEntry;

public class MainActivity extends Activity {

    //TODO menu for client when joining, more then 2 player multiplayer,
    // background ingame, settings page, bosses need to be unlocked,
    // more bosses, proper display for smaller screen devices, sideview putawaypiles expandle,
    // boss menu look, tutorial
    //TODO bugg with fill hand when server plays hand empty, client doesnt refill
    //TODO popup einde spel
    //TODO online customizable name
    //TODO client server interaction   2805



    private Button onlineButton;
    //private Button localButton;
    public static final String serverIP = "192.168.0.111";
    private int screenWidht, screenHeight;
    private Server server;
    private boolean toStop = false;
    private GameController controller;
    private Client client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        setContentView(R.layout.activity_main);
        Display d = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getRealSize(p);
        if(p.x>p.y) {
            screenWidht = p.x;
            screenHeight = p.y;
        }else{
            screenWidht = p.y;
            screenHeight = p.x;
        }

        Log.e("TEST", "width: "+screenWidht+" Height: "+screenHeight);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        findViewById(R.id.onlineButton).startAnimation(animation);
        findViewById(R.id.startTest).startAnimation(animation);
        findViewById(R.id.mainJoinButton).startAnimation(animation);
        findViewById(R.id.mainLocalButton).startAnimation(animation);



        Button start = (Button)findViewById(R.id.startTest);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller = new GameController(new String[]{"Astrid","René"}, MainActivity.this, screenWidht, screenHeight, 30);
                setContentView(controller.getView());
                //MainView view = new MainView(MainActivity.this, new String[]{"Astrid","René"}, p.x, p.y);
                //setContentView(view);
            }
        });

        /*
        Button join = (Button)findViewById(R.id.mainJoinButton);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Client(MainActivity.this, screenWidht, screenHeight);
            }
        });*/




    }

    public void toServerLayout(){
        server = new Server(this, (LinearLayout)findViewById(R.id.serverLobbyLin), screenWidht,screenHeight, this);
        TextView infoView = (TextView) findViewById(R.id.serverInfoLabel);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        infoView.setText("IP: "+ server.getIp()+" "+ "PORT: "+server.getSocket().getLocalPort());
        Button startGameButton = (Button)findViewById(R.id.serverStartGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server.startGame();
            }
        });
    }

    public void toLocalMenu(View v){
        setContentView(R.layout.menu_local);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        findViewById(R.id.localCampaign).startAnimation(animation);
        findViewById(R.id.localQuick).startAnimation(animation);
        findViewById(R.id.localBack).startAnimation(animation);
    }

    public void toLocalStartGame(View v){
        setContentView(R.layout.menu_quickgame);
        ((TextView)findViewById(R.id.localInput)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    addLobbyEntry(null);
                }
                return false;
            }
        });
    }

    public void addLobbyEntry(View v){
        String name = ((TextView)findViewById(R.id.localInput)).getText().toString();
        LobbyEntry entry = new LobbyEntry(this, name, true);
        ((LinearLayout) findViewById(R.id.lobbyLinearLayout)).addView(entry);
        ((TextView)findViewById(R.id.localInput)).setText("");
    }

    public void localStartGame(View v){
        LinearLayout lin = ((LinearLayout) findViewById(R.id.lobbyLinearLayout));
        if(lin.getChildCount()<2){
            Toast.makeText(this, "You need at least 2 players", Toast.LENGTH_SHORT).show();
        }else{
            String[] players = new String[lin.getChildCount()];
            for(int i=0;i<lin.getChildCount();i++){
                players[i] = (((LobbyEntry) lin.getChildAt(i)).getName());
            }
            int stockpileAmount = Integer.parseInt(((EditText)findViewById(R.id.localStockpileAmount)).getText().toString());
            GameController controller = new GameController(players, this, screenWidht, screenHeight, stockpileAmount);
            this.setContentView(controller.getView());
        }
    }

    public void localRemoveFromLobby(View v){
        LinearLayout lin = ((LinearLayout) findViewById(R.id.lobbyLinearLayout));
        lin.removeView(v);
    }

    public void toMainMenu(View v){
        setContentView(R.layout.activity_main);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        findViewById(R.id.onlineButton).startAnimation(animation);
        findViewById(R.id.startTest).startAnimation(animation);
        findViewById(R.id.mainJoinButton).startAnimation(animation);
        findViewById(R.id.mainLocalButton).startAnimation(animation);
    }



    public void toOnlineMenu(View v){
        setContentView(R.layout.menu_online);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        findViewById(R.id.onlineHost).startAnimation(animation);
        findViewById(R.id.onlineJoin).startAnimation(animation);
        findViewById(R.id.onlineBack).startAnimation(animation);
        //toServerLayout();

    }

    public void toHostMenu(View v){
        setContentView(R.layout.server_layout);
        toServerLayout();

    }

    public void exit(View v){
        shutdown();
    }

    public void toCampaign(View v){
        setContentView(R.layout.menu_campaign);
        LinearLayout scrollView = (LinearLayout) findViewById(R.id.campaignScrollView);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss_idiot);
        scrollView.addView(new BossView(this, bmp, BossInfo.BOSS1_NAME, BossInfo.BOSS1_DESC));
        scrollView.addView(new BossView(this, BitmapFactory.decodeResource(getResources(), R.drawable.boss_baby), BossInfo.BOSS2_NAME, BossInfo.BOSS2_DESC));
        scrollView.addView(new BossView(this, BitmapFactory.decodeResource(getResources(), R.drawable.boss_evil), BossInfo.BOSS3_NAME, BossInfo.BOSS3_DESC));
        scrollView.addView(new BossView(this, bmp, "Second Boss", "This is the second boss"));

    }

    public void toJoinMenu(View v){
        client = new Client(MainActivity.this, screenWidht, screenHeight);
    }

    public void exitCampaign(View v){
        setContentView(R.layout.menu_local);
    }

    public void exitServer(View v){
        setContentView(R.layout.menu_online);
        server.shutDown();
    }

    public void exitQuickgame(View v){
        setContentView(R.layout.menu_local);
    }

    public int getDPasPixels(int sizeInDp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (sizeInDp*scale + 0.5f);
    }

    public int getScreenWidht(){
        return screenWidht;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

    @Override
    public void onBackPressed() {
        if(toStop){
            this.shutdown();
        }else {
            Toast.makeText(this, "Press again to stop the application", Toast.LENGTH_SHORT).show();
            toStop = true;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toStop = false;
                }
            }, 2000);
        }

    }

    private void shutdown(){
        if(controller!=null){
            controller.shutdown();
        }
        if(server!=null){
            server.shutDown();
        }
        if(client!=null){
            client.shutDown();
        }
        this.finish();
    }




}
