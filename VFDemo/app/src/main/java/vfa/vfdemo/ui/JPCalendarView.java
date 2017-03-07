package vfa.vfdemo.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.imanoweb.calendarview.CustomCalendarView;

import java.util.List;

public class JPCalendarView extends CustomCalendarView{

	public JPCalendarView(Context mContext, AttributeSet attrs) {
		super(mContext, attrs);
	}
	
	public JPCalendarView(Context mContext, int currentMonth, List<String> holidays) {
		super(mContext, true,holidays,currentMonth);
		setWeekendTextColor(Color.parseColor("#ff726b"), Color.parseColor("#11bddc"));
	}

	
}
