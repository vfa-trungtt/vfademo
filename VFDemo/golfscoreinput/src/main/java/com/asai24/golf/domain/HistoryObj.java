package com.asai24.golf.domain;



import com.asai24.golf.adapter.AdapterFragmentHistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class HistoryObj implements Serializable {
//    public static enum TYPE_HISTORY{
//        HEADER,
//        ITEMS,
//        FOOTER
//    }

	AdapterFragmentHistory.TYPE_HISTORY viewType;
	private String id;
	private String club_name;
    private String course_name;
    private String course_id;
	private int total_shot;
	private int total_putt;
	private long playdate;
	private boolean puttDisabled;
	/*ThuNA 2013/03/06 ADD -S*/
	private String gora_score_id;
	private String liveId;
    // CanNC - Agency score request - 2015/01/06
    private String agencyRequestId;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    /*ThuNA 2013/03/06 ADD -E*/
    private String weather;
	// for header
	private ArrayList<HashMap<String, String>> hashResult;
	private String messageGift;
	// footer
	private int nextPage;

	public AdapterFragmentHistory.TYPE_HISTORY getViewType() {
		return viewType;
	}

	public void setViewType(AdapterFragmentHistory.TYPE_HISTORY viewType) {
		this.viewType = viewType;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClubName() {
		return club_name;
	}
	public void setClub_name(String round_name) {
		this.club_name = round_name;
	}

    public String getCourse_id() {
        return course_id;
    }
    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }
    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

	public int getTotalShot() {
		return total_shot;
	}
	public void setTotal_shot(int total_shot) {
		this.total_shot = total_shot;
	}
	public int getTotalPutt() {
		return total_putt;
	}
	public void setTotal_putt(int total_putt) {
		this.total_putt = total_putt;
	}
	public long getPlayDate() {
		return playdate;
	}
	public void setPlaydate(long playdate) {
		this.playdate = playdate;
	}
	public boolean isPuttDisabled() {
		return puttDisabled;
	}
	public void setPuttDisabled(boolean puttDisabled) {
		this.puttDisabled = puttDisabled;
	}
	
	public String getTotalPuttText() {
		return puttDisabled ? "" : total_putt +"";
	}
	public String getGora_score_id() {
		return gora_score_id;
	}
	public void setGora_score_id(String gora_score_id) {
		this.gora_score_id = gora_score_id;
	}
	public String getLiveId() {
		return liveId;
	}
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

    public void setAgencyRequestId(String id) {
        this.agencyRequestId = id;
    }

    public String getAgencyRequestId() {
        return agencyRequestId;
    }

	public static HistoryObj clone(RoundScoreCard other){
		HistoryObj obj = new HistoryObj();
		if(other != null){
			obj.setCourse_id(other.getCourse_id());
			obj.setClub_name(other.getClubName());
			obj.setCourse_name(other.getCourseName());
			try{
				obj.setTotal_shot(Integer.parseInt(other.getScore()));
			}catch (Exception e){
				obj.setTotal_putt(0);
			}
			try{
				obj.setTotal_putt(Integer.parseInt(other.getScorePutt()));
			}catch (Exception e){
				obj.setTotal_putt(0);
			}
			obj.setPlaydate(other.getPlayDate());
			obj.setPuttDisabled(other.isPuttDisable());
			obj.setGora_score_id(other.getGoraId());
			obj.setLiveId(other.getLiveId());
			obj.setAgencyRequestId(other.getAgencyRequestId());
			obj.setWeather(other.getWeather());
		}
		return obj;
	}

	public ArrayList<HashMap<String, String>> getHashResult() {
		return hashResult;
	}

	public void setHashResult(ArrayList<HashMap<String, String>> hashResult) {
		this.hashResult = hashResult;
	}

	public String getMessageGift() {
		return messageGift;
	}

	public void setMessageGift(String messageGift) {
		this.messageGift = messageGift;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
