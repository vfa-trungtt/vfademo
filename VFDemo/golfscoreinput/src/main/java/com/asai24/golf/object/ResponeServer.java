package com.asai24.golf.object;

import com.asai24.golf.Constant;

/**
 * Created by huynq on 8/4/16.
 */
public class ResponeServer {
    private  String json;
    private Constant.ErrorServer errorServer;


    public ResponeServer(String json, Constant.ErrorServer errorServer) {
        this.json = json;
        this.errorServer = errorServer;
    }
    public ResponeServer() {}


    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Constant.ErrorServer getErrorServer() {
        return errorServer;
    }

    public void setErrorServer(Constant.ErrorServer errorServer) {
        this.errorServer = errorServer;
    }


}
