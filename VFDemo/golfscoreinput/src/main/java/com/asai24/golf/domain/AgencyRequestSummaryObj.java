package com.asai24.golf.domain;

public class AgencyRequestSummaryObj {

	private int canceledNumber;
	private int requestedNumber;
    private int max;
    private boolean campaignUserExperience;

    public void setCanceledNumber(int canceledNumber) {
        this.canceledNumber = canceledNumber;
    }

    public int getCanceledNumber() {
        return canceledNumber;
    }

    public void setRequestedNumber(int requestedNumber) {
        this.requestedNumber = requestedNumber;
    }

    public int getRequestedNumber() {
        return requestedNumber;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public boolean getCampaignUserExperience() {return campaignUserExperience;}
    public void setCampaignUserExperience(boolean campaignUserExperience) {this.campaignUserExperience = campaignUserExperience;}
}
