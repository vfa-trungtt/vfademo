package com.asai24.golf.web;

import com.asai24.golf.GolfApplication;

import org.apache.http.client.methods.HttpPost;

/**
 * Created by CanNC on 11/13/15.
 */
public class YgoHttpPost extends HttpPost {

    public YgoHttpPost() {
        super();
        setDefaultHeaders();
    }

    public YgoHttpPost(java.net.URI uri) {
        super(uri);
        setDefaultHeaders();
    }

    public YgoHttpPost(String uri) {
        super(uri);
        setDefaultHeaders();
    }

    private void setDefaultHeaders() {
        setHeader("User-Agent", "YourGolf/" + GolfApplication.getVersionName() + " " + System.getProperty("http.agent"));
    }
}
