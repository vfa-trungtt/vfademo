package com.asai24.golf.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CanNC on 3/4/15.
 */
public class PlayedCourse implements Parcelable {

    private String id;
    private String extType;
    private String name;
    private String clubName;
    private int scoreCardsCount;

    public PlayedCourse() {
    }

    public PlayedCourse(Parcel source) {
        id = source.readString();
        extType = source.readString();
        name = source.readString();
        clubName = source.readString();
        scoreCardsCount = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(extType);
        dest.writeString(name);
        dest.writeString(clubName);
        dest.writeInt(scoreCardsCount);
    }

    public static final Creator<PlayedCourse> CREATOR = new Creator<PlayedCourse>() {
        @Override
        public PlayedCourse createFromParcel(Parcel source) {
            return new PlayedCourse(source);
        }

        @Override
        public PlayedCourse[] newArray(int size) {
            return new PlayedCourse[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setExtType(String extType) {
        this.extType = extType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setScoreCardsCount(int scoreCardsCount) {
        this.scoreCardsCount = scoreCardsCount;
    }

    public String getId() {
        return id;
    }

    public String getExtType() {
        return extType;
    }

    public String getName() {
        return name;
    }

    public String getClubName() {
        return clubName;
    }

    public int getScoreCardsCount() {
        return scoreCardsCount;
    }
}

//  "id": "5072d7a1ba6b0a7cfd6590a3",
//  "ext_type": "yourgolf2",
//  "name": "Hornet",
//  "club_name": "ABCゴルフ倶楽部",
//  "cnt_score_cards": 5
