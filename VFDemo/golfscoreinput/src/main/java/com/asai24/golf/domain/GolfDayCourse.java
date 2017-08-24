package com.asai24.golf.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CanNC on 3/4/15.
 */
public class GolfDayCourse implements Parcelable {

    private int golfDayClubId;
    private int golfDayCourseId;
    private int golfDayHole;

    public GolfDayCourse(int clubId, int courseId, int hole) {

        golfDayClubId = clubId;
        golfDayCourseId = courseId;
        golfDayHole = hole;
    }

    public GolfDayCourse(Parcel source) {
        golfDayClubId = source.readInt();
        golfDayCourseId = source.readInt();
        golfDayHole = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(golfDayClubId);
        dest.writeInt(golfDayCourseId);
        dest.writeInt(golfDayHole);
    }

    public static final Creator<GolfDayCourse> CREATOR = new Creator<GolfDayCourse>() {
        @Override
        public GolfDayCourse createFromParcel(Parcel source) {
            return new GolfDayCourse(source);
        }

        @Override
        public GolfDayCourse[] newArray(int size) {
            return new GolfDayCourse[size];
        }
    };

    public void setGolfDayClubId(int golfDayClubId) {
        this.golfDayClubId = golfDayClubId;
    }

    public int getGolfDayClubId() {
        return golfDayClubId;
    }

    public void setGolfDayCourseId(int golfDayCourseId) {
        this.golfDayCourseId = golfDayCourseId;
    }

    public int getGolfDayCourseId() {
        return golfDayCourseId;
    }

    public void setGolfDayHole(int golfDayHole) {
        this.golfDayHole = golfDayHole;
    }

    public int getGolfDayHole() {
        return golfDayHole;
    }
}