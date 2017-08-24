package com.asai24.golf.domain;

public class GiftPendingObj {
	private String id;
	private String playerName;
	private long sendAtDate;
	private String toMail;
	private String message;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public long getSendAtDate() {
		return sendAtDate;
	}
	public void setSendAtDate(long sendAtDate) {
		this.sendAtDate = sendAtDate;
	}
	public String getToMail() {
		return toMail;
	}
	public void setToMail(String toMail) {
		this.toMail = toMail;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
