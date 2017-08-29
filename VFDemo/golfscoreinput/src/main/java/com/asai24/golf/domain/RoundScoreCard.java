package com.asai24.golf.domain;

import com.asai24.golf.Constant.ErrorServer;

import java.io.Serializable;

public class RoundScoreCard implements Serializable{
	private String clubName;
	private String clubAddress;
	private String courseName;
	private String weather;
	private long playDate;
	private String score;
	private String memo;
	private String scorePutt;
    private String liveId;
    private String goraId;
    private boolean isPuttDisable;
	// NguyenTT-Use for ticket #16402
	private String agencyRequestId;
	private String course_id;

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getGoraId() {
        return goraId;
    }

    public void setGoraId(String goraId) {
        this.goraId = goraId;
    }

	public String getScorePutt() {
		return scorePutt;
	}
	public void setScorePutt(String scorePutt) {
		this.scorePutt = scorePutt;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	private ErrorServer errorStatus;
	public ErrorServer getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(ErrorServer errorStatus) {
		this.errorStatus = errorStatus;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getClubAddress() {
		return clubAddress;
	}
	public void setClubAddress(String clubAddress) {
		this.clubAddress = clubAddress;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public long getPlayDate() {
		return playDate;
	}
	public void setPlayDate(long playDate) {
		this.playDate = playDate;
	}

    public void setPuttDisable(boolean isPuttDisable) {
        this.isPuttDisable = isPuttDisable;
    }

    public boolean isPuttDisable() {
        return isPuttDisable;
    }

	public void setAgencyRequestId(String id) {
		this.agencyRequestId = id;
	}

	public String getAgencyRequestId() {
		return agencyRequestId;
	}

	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
}
