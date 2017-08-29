package com.asai24.golf.domain;

import java.io.Serializable;

public class LiveObj implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private String idServer;
	private String clubName;
	private String playDate;
	private String title;
    private String urlPhoto;
	
	public String getIdServer() {
		return idServer;
	}
	public void setIdServer(String idServer) {
		this.idServer = idServer;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getPlayDate() {
		return playDate;
	}
	public void setPlayDate(String playDate) {
		this.playDate = playDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public String getUrlPhoto(){return urlPhoto;}
    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
