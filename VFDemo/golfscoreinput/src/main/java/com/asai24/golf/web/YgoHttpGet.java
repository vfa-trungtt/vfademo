package com.asai24.golf.web;

import com.asai24.golf.GolfApplication;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by CanNC on 11/13/15.
 */
public class YgoHttpGet extends HttpGet {

    public YgoHttpGet() {
        super();
        setDefaultHeaders();
    }

    public YgoHttpGet(java.net.URI uri) {
        super(uri);
        setDefaultHeaders();
    }

    public YgoHttpGet(String uri) {
        super(uri);
        setDefaultHeaders();
    }

    private void setDefaultHeaders() {
        setHeader("User-Agent", "YourGolf/" + GolfApplication.getVersionName() + " " + System.getProperty("http.agent"));
    }
}
