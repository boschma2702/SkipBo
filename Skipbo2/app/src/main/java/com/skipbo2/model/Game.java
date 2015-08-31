package com.skipbo2.model;




public class Game implements Runnable{

    private CardPile cardpile;
    private PlayPile p1;
    private PlayPile p2;
    private PlayPile p3;
    private PlayPile p4;

    private Player[] humanPlayers;
    private PlayPile[] playPiles;
    private int current = 0;
    private boolean running;
    private boolean gameover;
    private int playercount;

    public Game(String[] names, int stockAmount, GameController c){
        cardpile = new CardPile(12);
        p1 = new PlayPile(cardpile);
        p2 = new PlayPile(cardpile);
        p3 = new PlayPile(cardpile);
        p4 = new PlayPile(cardpile);
        playPiles = new PlayPile[4];
        playPiles[0] = p1;
        playPiles[1] = p2;
        playPiles[2] = p3;
        playPiles[3] = p4;

        playercount = names.length;
        humanPlayers = new Player[names.length];
        for(int i = 0; i<names.length; i++){
            humanPlayers[i] = new Player(names[i], cardpile, stockAmount, c, playPiles);
        }

        //players[0] = new Player(name1, cardpile);
        //players[1] = new Player(name2, cardpile);

        new Thread(this).start();
    }


    @Override
    public void run() {
        running = true;
        gameover = false;
        while (running) {
            reset();
            play();
        }
    }

    private void reset() {
        gameover = false;
    }



    private void play() {
        while (!gameover) {
            humanPlayers[current].makeMove();
            current = (current + 1) % playercount;
        }
    }

    public void gameWon(){
        gameover = true;
    }


    public PlayPile[] getPlayPiles(){
        return playPiles;
    }

    public Player getCurrentPlayer(){
        return humanPlayers[current];
    }

    public Player[] getPlayers(){
        return humanPlayers;
    }


}
