package com.asai24.golf.object;

/**
 * Created by huynq on 12/15/16.
 */
public class ObRoundHistoryItem {
    private String gora_score_id;
    private String weather;
    private String club_name;
    private String course_name;
    private String course_id;
    private String agency_request_id;
    private String id;
    private String live_id;
    private int total_shot;
    private int total_putt;
    private boolean putt_disabled;
    private String playdate;

    public ObRoundHistoryItem() {
    }

    public String getGora_score_id() {
        return gora_score_id;
    }

    public String getWeather() {
        return weather;
    }

    public String getClub_name() {
        return club_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getAgency_request_id() {
        return agency_request_id;
    }

    public String getId() {
        return id;
    }

    public String getLive_id() {
        return live_id;
    }

    public int getTotal_shot() {
        return total_shot;
    }

    public int getTotal_putt() {
        return total_putt;
    }

    public boolean isPutt_disabled() {
        return putt_disabled;
    }

    public String getPlaydate() {
        return playdate;
    }

    public void setGora_score_id(String gora_score_id) {
        this.gora_score_id = gora_score_id;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setAgency_request_id(String agency_request_id) {
        this.agency_request_id = agency_request_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public void setTotal_shot(int total_shot) {
        this.total_shot = total_shot;
    }

    public void setTotal_putt(int total_putt) {
        this.total_putt = total_putt;
    }

    public void setPutt_disabled(boolean putt_disabled) {
        this.putt_disabled = putt_disabled;
    }

    public void setPlaydate(String playdate) {
        this.playdate = playdate;
    }
}
