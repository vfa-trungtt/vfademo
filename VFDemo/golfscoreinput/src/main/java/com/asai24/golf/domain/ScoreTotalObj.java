package com.asai24.golf.domain;

/**
 * Created by huynhtd on 10/17/13.
 */
public class ScoreTotalObj {
    private String avg;
    private String total;
    private String bestScore;
    private String lastScore;
    private String msgError;
    private int codeError;
    private String best_score_round_id;
    private String last_score_round_id;

    public String getBest_score_round_id() {
        return best_score_round_id;
    }

    public void setBest_score_round_id(String best_score_round_id) {
        this.best_score_round_id = best_score_round_id;
    }

    public String getLast_score_round_id() {
        return last_score_round_id;
    }

    public void setLast_score_round_id(String last_score_round_id) {
        this.last_score_round_id = last_score_round_id;
    }

    public int getCodeError() {
        return codeError;
    }

    public void setCodeError(int codeError) {
        this.codeError = codeError;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBestScore() {
        return bestScore;
    }

    public void setBestScore(String bestScore) {
        this.bestScore = bestScore;
    }

    public String getLastScore() {
        return lastScore;
    }

    public void setLastScore(String lastScore) {
        this.lastScore = lastScore;
    }


}
