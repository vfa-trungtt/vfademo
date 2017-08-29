package com.asai24.golf.domain;

import android.content.Context;


import com.asai24.golf.inputscore.R;

import java.util.HashMap;

public class ClubConverter {
	
	HashMap<String, String> mClubsLongKey;
	HashMap<String, String> mClubsShortKey;
	String[] mClubValues;
	String[] mClubNames;

	public ClubConverter(Context context) {
		mClubValues = context.getResources()
				.getStringArray(R.array.club_value);
		mClubNames = context.getResources().getStringArray(R.array.club_name);
		mClubsLongKey = new HashMap<String, String>();
		mClubsShortKey = new HashMap<String, String>();

		int cnt = mClubValues.length;
		for (int i = 0; i < cnt; i++) {
			mClubsLongKey.put(mClubNames[i], mClubValues[i]);
			mClubsShortKey.put(mClubValues[i], mClubNames[i]);
		}
	}

	public String getValue(String clubName) {
		if (clubName != null && !clubName.equals("") && mClubsLongKey.containsKey(clubName)) {
			return mClubsLongKey.get(clubName);
		} else {
			return "";
		}
	}
	
	public String getLongValue(String clubName) {
		if (clubName != null && !clubName.equals("") && mClubsShortKey.containsKey(clubName)) {
			return mClubsShortKey.get(clubName);
		} else {
			return "";
		}
	}
}
