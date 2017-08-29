package com.asai24.golf.object;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nguyen on 10/5/16.
 */
public class SearchTagEntity implements Serializable {
    private String keyword;
    private int numOfUse;

    public SearchTagEntity(String keyword){
        this.keyword = keyword;
        numOfUse = 1;
    }
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNumOfUse() {
        return numOfUse;
    }

    public void setNumOfUse(int numOfUse) {
        this.numOfUse = numOfUse;
    }

    public SearchTagEntity inScreaseNumOfUse(){
        numOfUse++;
        return this;
    }
    public static void sort(List<SearchTagEntity> list){
        Collections.sort(list, new Comparator<SearchTagEntity>() {
            @Override
            public int compare(SearchTagEntity mine, SearchTagEntity other) {
                if(mine.getNumOfUse() > other.getNumOfUse()){
                    return -1;
                }else if(mine.getNumOfUse() < other.getNumOfUse()){
                    return 1;
                }else{
                    return mine.getKeyword().compareToIgnoreCase(other.getKeyword());
                }
            }
        });
    }
}
