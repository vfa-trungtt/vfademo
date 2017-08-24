package com.asai24.golf.web;

import com.asai24.golf.GolfApplication;

import org.apache.http.client.methods.HttpPut;

/**
 * Created by CanNC on 11/13/15.
 */
public class YgoHttpPut extends HttpPut {

    public YgoHttpPut() {
        super();
        setDefaultHeaders();
    }

    public YgoHttpPut(java.net.URI uri) {
        super(uri);
        setDefaultHeaders();
    }

    public YgoHttpPut(String uri) {
        super(uri);
        setDefaultHeaders();
    }

    private void setDefaultHeaders() {
        setHeader("User-Agent", "YourGolf/" + GolfApplication.getVersionName() + " " + System.getProperty("http.agent"));
    }
}
