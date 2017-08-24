/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.asai24.golf.view;

import android.content.Context;
import android.graphics.drawable.Drawable;


import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.inputscore.ActivitySearchCourse;
import com.asai24.golf.utils.YgoLog;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

public class SimpleItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private ArrayList<ClubObj> mClubs = new ArrayList<ClubObj>();
	private Context context;

	public SimpleItemizedOverlay(Context curContext, Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		this.context = curContext;
	}

	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	public void addClubObj(ClubObj club){
		mClubs.add(club);
	}
	public void clearClubs(){
		mClubs.clear();
	}
	public void clearOverLay(){
		m_overlays.clear();
	}
	public int getInitialSize(){
		return mClubs.size();
	}
	public ArrayList<ClubObj> getClubs(){
		return mClubs;
	}
	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
        try {
            ClubObj clubObj = mClubs.get(index);
            ((ActivitySearchCourse) context).performOverlayClick(clubObj);
        } catch (Exception e) {
            YgoLog.e("CanNC", "onBalloonTap:false at " + index);
        }
		return true;
	}
	
}
