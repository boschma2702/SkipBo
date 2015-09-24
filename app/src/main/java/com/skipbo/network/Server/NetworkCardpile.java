package com.skipbo.network.Server;

import com.skipbo.model.CardPile;
import com.skipbo.model.LocalCardPile;
import com.skipbo.network.Sendable;
import com.skipbo.network.Server.CommandList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 26-7-2015.
 */
public class NetworkCardpile  extends LocalCardPile{

    //private List<Integer> pile;
    private Sendable sendable;

    public NetworkCardpile(Sendable sendable){
        super();
        this.sendable = sendable;
    }

    //TODO if hand of server is card_empty, need to send new hand to client.
    @Override
    public List<Integer> getCards(int amount) {
        return sendCards(super.getCards(amount));
    }

    private List<Integer> sendCards(List<Integer> list){
        String command = CommandList.SEND_CARDPILECARDS;
        for (int i = 0; i<list.size();i++){
            command+="$"+list.get(i);
        }
        sendable.sendMessage(command);
        return list;
    }
}
