package com.asai24.golf.domain;

import com.asai24.golf.Constant.ErrorServer;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveInfo implements Serializable {
	private String id;
	private String title;
	private String playDate;
	private String weather;
	private String golfCourse;
	private String code;
	private String clubId;
	private boolean isAdmin;
	private String entryId;
	private String roundId;
	private String playerHdcp;
	private boolean useDoublePeoria;
	private String purchasedAt;
	private boolean doubleParCut;
	private boolean showNetScoreInRanking;
    private String photoURL;
	public boolean isShowNetScoreInRanking() {
		return showNetScoreInRanking;
	}
	public void setShowNetScoreInRanking(boolean showNetScoreInRanking) {
		this.showNetScoreInRanking = showNetScoreInRanking;
	}
	private ArrayList<MemberObj> members;
	private ArrayList<CourseLiveObj> courseLiveObjLst;
	private ErrorServer errorStatus;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClubId() {
		return clubId;
	}
	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlayDate() {
		return playDate;
	}
	public void setPlayDate(String playDate) {
		this.playDate = playDate;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getGolfCourse() {
		return golfCourse;
	}
	public void setGolfCourse(String golfCourse) {
		this.golfCourse = golfCourse;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public ErrorServer getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(ErrorServer errorStatus) {
		this.errorStatus = errorStatus;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	public ArrayList<MemberObj> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<MemberObj> members) {
		this.members = members;
	}
	public String getPlayerHdcp() {
		return playerHdcp;
	}
	public void setPlayerHdcp(String playerHdcp) {
		this.playerHdcp = playerHdcp;
	}
	public boolean isUseDoublePeoria() {
		return useDoublePeoria;
	}
	public void setUseDoublePeoria(boolean useDoublePeoria) {
		this.useDoublePeoria = useDoublePeoria;
	}
	public String getPurchasedAt() {
		return purchasedAt;
	}
	public void setPurchasedAt(String purchasedAt) {
		this.purchasedAt = purchasedAt;
	}
	public ArrayList<CourseLiveObj> getCourseLiveObjLst() {
		return courseLiveObjLst;
	}
	public void setCourseLiveObjLst(ArrayList<CourseLiveObj> courseLiveObjLst) {
		this.courseLiveObjLst = courseLiveObjLst;
	}
	public boolean isDoubleParCut() {
		return doubleParCut;
	}
	public void setDoubleParCut(boolean doubleParCut) {
		this.doubleParCut = doubleParCut;
	}

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
