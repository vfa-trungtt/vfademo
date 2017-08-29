package com.asai24.golf.object;


import java.util.ArrayList;

public class ItemRakuten extends ObjectParent{

    private int golfCourseId;
    private String golfCourseName;
    private String golfCourseCaption;
    private int golfCourseRsvType;
    private int areaCode;
    private String prefecture;
    private int highwayCode;
    private String highway;
    private String ic;
    private String icDistance;
    private String golfCourseImageUrl;
    private String displayWeekdayMinPrice;
    private String displayWeekdayMinBasePrice;
    private String displayHolidayMinPrice;
    private String displayHolidayMinBasePrice;
    private int cancelFeeFlag;
    private String cancelFee;
    private int ratingNum;
    private float evaluation;
    private String reserveCalUrlPC;
    private  String reserveCalUrlMobile;
    private String ratingUrlPC;
    private String ratingUrlMobile;
    private ArrayList<PlanInfoRakuten> planInfoNext;

    public int getHighwayCode() {
        return this.highwayCode;
    }

    public void setHighwayCode(int highwayCode) {
        this.highwayCode = highwayCode;
    }

    public int getRatingNum() {
        return this.ratingNum;
    }

    public void setRatingNum(int ratingNum) {
        this.ratingNum = ratingNum;
    }

    public int getGolfCourseId() {
        return this.golfCourseId;
    }

    public void setGolfCourseId(int golfCourseId) {
        this.golfCourseId = golfCourseId;
    }

    public String getGolfCourseName() {
        return this.golfCourseName;
    }

    public void setGolfCourseName(String golfCourseName) {
        this.golfCourseName = golfCourseName;
    }

    public String getGolfCourseCaption() {
        return this.golfCourseCaption;
    }

    public void setGolfCourseCaption(String golfCourseCaption) {
        this.golfCourseCaption = golfCourseCaption;
    }

    public int getGolfCourseRsvType() {
        return this.golfCourseRsvType;
    }

    public void setGolfCourseRsvType(int golfCourseRsvType) {
        this.golfCourseRsvType = golfCourseRsvType;
    }

    public int getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public String getPrefecture() {
        return this.prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public String getHighway() {
        return this.highway;
    }

    public void setHighway(String highway) {
        this.highway = highway;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getIcDistance() {
        return this.icDistance;
    }

    public void setIcDistance(String icDistance) {
        this.icDistance = icDistance;
    }

    public String getGolfCourseImageUrl() {
        return this.golfCourseImageUrl;
    }

    public void setGolfCourseImageUrl(String golfCourseImageUrl) {
        this.golfCourseImageUrl = golfCourseImageUrl;
    }

    public String getDisplayWeekdayMinPrice() {
        return this.displayWeekdayMinPrice;
    }

    public void setDisplayWeekdayMinPrice(String displayWeekdayMinPrice) {
        this.displayWeekdayMinPrice = displayWeekdayMinPrice;
    }

    public String getDisplayWeekdayMinBasePrice() {
        return this.displayWeekdayMinBasePrice;
    }

    public void setDisplayWeekdayMinBasePrice(String displayWeekdayMinBasePrice) {
        this.displayWeekdayMinBasePrice = displayWeekdayMinBasePrice;
    }

    public String getDisplayHolidayMinPrice() {
        return this.displayHolidayMinPrice;
    }

    public void setDisplayHolidayMinPrice(String displayHolidayMinPrice) {
        this.displayHolidayMinPrice = displayHolidayMinPrice;
    }

    public String getDisplayHolidayMinBasePrice() {
        return this.displayHolidayMinBasePrice;
    }

    public void setDisplayHolidayMinBasePrice(String displayHolidayMinBasePrice) {
        this.displayHolidayMinBasePrice = displayHolidayMinBasePrice;
    }

    public int getCancelFeeFlag() {
        return this.cancelFeeFlag;
    }

    public void setCancelFeeFlag(int cancelFeeFlag) {
        this.cancelFeeFlag = cancelFeeFlag;
    }

    public String getCancelFee() {
        return this.cancelFee;
    }

    public void setCancelFee(String cancelFee) {
        this.cancelFee = cancelFee;
    }

    public float getEvaluation() {
        return this.evaluation;
    }

    public void setEvaluation(float evaluation) {
        this.evaluation = evaluation;
    }

    public String getReserveCalUrlPC() {
        return this.reserveCalUrlPC;
    }

    public void setReserveCalUrlPC(String reserveCalUrlPC) {
        this.reserveCalUrlPC = reserveCalUrlPC;
    }

    public String getReserveCalUrlMobile() {
        return this.reserveCalUrlMobile;
    }

    public void setReserveCalUrlMobile(String reserveCalUrlMobile) {
        this.reserveCalUrlMobile = reserveCalUrlMobile;
    }

    public String getRatingUrlPC() {
        return this.ratingUrlPC;
    }

    public void setRatingUrlPC(String ratingUrlPC) {
        this.ratingUrlPC = ratingUrlPC;
    }

    public String getRatingUrlMobile() {
        return this.ratingUrlMobile;
    }

    public void setRatingUrlMobile(String ratingUrlMobile) {
        this.ratingUrlMobile = ratingUrlMobile;
    }

    public ArrayList<PlanInfoRakuten> getPlanInfoNext() {
        return this.planInfoNext;
    }

    public void setPlanInfoNext(ArrayList<PlanInfoRakuten> planInfoNext) {
        this.planInfoNext = planInfoNext;
    }
}
