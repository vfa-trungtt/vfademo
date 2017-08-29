package com.asai24.golf.domain;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;

import java.util.ArrayList;

public class PlayerObj {
	
	private long id;
	private String name;
	private long ownner_flag;
	private String email;
	
	private String idServer;
	private String realUser;
	private long sentAtDate;
	private String sentId;
	private String playerHdcp;

    private String playerGoal;

    public String getLivePlayerId() {
        return livePlayerId;
    }

    public void setLivePlayerId(String livePlayerId) {
        this.livePlayerId = livePlayerId;
    }

    private String livePlayerId;
	
	public String getSentId() {
		return sentId;
	}
	public void setSentId(String sentId) {
		this.sentId = sentId;
	}
	private ArrayList<ScoreObj> arrScoreObj;
	private boolean displayAsFriend;
	private ErrorServer errorStatus;
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setOwnner_flag(long ownner_flag) {
		this.ownner_flag = ownner_flag;
	}
	public long getOwnner_flag() {
		return ownner_flag;
	}
	public void setArrScoreObj(ArrayList<ScoreObj> arrScoreObj) {
		this.arrScoreObj = arrScoreObj;
	}
	public ArrayList<ScoreObj> getArrScoreObj() {
		return arrScoreObj;
	}
	public void setIdServer(String idServer) {
		this.idServer = idServer;
	}
	public String getIdServer() {
		return idServer;
	}
	public void setRealUser(String realUser) {
		this.realUser = realUser;
	}
	public String getRealUser() {
		return realUser;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public boolean getDisplayAsFriend() {
		return displayAsFriend;
	}
	public void setDisplayAsFriend(boolean displayAsFriend) {
		this.displayAsFriend = displayAsFriend;
	}
	public long getSentAtDate() {
		return sentAtDate;
	}
	public void setSentAtDate(long sentAtDate) {
		this.sentAtDate = sentAtDate;
	}
	public ErrorServer getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(ErrorServer errorStatus) {
		this.errorStatus = errorStatus;
	}
	public String getPlayerHdcp() {
		return playerHdcp;
	}
	public void setPlayerHdcp(String playerHdcp) {
		this.playerHdcp = playerHdcp;
	}

    public String getPlayerGoal() {
        try {
            int goal = Integer.parseInt(playerGoal);
            return String.valueOf(goal);
        } catch (Exception e) {
            return String.valueOf(Constant.DEFAULT_GOAL_VALUE);
        }
    }

    public void setPlayerGoal(String playerGoal) {
        this.playerGoal = playerGoal;
    }
}
