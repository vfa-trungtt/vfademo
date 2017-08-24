package com.asai24.golf.object;

import java.util.ArrayList;

/**
 * Created by huynq on 12/15/16.
 */
public class ObRoundHistroy {
    private ArrayList<ObRoundHistoryItem> rounds;
    private int total;
    private int page;
    private int getSizeList(){
        return rounds.size();
    }
}
