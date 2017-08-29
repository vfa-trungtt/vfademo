package com.asai24.golf.domain;

import java.io.Serializable;

public class Hole implements Serializable {
	private static final long serialVersionUID = 1L;

	// handicapの値が不明
	public static final int HANDICAP_UNNOWN = -1;
	// parの値が不明
	public static final int PAR_UNKNOWN = -1;
	// yardの値が不明
	public static final int YARD_UNKNOWN = -1;

	public static final int TEMP_PAR = 4;
	public static final int TEMP_YARD = 0;

	private int holeNumber;
	private int par;
	private int womenPar;
	private int yard;
	private int handicap;
	private int womenHandicap;
	private double latitude;
	private double longitude;
    private boolean secret = false;

    public boolean getSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public int getHoleNumber() {
		return holeNumber;
	}

	public void setHoleNumber(int holeNumber) {
		this.holeNumber = holeNumber;
	}

	public int getPar() {
		return par;
	}

	public void setPar(int par) {
		this.par = par;
	}

	public int getYard() {
		return yard;
	}

	public void setYard(int yard) {
		this.yard = yard;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setWomenPar(int womenPar) {
		this.womenPar = womenPar;
	}

	public int getWomenPar() {
		return womenPar;
	}

	public void setWomenHandicap(int womenHandicap) {
		this.womenHandicap = womenHandicap;
	}

	public int getWomenHandicap() {
		return womenHandicap;
	}

	public void setHandicap(int handicap) {
		this.handicap = handicap;
	}

	public int getHandicap() {
		return handicap;
	}

	public boolean isBlank() {
		return (handicap == HANDICAP_UNNOWN && womenHandicap == HANDICAP_UNNOWN
				&& par == PAR_UNKNOWN && womenPar == PAR_UNKNOWN && yard == YARD_UNKNOWN);
	}
}
