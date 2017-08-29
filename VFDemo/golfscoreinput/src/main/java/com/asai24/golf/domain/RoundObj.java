package com.asai24.golf.domain;

public class RoundObj {

	private String id;
	private long playDate;
	private String weather;
	private String updateAt;
	private int startHole;
	private String liveId;
    private String liveEntryId;
	public void setStartHole(int startHole) {
		this.startHole = startHole;
	}
	public int getStartHole() {
		return startHole;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setPlayDate(long playDate) {
		this.playDate = playDate;
	}
	public long getPlayDate() {
		return playDate;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWeather() {
		return weather;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	public String getUpdateAt() {
		return updateAt;
	}

    public String getLiveEntryId() {
        return liveEntryId;
    }
    public void setLiveEntryId(String liveEntryId) {
        this.liveEntryId = liveEntryId;
    }
    public String getLiveId() {
		return liveId;
	}
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}
	
	
}
