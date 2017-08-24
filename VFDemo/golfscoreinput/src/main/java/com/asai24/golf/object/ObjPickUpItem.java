package com.asai24.golf.object;

import com.asai24.golf.Constant;

import java.io.Serializable;

/**
 * Created by huynq on 12/21/16.
 */
public class ObjPickUpItem implements Serializable {
   private String created_at;
   private String updated_at;
    private int id;
    private String summary;
    private String title;
    private String url;
    private String media_name;
    private String image;

    private Constant.ErrorServer errorServer;
    private ObjPickUp.ViewType viewType;
    // Advertisement
    private String apiKey;

    public ObjPickUpItem(ObjPickUp.ViewType viewType) {
        this.viewType = viewType;
    }

    public ObjPickUpItem(ObjPickUp.ViewType viewType, String key) {
        this.viewType = viewType;
        this.apiKey = key;
    }

    public ObjPickUpItem(Constant.ErrorServer errorServer) {
        this.errorServer = errorServer;
    }

    public ObjPickUp.ViewType isViewType() {
        return ObjPickUp.ViewType.VIEW_1;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // setViewType


    public Constant.ErrorServer getErrorServer() {
        return errorServer;
    }

    public void setErrorServer(Constant.ErrorServer errorServer) {
        this.errorServer = errorServer;
    }

    public ObjPickUp.ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ObjPickUp.ViewType viewType) {
        this.viewType = viewType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
