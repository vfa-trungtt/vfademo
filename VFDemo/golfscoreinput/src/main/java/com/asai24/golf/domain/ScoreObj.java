package com.asai24.golf.domain;

import java.util.ArrayList;

public class ScoreObj {
	
	private int holeScore;
	private int holeNumber;
	private String gameScore;
	private String fairway;
	private String teeOffClub;
	private int sandShot;
	private int waterHazard;
	private int ob;
	private boolean puttDisabled;
	
	private ArrayList<ScoreDetailObj> arrScoreDetailObj;
	
	public void setHoleScore(int holeScore) {
		this.holeScore = holeScore;
	}
	public int getHoleScore() {
		return holeScore;
	}
	public void setHoleNumber(int holeNumber) {
		this.holeNumber = holeNumber;
	}
	public int getHoleNumber() {
		return holeNumber;
	}
	public void setGameScore(String gameScore) {
		this.gameScore = gameScore;
	}
//	public int getGameScore() {
//		return gameScore;
//	}
	public String getGameScore() {
		return gameScore;
	}
	public String getFairway() {
		return fairway;
	}
	public void setFairway(String fairway) {
		this.fairway = fairway;
	}
	public String getTeeOffClub() {
		return teeOffClub;
	}
	public void setTeeOffClub(String teeOffClub) {
		this.teeOffClub = teeOffClub;
	}
	public int getSandShot() {
		return sandShot;
	}
	public void setSandShot(int sandShot) {
		this.sandShot = sandShot;
	}
	public int getWaterHazard() {
		return waterHazard;
	}
	public void setWaterHazard(int waterHazard) {
		this.waterHazard = waterHazard;
	}
	public int getOb() {
		return ob;
	}
	public void setOb(int ob) {
		this.ob = ob;
	}
	public void setArrScoreDetailObj(ArrayList<ScoreDetailObj> arrScoreDetailObj) {
		this.arrScoreDetailObj = arrScoreDetailObj;
	}
	public ArrayList<ScoreDetailObj> getArrScoreDetailObj() {
		return arrScoreDetailObj;
	}
	public boolean isPuttDisabled() {
		return puttDisabled;
	}
	public void setPuttDisabled(boolean puttDisabled) {
		this.puttDisabled = puttDisabled;
	}
	

}
