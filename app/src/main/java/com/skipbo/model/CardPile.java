package com.skipbo.model;

import java.util.List;

/**
 * Created by reneb_000 on 27-4-2015.
 */
public interface CardPile {

    public List<Integer> getCards(int amount);


    void add(List<Integer> cards);
}
