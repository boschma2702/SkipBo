package com.skipbo.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 19-9-2015.
 */
public class ListMan {

    public static List<Integer> mirrorList(List<Integer> list){
        List<Integer> result = new ArrayList<>();
        for(int i=list.size()-1; i>=0; i--){
            result.add(list.get(i));
        }
        return result;
    }

}
