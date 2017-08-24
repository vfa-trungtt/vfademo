package com.asai24.golf.domain;

import java.io.Serializable;

/**
 * Created by admin on 6/3/15.
 */
public class GolfDayClub implements Serializable {

    private String clubId;

    private String clubName;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

}
