package com.skipbo.network.Client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skipbo.GameController;
import com.skipbo.R;
import com.skipbo.menus.MainActivity;
import com.skipbo.model.Game;
import com.skipbo.model.Player;
import com.skipbo.network.NetworkPlayer;
import com.skipbo.network.Sendable;
import com.skipbo.network.Server.CommandList;
import com.skipbo.network.Server.Server;
import com.skipbo.view.LobbyEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 22-7-2015.
 */
public class Client extends NetworkPlayer implements Runnable, Sendable {

    private String ip;

    private MainActivity context;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean running = true;
    //private Activity activity;
    private int screenwidht, screenheight;
    private OnlineCardPile onlineCardPile;

    private static final String TAG = "NETWORK";
    private Game game;

    private boolean gameStarted = false;
    private LinearLayout lobby;

    private String incoming;

    public Client(MainActivity context, int screenwidht, int screenheight){
        //interperterClient = new InterperterClient(this);
        this.context = context;
        //this.activity = (Activity) context;
        this.screenwidht = screenwidht;
        this.screenheight = screenheight;
        //new Thread(this).start();
        //popup();


    }

    public void connect(String ip, String name){
        this.ip = ip;
        this.name = name;

        new Thread(this).start();


    }


    @Override
    public void run() {
        try {
            socket = new Socket(ip, Server.DEFAULT_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            running = true;
            sendMessage(CommandList.SEND_NAME$ + name);
        } catch (IOException e) {
            running = false;
            Handler handler = new Handler(context.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_LONG).show();
                }
            };
            handler.post(runnable);

            e.printStackTrace();

        }



        while (running) {
            try {
                incoming = reader.readLine();
                if(incoming!=null) {
                    Log.i(TAG, "Received: " + incoming);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            interpreter(incoming.split("\\$"));
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private void interpreter(String[] s) {
        switch (s[0]){
            case CommandList.SEND_GAMESTRING:
                startGame(s);
                break;
            case CommandList.SEND_CARDPILECARDS:
                if(gameStarted) {
                    onlineCardPile.received(s);
                }
                break;
            case CommandList.PLAY_HAND_HAND:
                if(gameStarted) {
                    game.getCurrentPlayer().switchCard(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
                break;
            case CommandList.PLAY_HAND_PLAYPILE:
                if(gameStarted) {
                    game.getCurrentPlayer().playHandToPlayPile(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
                break;
            case CommandList.PLAY_STOCKPILE:
                if(gameStarted) {
                    game.getCurrentPlayer().playStockPile(Integer.parseInt(s[1]));
                }
                break;
            case CommandList.PLAY_HAND_PUTAWAYPILE:
                if(gameStarted) {
                    game.getCurrentPlayer().playHandToPutAway(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                    synchronized (this) {
                        notifyAll();
                    }
                }
                break;
            case CommandList.PLAY_PUTAWAYPILE:
                if(gameStarted) {
                    game.getCurrentPlayer().playPutawayToPlayPile(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
                break;
            case CommandList.KICK_PLAYER:
                this.kicked();
                break;
            case CommandList.SEND_LOBBY:
                this.fillLobby(s);
                break;
        }
    }

    private void fillLobby(final String[] s) {
        Handler handler = new Handler(context.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                lobby.removeAllViews();
                for(int i =1;i<s.length;i++){
                    addToLobby(s[i]);
                }
            }
        };
        handler.post(runnable);

    }

    private void addToLobby(String s){
        lobby.addView(new LobbyEntry(context, s, false, null));
    }

    private void kicked() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Handler handler = new Handler(context.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "You have been kicked", Toast.LENGTH_LONG).show();
                context.setContentView(R.layout.menu_online);
            }
        };
        handler.post(runnable);


    }

    public void startGame(String[] s){
        List<Player> players = new ArrayList<>();
        List<List<Integer>> handcards = new ArrayList<>();
        List<List<Integer>> stockpiles = new ArrayList<>();
        List<Integer> subHand = new ArrayList<>();
        List<Integer> subStockpile = new ArrayList<>();
        int counter = 0;
        for(int i=1;i<s.length; i++){
            try {
                int number = Integer.parseInt(s[i]);
                if(counter<5){
                    subHand.add(number);
                }else{
                    subStockpile.add(number);
                }
                counter++;
            }catch (NumberFormatException e) {
                if(s[i].equals("í")){
                    counter = 0;
                    handcards.add(new ArrayList<Integer>(subHand));
                    stockpiles.add(new ArrayList<Integer>(subStockpile));
                    subHand.clear();
                    subStockpile.clear();
                }else if (s[i].equals(name)) {
                    players.add(this);
                } else {
                    players.add(new StubPlayer(s[i]));
                }
            }

        }

        Player[] p = new Player[players.size()];
        for(int i=0; i<players.size(); i++){
            p[i] = players.get(i);
        }
        final GameController controller = new GameController(p, context, screenwidht, screenheight, handcards, stockpiles);
        onlineCardPile = (OnlineCardPile)controller.getCardPile();
        Handler handler = new Handler(context.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                context.setContentView(controller.getView());
            }
        };
        handler.post(runnable);
        gameStarted = true;
        game = controller.getGame();

    }

    public void sendMessage(String msg){
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
            Log.d(TAG, "Send: "+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void popup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter IP and Name");

// Set up the input
        LinearLayout lin = new LinearLayout(context);
        lin.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(context);
        input.setHint("IP Adress");
        final EditText nameField = new EditText(context);
        nameField.setHint("Your Name");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        lin.addView(input);
        lin.addView(nameField);
        builder.setView(lin);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ip = input.getText().toString();
                name = nameField.getText().toString();
                synchronized (Client.this){
                    Client.this.notifyAll();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void shutDown(){
        if(socket!=null){
            try {
                running = false;
                sendMessage(CommandList.LEAVE_LOBBY);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean makeMove(GameController controller) {
        controller.makeMove();
        fillHand();
        return hasWon();
    }

    public boolean isGameStarted(){
        return gameStarted;
    }

    public String getIp() {
        return ip;
    }

    public Context getContext() {
        return context;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public boolean isRunning() {
        return running;
    }



    public int getScreenwidht() {
        return screenwidht;
    }

    public int getScreenheight() {
        return screenheight;
    }

    public OnlineCardPile getOnlineCardPile() {
        return onlineCardPile;
    }

    public Game getGame() {
        return game;
    }

    public void setLobby(LinearLayout lobby) {
        this.lobby = lobby;
    }
}
