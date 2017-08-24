package com.asai24.golf.domain;

public class ScoreDetailObj {
	
	private String shotResult;
	private int shotNumber;
	private String club;
	private double lat;
	private double lng;
	
	public void setShotResult(String shotResult) {
		this.shotResult = shotResult;
	}
	public String getShotResult() {
		return shotResult;
	}
	public void setShotNumber(int shotNumber) {
		this.shotNumber = shotNumber;
	}
	public int getShotNumber() {
		return shotNumber;
	}
	public void setClub(String club) {
		this.club = club;
	}
	public String getClub() {
		return club;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLng() {
		return lng;
	}
	
	

}
