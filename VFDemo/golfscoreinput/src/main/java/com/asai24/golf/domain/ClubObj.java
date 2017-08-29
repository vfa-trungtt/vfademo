package com.asai24.golf.domain;

import java.io.Serializable;
import java.util.List;

public class ClubObj implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private long id; //NAMLH 2012.05.14 fixed
	private String extId;
	private String extType;
	private String clubName;
	private String address;
	private String country;
	private String state;
	private String city;
	private String url;
	private String phoneNumber;
	private String type;
	private long distance;
	private String rating;
	private double lat;
	private double lng;
	private List<Course> courses;

    private List<GolfDayClub> golfDayClubs;
	private String idServer;
	private String rakutenId;

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
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




	public String getExtType() {
		return extType;
	}

	public void setExtType(String extType) {
		this.extType = extType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public ClubObj clone() throws CloneNotSupportedException {
		return (ClubObj) super.clone();
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getExtId() {
		return extId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setIdServer(String idServer) {
		this.idServer = idServer;
	}

	public String getIdServer() {
		return idServer;
	}

	public String getRakutenId() {
		return rakutenId;
	}

	public void setRakutenId(String rakuteId) {
		this.rakutenId = rakuteId;
	}


    public List<GolfDayClub> getGolfDayClubs() {
        return golfDayClubs;
    }

    public void setGolfDayClubs(List<GolfDayClub> golfDayClubs) {
        this.golfDayClubs = golfDayClubs;
    }
}
