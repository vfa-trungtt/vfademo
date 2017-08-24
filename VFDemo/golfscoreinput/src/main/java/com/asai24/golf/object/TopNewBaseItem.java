package com.asai24.golf.object;

import java.io.Serializable;

/**
 * Created by huynq on 12/21/16.
 */

public class TopNewBaseItem implements Serializable{
    // Error and Type
    public enum ViewType{
        VIEW_LIST_TYPE,
        VIEW_REPORT_TYPE,
        VIEW_DFP_TYPE,
        VIEW_APP_VADOR_TYPE
    }

    protected ViewType mType;

    public String getImageUrl(){
        return "";
    }

    public String getTitle(){
        return "";
    }

    public String getLink(){
        return "";
    }

    public String getRssDate(){
        return "";
    }

    public String getDescription(){
        return "";
    }


}
