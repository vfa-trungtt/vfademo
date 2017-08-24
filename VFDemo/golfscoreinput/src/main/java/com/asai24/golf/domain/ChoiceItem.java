package com.asai24.golf.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


import com.asai24.golf.inputscore.R;

import java.util.Calendar;

/**
 * Created by huynhtd on 10/15/13.
 */
public class ChoiceItem implements Parcelable {
    private String label;
    private int year;
    private int lastMonth;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(int lastMonth) {
        this.lastMonth = lastMonth;
    }

    public static ChoiceItem createDefaultItem() {
        ChoiceItem item = new ChoiceItem();
        int year = Calendar.getInstance().get(Calendar.YEAR);// new DateTime().getYear();
        item.setYear(year);
        item.setLabel("" + year);
        return item;
    }

    public static ChoiceItem createDefaultItemForHistory(Context context) {
        ChoiceItem item = new ChoiceItem();
        item.setLabel(context.getString(R.string.all));
        item.setLastMonth(0);
        return item;
    }

    public boolean equals(ChoiceItem object) {
        if (object == null) {
            return false;
        }
        if (year == object.getYear() && lastMonth == object.getLastMonth()) {
            return true;
        } else {
            return false;
        }
    }

    public ChoiceItem() {
    }

    public ChoiceItem(Parcel source) {
        label = source.readString();
        year = source.readInt();
        lastMonth = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeInt(year);
        dest.writeInt(lastMonth);
    }

    public static final Creator<ChoiceItem> CREATOR = new Creator<ChoiceItem>() {
        @Override
        public ChoiceItem createFromParcel(Parcel source) {
            return new ChoiceItem(source);
        }

        @Override
        public ChoiceItem[] newArray(int size) {
            return new ChoiceItem[size];
        }
    };
}
