package com.asai24.golf.domain;

import com.asai24.golf.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by CanNC on 7/3/14.
 */
public class AnalysisDetailV2 {

    public static final float NO_VALUE_ANALYSIS = -1;

    // average_total_score
    @SerializedName("cnt_score_cards")
    private int numberOfRounds;
    private int totalHole;
    private float averageTotalScore = NO_VALUE_ANALYSIS;
    // gir_analysis
    private int gir = (int) NO_VALUE_ANALYSIS;
    private float percentRGir = NO_VALUE_ANALYSIS;
    private int gir1 = (int) NO_VALUE_ANALYSIS;
    private float percentRGir1 = NO_VALUE_ANALYSIS;
    // putt_analysis
    private float averagePutts = NO_VALUE_ANALYSIS;
    private float girAveragePutts = NO_VALUE_ANALYSIS;
    // score_analysis
    private float percentEagle = NO_VALUE_ANALYSIS;
    private float percentBirdie = NO_VALUE_ANALYSIS;
    private float percentPar = NO_VALUE_ANALYSIS;
    private float percentBogey = NO_VALUE_ANALYSIS;
    private float percentDoubleBogey = NO_VALUE_ANALYSIS;
    private float percentTripleBogey = NO_VALUE_ANALYSIS;
    // stroke_analysis
    private float averageStrokePar3 = NO_VALUE_ANALYSIS;
    private float averageStrokePar4 = NO_VALUE_ANALYSIS;
    private float averageStrokePar5 = NO_VALUE_ANALYSIS;
    private float percentRecovery = NO_VALUE_ANALYSIS;
    // driving_accuracy
    private float percentFairwayHit = NO_VALUE_ANALYSIS;
    private float percentMissedRight = NO_VALUE_ANALYSIS;
    private float percentMissedLeft = NO_VALUE_ANALYSIS;
    // sand_saves
    private float percentSaved = NO_VALUE_ANALYSIS;
    private Constant.ErrorServer errorServer;

    public Constant.ErrorServer getErrorServer() {
        return errorServer;
    }

    public void setErrorServer(Constant.ErrorServer errorServer) {
        this.errorServer = errorServer;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setTotalHole(int totalHole) {
        this.totalHole = totalHole;
    }

    public int getTotalHole() {
        return totalHole;
    }

    public void setAverageTotalScore(float averageTotalScore) {
        this.averageTotalScore = averageTotalScore;
    }

    public float getAverageTotalScore() {
        return averageTotalScore;
    }

    public void setGir(int gir) {
        this.gir = gir;
    }

    public int getGir() {
        return gir;
    }

    public void setPercentRGir(float percentRGir) {
        this.percentRGir = percentRGir;
    }

    public float getPercentRGir() {
        return percentRGir;
    }

    public void setGir1(int gir1) {
        this.gir1 = gir1;
    }

    public int getGir1() {
        return gir1;
    }

    public void setPercentRGir1(float percentRGir1) {
        this.percentRGir1 = percentRGir1;
    }

    public float getPercentRGir1() {
        return percentRGir1;
    }

    public void setAveragePutts(float averagePutts) {
        this.averagePutts = averagePutts;
    }

    public float getAveragePutts() {
        return averagePutts;
    }

    public void setGirAveragePutts(float girAveragePutts) {
        this.girAveragePutts = girAveragePutts;
    }

    public float getGirAveragePutts() {
        return girAveragePutts;
    }

    public void setPercentEagle(float percentEagle) {
        this.percentEagle = percentEagle;
    }

    public float getPercentEagle() {
        return percentEagle;
    }

    public void setPercentBirdie(float percentBirdie) {
        this.percentBirdie = percentBirdie;
    }

    public float getPercentBirdie() {
        return percentBirdie;
    }

    public void setPercentPar(float percentPar) {
        this.percentPar = percentPar;
    }

    public float getPercentPar() {
        return percentPar;
    }

    public void setPercentBogey(float percentBogey) {
        this.percentBogey = percentBogey;
    }

    public float getPercentBogey() {
        return percentBogey;
    }

    public void setPercentDoubleBogey(float percentDoubleBogey) {
        this.percentDoubleBogey = percentDoubleBogey;
    }

    public float getPercentDoubleBogey() {
        return percentDoubleBogey;
    }

    public void setPercentTripleBogey(float percentTripleBogey) {
        this.percentTripleBogey = percentTripleBogey;
    }

    public float getPercentTripleBogey() {
        return percentTripleBogey;
    }

    public void setAverageStrokePar3(float averageStrokePar3) {
        this.averageStrokePar3 = averageStrokePar3;
    }

    public float getAverageStrokePar3() {
        return averageStrokePar3;
    }

    public void setAverageStrokePar4(float averageStrokePar4) {
        this.averageStrokePar4 = averageStrokePar4;
    }

    public float getAverageStrokePar4() {
        return averageStrokePar4;
    }

    public void setAverageStrokePar5(float averageStrokePar5) {
        this.averageStrokePar5 = averageStrokePar5;
    }

    public float getAverageStrokePar5() {
        return averageStrokePar5;
    }

    public void setPercentRecovery(float percentRecovery) {
        this.percentRecovery = percentRecovery;
    }

    public float getPercentRecovery() {
        return percentRecovery;
    }

    public void setPercentFairwayHit(float percentFairwayHit) {
        this.percentFairwayHit = percentFairwayHit;
    }

    public float getPercentFairwayHit() {
        return percentFairwayHit;
    }

    public void setPercentMissedRight(float percentMissedRight) {
        this.percentMissedRight = percentMissedRight;
    }

    public float getPercentMissedRight() {
        return percentMissedRight;
    }

    public void setPercentMissedLeft(float percentMissedLeft) {
        this.percentMissedLeft = percentMissedLeft;
    }

    public float getPercentMissedLeft() {
        return percentMissedLeft;
    }

    public void setPercentSaved(float percentSaved) {
        this.percentSaved = percentSaved;
    }

    public float getPercentSaved() {
        return percentSaved;
    }
}