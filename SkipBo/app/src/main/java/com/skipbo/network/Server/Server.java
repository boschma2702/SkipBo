package com.skipbo.network.Server;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skipbo.GameController;
import com.skipbo.R;
import com.skipbo.Utils.IP;
import com.skipbo.menus.MainActivity;
import com.skipbo.model.Game;
import com.skipbo.model.Player;
import com.skipbo.network.NetworkPlayer;
import com.skipbo.network.Sendable;
import com.skipbo.view.LobbyEntry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by reneb_000 on 19-7-2015.
 */
public class Server extends NetworkPlayer implements Runnable, Sendable{

    private ServerSocket serverSocket;
    public static final int DEFAULT_PORT = 60001;
    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private String ip;
    private boolean running = true;

    private LinearLayout lobby;
    //private List<LobbyEntry> persons = new ArrayList<>();

    private Activity activity;
    private MainActivity context;
    private GameController controller;

    private int screenWidth, screenHeight;

    private static final String TAG = "NETWORK";

    public Server(MainActivity context, LinearLayout lobby, int screenWidth, int screenHeight, Activity activity){
        this.context = context;
        this.lobby = lobby;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.activity = activity;
        this.name = "HOST";
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);

            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //addToLobby(name, false);
    }


    public void startGame(){
        Player[] players = new Player[clientHandlers.size()+1];
        players[0] = this;
        for(int i =0; i<clientHandlers.size(); i++){
            players[i+1] = clientHandlers.get(i);
        }
        int stockpileamount = Integer.parseInt(((EditText)context.findViewById(R.id.serverStockpileAmount)).getText().toString());
        controller = new GameController(players, context, screenWidth,screenHeight, new NetworkCardpile(this), stockpileamount);
        Handler handler = new Handler(context.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                activity.setContentView(controller.getView());
            }
        };
        handler.post(runnable);
        sendMessage(generateGameString());
    }

    public ServerSocket getSocket(){
        return serverSocket;
    }

    public String getIp(){
        return ip;
    }

    @Override
    public void run() {
        ip = IP.getIpAddress();
        try {
            while(running) {
                Socket socket = serverSocket.accept();
                Log.i(TAG, "Host accepted");
                clientHandlers.add(new ClientHandler(socket,this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToLobby(String name, final boolean deleteable, final ClientHandler clientHandler){
        Handler handler = new Handler(context.getMainLooper());
        final String n = name;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                lobby.addView(new LobbyEntry(context, n, deleteable, clientHandler));
            }
        };
        handler.post(runnable);
        listChanged();
    }

    public void clientLeaves(final ClientHandler clientHandler){
        final int place = clientHandlers.indexOf(clientHandler);
        Handler handler = new Handler(context.getMainLooper());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Player "+clientHandler.getName()+" has left", Toast.LENGTH_LONG).show();
                lobby.removeView(lobby.getChildAt(place));
            }
        };
        handler.post(runnable);
        clientHandlers.remove(clientHandler);
        listChanged();
    }

    public void deleteFromLobby(final LobbyEntry toDelete){
        toDelete.getClientHandler().sendMessage(CommandList.KICK_PLAYER);
        clientHandlers.remove(toDelete.getClientHandler());
        Handler handler = new Handler(context.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                lobby.removeView(toDelete);
            }
        };
        handler.post(runnable);
        listChanged();
    }



    @Override
    public void makeMove(GameController controller) {
        controller.makeMove();
        fillHand();
        Log.e("TEST", "Turn " + name + " really finished");
    }

    public void sendMessage(String msg){
        for(ClientHandler c :clientHandlers){
            c.sendMessage(msg);
        }
    }

    public void shutDown(){
        for(ClientHandler c:clientHandlers){
            c.shutDown();
        }
        try {
            running = false;
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * generates game string
     * @return string in format: {playerName}+$+{handCards}+$+{StockpileCards}+$í$+{playerName2}...
     */
    private String generateGameString(){
        Game g = controller.getGame();
        Player[] player = g.getPlayers();
        String master = CommandList.SEND_GAMESTRING$;
        for(int i=0; i<player.length; i++){
            master+=player[i].getName();
            List<Integer> hand = player[i].getHandcards();
            //adds handcards to masterString
            for(int x=0; x<hand.size(); x++){
                master+="$"+hand.get(x);
            }
            //adds stockpile cards
            List<Integer> pile = player[i].getStockpile().getCards();
            for(int x=0; x<pile.size(); x++){
                master+="$"+pile.get(x);
            }
            master+="$í$";
        }
        Log.e("TEST", "MasterString: "+master);
        return master;
    }

    private void listChanged(){
        String tosend = CommandList.SEND_LOBBY$;
        tosend +=name+"$";
        for(int i=0;i<clientHandlers.size();i++){
            tosend+=clientHandlers.get(i).getName()+"$";
        }
        sendMessage(tosend);
    }

    public int getPlayersCount(){
        return clientHandlers.size()+1;
    }
}
