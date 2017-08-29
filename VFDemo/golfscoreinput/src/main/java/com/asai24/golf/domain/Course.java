package com.asai24.golf.domain;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String oobId="";
	private String yourgolfId="";
	private String clubName;
	private String courseName;
	private String address;
	private String country;
	private String url;
	private String phoneNumber;
	private List<Tee> tees;
	private long holes;
	private String idServer;

	public String getName() {
		if (courseName != null && courseName.length() != 0) {
			return clubName + " - " + courseName;
		} else {
			return clubName;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOobId() {
		return oobId;
	}

	public void setOobId(String oobId) {
		this.oobId = oobId;
	}

	public String getYourGolfId() {
		return yourgolfId;
	}

	public void setYourGolfId(String yourgolfId) {
		this.yourgolfId = yourgolfId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Tee> getTees() {
		return tees;
	}

	public void setTees(List<Tee> tees) {
		this.tees = tees;
	}

	@Override
	public Course clone() throws CloneNotSupportedException {
		return (Course) super.clone();
	}

	public void setHoles(long holes) {
		this.holes = holes;
	}

	public long getHoles() {
		return holes;
	}

	public void setIdServer(String idServer) {
		this.idServer = idServer;
	}

	public String getIdServer() {
		return idServer;
	}
}
