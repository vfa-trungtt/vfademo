package com.asai24.golf.domain;

import com.asai24.golf.Constant.ErrorServer;

public class ClubDownloadResult {

	private ErrorServer errorStatus;
	private ClubObj club;

	public ClubObj getClub() {
		return club;
	}
	
	public void setClub(ClubObj club) {
		this.club = club;
	}

	public ErrorServer getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(ErrorServer errorStatus) {
		this.errorStatus = errorStatus;
	}

}
