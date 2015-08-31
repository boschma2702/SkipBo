package com.skipbo.network.Server;

import android.util.Log;

import com.skipbo.GameController;
import com.skipbo.model.Game;
import com.skipbo.model.PlayPile;
import com.skipbo.model.Player;
import com.skipbo.model.PutAwayPile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by reneb_000 on 19-7-2015.
 */
public class ClientHandler extends Player implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    //private String name = "UNDEFINED";

    private boolean myTurn = false;
    private boolean nameSet = false;
    private boolean running = true;

    private Server server;



    //private Game game;


    private static final String TAG = "NETWORK";


    public ClientHandler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg){
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
            Log.i("NETWORK", "Send: "+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO only works with 2 players, move needs to be send to all peers.
    private void interpreter(String[] s){
        switch (s[0]){
            case CommandList.SEND_NAME:
                if(!nameSet) {
                    name = s[1];
                    Log.e("TEST", "Name set: "+s[1]);
                    nameSet = true;
                    server.addToLobby(name, true, this);
                }
                break;
            case CommandList.PLAY_HAND_HAND:
                this.switchCard(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                break;
            case CommandList.PLAY_HAND_PLAYPILE:
                this.playHandToPlayPile(Integer.parseInt(s[1]),Integer.parseInt(s[2]));
                break;
            case CommandList.PLAY_STOCKPILE:
                this.playStockPile(Integer.parseInt(s[1]));
                break;
            case CommandList.PLAY_HAND_PUTAWAYPILE:
                this.playHandToPutAway(Integer.parseInt(s[1]),Integer.parseInt(s[2]));
                synchronized (this){
                    notifyAll();
                }
                break;
            case CommandList.PLAY_PUTAWAYPILE:
                this.playPutawayToPlayPile(Integer.parseInt(s[1]),Integer.parseInt(s[2]));
                break;
            case CommandList.LEAVE_LOBBY:
                server.clientLeaves(this);
                break;

        }
    }

    @Override
    public void run() {
        try {
            while(running) {
                String incomming = reader.readLine();
                Log.i(TAG, "Received: " + incomming);
                interpreter(incomming.split("\\$"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void makeMove(GameController controller) {
        synchronized (this){
            try {
                Log.e("TEST", "Before Waiting of "+name);
                wait();
                fillHand();
                Log.e("TEST", "Turn "+name+" really finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Server getServer(){
        return server;
    }


    public void shutDown() {
        try {
            running = false;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
