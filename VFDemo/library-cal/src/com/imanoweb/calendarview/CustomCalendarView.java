package com.imanoweb.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CustomCalendarView extends LinearLayout {
    private Context mContext;

    private View view;
    private ImageView previousMonthButton;
    private ImageView nextMonthButton;

    private CalendarListener calendarListener;
    private Calendar currentCalendar;
    private Locale locale;

    private Date lastSelectedDay;
    private Typeface customTypeface;

    private int firstDayOfWeek = Calendar.SUNDAY;

    private List<DayDecorator> decorators = null;

    private static final String DAY_OF_WEEK = "dayOfWeek";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";

    private int disabledDayBackgroundColor;
    private int disabledDayTextColor;
    private int calendarBackgroundColor;
    private int selectedDayBackground;
    private int weekLayoutBackgroundColor;
    private int calendarTitleBackgroundColor = 0xf0ece1;
    private int selectedDayTextColor;
    private int calendarTitleTextColor;
    private int dayOfWeekTextColor;
    private int dayOfMonthTextColor;
    private int currentDayOfMonth;

    private int currentMonthIndex = 0;
    private boolean isOverflowDateVisible = true;

    
    private int sundayTextColor;
    private int saturdayTextColor;
    
    private boolean _isJPCalendar = true;    
    private DayView currentDayView;
    
    HashMap<String, DayView> dayCells = new HashMap<String, DayView>();
    
    DayView[] arrDayCells = new DayView[42];
    public List<String> holidays;
    
    @SuppressLint("NewApi") public CustomCalendarView(Context mContext,boolean isJP,List<String> holidays,int currentMonth) {
    	super(mContext);
    	_isJPCalendar = isJP;
    	_currentMonth = currentMonth;
    	
    	this.mContext = mContext;
    	if (isInEditMode())
            return;
    	this.holidays= holidays; 
    	getAttributes(null);
        initializeCalendar();     
    }
    
    public CustomCalendarView(Context mContext) {
        this(mContext, null);
    }

    @SuppressLint("NewApi") public CustomCalendarView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (isInEditMode())
                return;
        }

        getAttributes(attrs);
        initializeCalendar();
    }

    private void getAttributes(AttributeSet attrs) {
        final TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CustomCalendarView, 0, 0);
        calendarBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_calendarBackgroundColor, getResources().getColor(R.color.white));
//        calendarTitleBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_titleLayoutBackgroundColor, getResources().getColor(R.color.white));
        calendarTitleTextColor = typedArray.getColor(R.styleable.CustomCalendarView_calendarTitleTextColor, getResources().getColor(R.color.black));
        weekLayoutBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_weekLayoutBackgroundColor, getResources().getColor(R.color.white));
        dayOfWeekTextColor = typedArray.getColor(R.styleable.CustomCalendarView_dayOfWeekTextColor, getResources().getColor(R.color.black));
        dayOfMonthTextColor = typedArray.getColor(R.styleable.CustomCalendarView_dayOfMonthTextColor, getResources().getColor(R.color.black));
        disabledDayBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_disabledDayBackgroundColor, getResources().getColor(R.color.day_disabled_background_color));
        disabledDayTextColor = typedArray.getColor(R.styleable.CustomCalendarView_disabledDayTextColor, getResources().getColor(R.color.day_disabled_text_color));
        selectedDayBackground = typedArray.getColor(R.styleable.CustomCalendarView_selectedDayBackgroundColor, getResources().getColor(R.color.selected_day_background));
        selectedDayTextColor = typedArray.getColor(R.styleable.CustomCalendarView_selectedDayTextColor, getResources().getColor(R.color.white));
        currentDayOfMonth = typedArray.getColor(R.styleable.CustomCalendarView_currentDayOfMonthColor, getResources().getColor(R.color.current_day_of_month));
        typedArray.recycle();
        
        dayOfWeekTextColor = 0xff705b4c;
    }

    private int _currentMonth =  -1;
    
    @SuppressLint("NewApi") private void initializeCalendar() {
    	
    	
        final LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
//        view = inflate.inflate(R.layout.custom_calendar_layout, this, true);
        view = inflate.inflate(R.layout.jp_calendar_layout, this, true);
        
        previousMonthButton = (ImageView) view.findViewById(R.id.leftButton);
        nextMonthButton = (ImageView) view.findViewById(R.id.rightButton);

        previousMonthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonthIndex--;
                currentCalendar = Calendar.getInstance(Locale.getDefault());
                currentCalendar.add(Calendar.MONTH, currentMonthIndex);

                refreshCalendar(currentCalendar);
                if (calendarListener != null) {
                    calendarListener.onMonthChanged(currentCalendar.getTime());
                }
            }
        });

        nextMonthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMonthIndex++;
                currentCalendar = Calendar.getInstance(Locale.getDefault());
                currentCalendar.add(Calendar.MONTH, currentMonthIndex);
                refreshCalendar(currentCalendar);

                if (calendarListener != null) {
                    calendarListener.onMonthChanged(currentCalendar.getTime());
                }else{
                	Log.i("TrungTT","NULLLLLLLLLLL");
                }
            }
        });

        for (int i = 1; i < 43; i++) {
    		DayView dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
			 if (dayView != null){
				arrDayCells[i-1] = dayView; 
			 }			  
    	}
        // Initialize calendar for current month
        Locale locale = mContext.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        
        sundayTextColor 	= Color.parseColor("#ff726b");
    	saturdayTextColor	= Color.parseColor("#11bddc");
    	
        setFirstDayOfWeek(Calendar.SUNDAY);
        if(_currentMonth >= 0){
        	refreshCalendar(_currentMonth);
        }
        
        
//        if(currentMonthIndex)
//        refreshCalendar(currentCalendar);
    }


    /**
     * Display calendar title with next previous month button
     */
    private void initializeTitleLayout() {
    	calendarTitleTextColor = 0xff705b4c;
//        View titleLayout = view.findViewById(R.id.titleLayout);
//        titleLayout.setBackgroundColor(calendarTitleBackgroundColor);

        String dateText = new DateFormatSymbols(locale).getShortMonths()[currentCalendar.get(Calendar.MONTH)].toString();
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());

        TextView dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        dateTitle.setTextColor(calendarTitleTextColor);
//        dateTitle.setTextSize(getResources().getDimension(R.dimen.textsize_calendar_title));
        dateTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textsize_calendar_title_header));
        
        if(_isJPCalendar){
//        	String month = String.format("%02d", currentCalendar.get(Calendar.MONTH) +1);
        	String month = "" + (currentCalendar.get(Calendar.MONTH) +1);
        	dateTitle.setText(currentCalendar.get(Calendar.YEAR) + "年"+month + "月");
        }else{
        	 dateTitle.setText(dateText + " " + currentCalendar.get(Calendar.YEAR));
        }
        
        if (null != getCustomTypeface()) {
            dateTitle.setTypeface(getCustomTypeface(), Typeface.BOLD);
        }

    }

    /**
     * Initialize the calendar week layout, considers start day
     */
    @SuppressLint("DefaultLocale")
    private void initializeWeekLayout() {
    	String[] jpWeek = {"日","月","火","水","木","金","土"};
    	
        TextView dayOfWeek;
        String dayOfTheWeekString;

        //Setting background color white
        View titleLayout = view.findViewById(R.id.weekLayout);
        titleLayout.setBackgroundColor(weekLayoutBackgroundColor);

        final String[] weekDaysArray = new DateFormatSymbols(locale).getShortWeekdays();
        if(_isJPCalendar){
        	for (int i = 1; i <= jpWeek.length; i++) {
        		dayOfTheWeekString = jpWeek[i-1];
        		
        		dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i, currentCalendar));
        		dayOfWeek.setText(dayOfTheWeekString);
        		/*if((i-1) == 0){
        			//sunday
        			dayOfWeek.setTextColor(sundayTextColor);
        		}
        		dayOfWeek.setTextColor(dayOfWeekTextColor);*/
        		dayOfWeek.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textsize_day));
        		
        	}
    	}else{
    		
    		for (int i = 1; i < weekDaysArray.length; i++) {
            	
                dayOfTheWeekString = weekDaysArray[i];
                if(dayOfTheWeekString.length() > 3){
                    dayOfTheWeekString = dayOfTheWeekString.substring(0, 3).toUpperCase();
                }
                
                dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i, currentCalendar));
                dayOfWeek.setText(dayOfTheWeekString);
                dayOfWeek.setTextColor(dayOfWeekTextColor);

                if (null != getCustomTypeface()) {
                    dayOfWeek.setTypeface(getCustomTypeface());
                }
            }
    	}
        
    }

    private void setDaysInCalendar() {
    	dayCells.clear();
    	
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final Calendar startCalendar = (Calendar) calendar.clone();
        //Add required number of days
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//		   if(df.format(dayView.getDate()).equalsIgnoreCase(df.format(date))){
        DayView dayView;
//        ViewGroup dayOfMonthContainer;
        for (int i = 1; i < 43; i++) {
//            dayOfMonthContainer = (ViewGroup) view.findViewWithTag(DAY_OF_MONTH_CONTAINER + i);
//            dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            dayView = arrDayCells[i-1];
            
//            dayView.setDa
            if (dayView == null)
                continue;

            
            //Apply the default styles
//            dayOfMonthContainer.setOnClickListener(null);
            dayView.setOnClickListener(null);
            dayView.bind(startCalendar.getTime(), getDecorators());
            dayView.setVisibility(View.VISIBLE);
            dayView.setTextColor(Color.parseColor("#705b4c"));
            
            dayCells.put(df.format(startCalendar.getTime()), dayView);
            
            if (null != getCustomTypeface()) {
                dayView.setTypeface(getCustomTypeface());
            }

            dayView.setOnClickListener(onDayOfMonthClickListener);
            if (isSameMonth(calendar, startCalendar)) {
//            	dayView.setOnClickListener(onDayOfMonthClickListener);
//                dayView.setBackgroundColor(calendarBackgroundColor);
                dayView.setBackgroundColor(Color.TRANSPARENT);
                dayView.setTextColor(dayOfWeekTextColor);
            } else {
                dayView.setBackgroundColor(disabledDayBackgroundColor);
//                dayView.setTextColor(disabledDayTextColor);
//                if (!isOverflowDateVisible())
//                    dayView.setVisibility(View.GONE);
//                else if (i >= 36 && ((float) monthEndIndex / 7.0f) >= 1) {
//                    dayView.setVisibility(View.GONE);
//                }
            }
            dayView.decorate();

            //Set the current day color
            markDayAsCurrentDay(startCalendar);
            
          
            
            if(startCalendar.get(Calendar.DAY_OF_WEEK) == 7){
            	//saturday
            	dayView.setTextColor(saturdayTextColor);
            }
            if(startCalendar.get(Calendar.DAY_OF_WEEK) == 1){
            	//sunday
            	dayView.setTextColor(sundayTextColor);
            }     
            
            startCalendar.add(Calendar.DATE, 1);

            if(holidays != null){
            	SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
 			   	String dateCalendar = df1.format(dayView.getDate());
 			   if(holidays.contains(dateCalendar)){ 				
 				   dayView.setTextColor(Color.parseColor("#ff726b"));
 			   }
            	
            		
            }
            dayOfMonthIndex++;
        }

        // If the last week row has no visible days, hide it or show it in case
//        ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
//        dayView = (DayView) view.findViewWithTag("dayOfMonthText36");
//        if (dayView.getVisibility() != VISIBLE) {
//            weekRow.setVisibility(GONE);
//        } else {
//            weekRow.setVisibility(VISIBLE);
//        }
    }
    
    


    public boolean isSameMonth(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return false;
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    /**
     * <p>Checks if a calendar is today.</p>
     *
     * @param calendar the calendar, not altered, not null.
     * @return true if the calendar is today.
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            throw new IllegalArgumentException("The dates must not be null");
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }


    private void clearDayOfTheMonthStyle(Date currentDate) {
        if (currentDate != null) {
            final Calendar calendar = getTodaysCalendar();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentDate);

            final DayView dayView = getDayOfMonthText(calendar);
            dayView.setBackgroundColor(calendarBackgroundColor);
            dayView.setTextColor(dayOfWeekTextColor);
        }
    }

    private DayView getDayOfMonthText(Calendar currentCalendar) {
        return (DayView) getView(DAY_OF_MONTH_TEXT, currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int index = currentDay + monthOffset;
        return index;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {
            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {

            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        View childView = view.findViewWithTag(key + index);
        return childView;
    }

    private Calendar getTodaysCalendar() {
        Calendar currentCalendar = Calendar.getInstance(mContext.getResources().getConfiguration().locale);
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        return currentCalendar;
    }

    @SuppressLint("DefaultLocale")
    public void refreshCalendar(Calendar currentCalendar) {
        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        locale = mContext.getResources().getConfiguration().locale;

        // Set date title
        initializeTitleLayout();

        // Set weeks days titles
        initializeWeekLayout();

        // Initialize and set days in calendar
        setDaysInCalendar();
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public void markDayAsCurrentDay(Calendar calendar) {
        if (calendar != null && isToday(calendar)) {
            DayView dayOfMonth = getDayOfMonthText(calendar);
            dayOfMonth.setTextColor(currentDayOfMonth);
            dayOfMonth.setBackgroundColor(getResources().getColor(R.color.bg_current_day_of_month));
        }
    }

    public void markDayAsSelectedDay(Date currentDate) {
        final Calendar currentCalendar = getTodaysCalendar();
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);

        // Clear previous marks
        clearDayOfTheMonthStyle(lastSelectedDay);

        // Store current values as last values
        storeLastValues(currentDate);

        // Mark current day as selected
        DayView view = getDayOfMonthText(currentCalendar);
        view.setBackgroundColor(selectedDayBackground);
        view.setTextColor(selectedDayTextColor);
    }

    private void storeLastValues(Date currentDate) {
        lastSelectedDay = currentDate;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Extract day selected
        	if(currentDayView == null){
        		currentDayView = (DayView)view;
        	}else{
        		currentDayView.setSelected(false);
        		currentDayView = (DayView)view;
        		
        	}
        	
        	currentDayView.setSelected(true);
			if (calendarListener != null){
				Log.d("TrungTT", "select date:");
				calendarListener.onDateSelected(currentDayView.getDate());
			}else{
				Log.d("TrungTT", "NULLLLLLLLLLLLL");
			}

//        	
//            ViewGroup dayOfMonthContainer = (ViewGroup) view;        	
//            String tagId = (String) view.getTag();
//            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length(), tagId.length());
//            final TextView dayOfMonthText = (TextView) view.findViewWithTag(DAY_OF_MONTH_TEXT + tagId);
//
//            // Fire event
//            final Calendar calendar = Calendar.getInstance();
//            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
//            calendar.setTime(currentCalendar.getTime());
//            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfMonthText.getText().toString()));
//            markDayAsSelectedDay(calendar.getTime());
//
//            //Set the current day color
//            markDayAsCurrentDay(currentCalendar);
//
//            if (calendarListener != null)
//                calendarListener.onDateSelected(calendar.getTime());
        }
    };

    public List<DayDecorator> getDecorators() {
        return decorators;
    }

    public void setDecorators(List<DayDecorator> decorators) {
        this.decorators = decorators;
    }

    public boolean isOverflowDateVisible() {
        return isOverflowDateVisible;
    }

    public void setShowOverflowDate(boolean isOverFlowEnabled) {
        isOverflowDateVisible = isOverFlowEnabled;
    }

    public void setCustomTypeface(Typeface customTypeface) {
        this.customTypeface = customTypeface;
    }

    public Typeface getCustomTypeface() {
        return customTypeface;
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }
    
    public void setJPCalendar(boolean value){
    	_isJPCalendar = value;
    	initializeWeekLayout();
    }
    
    public void setSundayTextColor(int color){
    	sundayTextColor = color;
    	TextView dayOfWeek;

        for (int i = 0; i < 7; i++) {
        	if(getWeekIndex(i+1, currentCalendar) == 1){
        		dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i+1, currentCalendar));
        		dayOfWeek.setTextColor(sundayTextColor);
        	}    		    		
        }
        
    }
    public void setWeekendTextColor(int colorSunday,int colorSaturday){
    	sundayTextColor 	= colorSunday;
    	saturdayTextColor	= colorSaturday;
    	
    	TextView dayOfWeek;

        /*for (int i = 0; i < 7; i++) {
        	if(getWeekIndex(i+1, currentCalendar) == 1){
        		dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i+1, currentCalendar));
        		dayOfWeek.setTextColor(sundayTextColor);
        	}   
        	if(getWeekIndex(i+1, currentCalendar) == 7){
        		dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i+1, currentCalendar));
        		dayOfWeek.setTextColor(saturdayTextColor);
        	}   
        }*/
        setDaysInCalendar();
    }
    
    public void cleanDayData(){    	
    	DayView dayView = null;
    	for (int i = 0; i < 42; i++) {
    		dayView = arrDayCells[i];
    		dayView.setCountNumber(0);
			dayView.cleanData();
//			dayView.postInvalidate();
    	}    	        	
//    	invalidate();
//    	System.gc();
    }
    
    public void refresh(){    	
    	DayView dayView = null;
    	for (int i = 0; i < 42; i++) {
    		dayView = arrDayCells[i];
//    		dayView.invalidate();
    		dayView.postInvalidate();
//			dayView.cleanData();
    	}    	        	
//    	invalidate();
//    	System.gc();
    }
    
    @SuppressLint("SimpleDateFormat")
    public void setHolidays(List<String> holidays){
    	if(holidays == null) return;
    	DayView dayView = null;
    	Log.e("TrungTT", "update holiday");
    	for (int i = 1; i < 43; i++) {
    		dayView = arrDayCells[i-1];
			  if (dayView == null)
			      continue;
			   SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			   String dateCalendar = df.format(dayView.getDate());
//			   for(String s:holidays){
//				   Log.e("TrungTT", "set holiday:"+dateCalendar +" vs " + s);
//				   if(s.trim().equalsIgnoreCase(dateCalendar)){
//					   dayView.setTextColor(Color.parseColor("#ff726b"));
//				   }
//				   break;
//			   }
			   if(holidays.contains(dateCalendar)){
				   Log.e("TrungTT", "set holiday:" + dateCalendar);
				   dayView.setTextColor(Color.parseColor("#ff726b"));
			   }
    	}
    }
    
//    @SuppressLint("SimpleDateFormat")
//    public void setBitmapForDate(Bitmap bmp,Date date){    	
//    	Log.i("TrungTT", "select date:"+date.toString());            
//    	DayView dayView = null;
//    	for (int i = 1; i < 43; i++) {
//    		dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
//			  if (dayView == null)
//			      continue;
//			   SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//			   if(df.format(dayView.getDate()).equalsIgnoreCase(df.format(date))){
//				   break;
//			   }else{
//				   dayView = null;
//			   }
//    	}
//    
//    	if(dayView != null){
//    		Log.i("TrungTT", "set bitmap of date:"+dayView.getDate().toString());
//    		dayView.setBitmap(bmp);
//    	}
//    }
    
    @SuppressLint("SimpleDateFormat") public void setBitmapForDate(Bitmap bmp,int color,Date date){    	
//    	Log.i("TrungTT", "select date:"+date.toString());            
    	DayView dayView = null;
    	for (int i = 1; i < 43; i++) {
    		dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
			  if (dayView == null)
			      continue;
			   SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			   if(df.format(dayView.getDate()).equalsIgnoreCase(df.format(date))){
				   break;
			   }else{
				   dayView = null;
			   }
    	}
    
    	if(dayView != null){
//    		Log.i("TrungTT", "set bitmap of date:"+dayView.getDate().toString()+" ,color:"+color);
    		dayView.setBitmap(bmp,color);
    	}
    }
    
    public void setImageForDate(Bitmap bmp,Date date){    	
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String daystring = df.format(date);
    	DayView dayView = dayCells.get(daystring);
    	
    	if(dayView != null){
    		dayView.setBitmap(bmp);
    	}
    }
    
    public void setImageForDate(Bitmap bmp,Date date,int animId){    	
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String daystring = df.format(date);
    	DayView dayView = dayCells.get(daystring);
    	
    	if(dayView != null){
    		dayView.setBitmap(bmp);
//    		dayView.startAnimation(AnimationUtils.loadAnimation(getContext(), animId));
    	}
    }
    
    public void setColorForDate(int color,Date date){    	
//    	Log.i("TrungTT", "select date:"+date.toString());    
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String daystring = df.format(date);
    	DayView dayView = dayCells.get(daystring);
    	
//    	DayView dayView = null;
//    	for (int i = 1; i < 42; i++) {
//    		dayView = arrDayCells[i];
////    		dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
//			  if (dayView == null)
//			      continue;
//			   SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//			   if(df.format(dayView.getDate()).equalsIgnoreCase(df.format(date))){
//				   break;
//			   }else{
//				   dayView = null;
//			   }
//    	}
    
    	if(dayView != null){
//    		Log.i("TrungTT", "set color of date:"+dayView.getDate().toString()+" ,color:"+color);
    		dayView.setColor(color);
    	}
//    	invalidate();
    }
    
    public void nextMonth(){
    	nextMonthButton.performClick();
    }
    
    public void prevMonth(){
    	previousMonthButton.performClick();
    }
    
    private void loadCalendar(int year,int month){
    	currentMonthIndex = month;
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.set(Calendar.MONTH, currentMonthIndex);
        refreshCalendar(currentCalendar);

        if (calendarListener != null) {
            calendarListener.onMonthChanged(currentCalendar.getTime());
        }else{
        	Log.i("TrungTT","NULLLLLLLLLLL");
        }
    }
    
    public void refreshCalendar(int month){
//    	if(month == 0) return;
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, month);
        
//        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        locale = mContext.getResources().getConfiguration().locale;

        // Set date title
        initializeTitleLayout();

        // Set weeks days titles
        initializeWeekLayout();

        // Initialize and set days in calendar
        setDaysInCalendar();
        
//        refreshCalendar(currentCalendar);

    }
    
    public int disableDateIsNextMonth(Date date){
//    	Log.i("TrungTT", "current month"+currentCalendar.get(Calendar.MONTH));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        Log.i("TrungTT", "select month"+calendar.get(Calendar.MONTH));
        if(calendar.get(Calendar.MONTH) > currentCalendar.get(Calendar.MONTH)){
        	return 1;
        }else if (calendar.get(Calendar.MONTH) < currentCalendar.get(Calendar.MONTH)){
        	return -1;
        }else{
        	return 0;
        }
    }
    
    @SuppressLint("SimpleDateFormat") public void setDateSelected(Date selectDate){
//    	Log.i("TrungTT", "SET SELECTED DAY:"+selectDate.toString());
    	DayView dayView = null;
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	
    	for (int i = 1; i < 43; i++) {
    		dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
			  if (dayView == null)
			      continue;
			   
			   if(df.format(dayView.getDate()).equalsIgnoreCase(df.format(selectDate))){
				   Log.i("TrungTT", "SET SELECTED DAY FOUND:"+selectDate.toString());
				   if(currentDayView == null){
		        		currentDayView = dayView;
		        	}else{
		        		currentDayView.setSelected(false);
		        		currentDayView = dayView;
		        		
		        	}		        	
		        	currentDayView.setSelected(true);
			   }else{
				   dayView.setSelected(false);
			   }
			   dayView.invalidate();
    	}        	
    }
    
    @SuppressLint("SimpleDateFormat") public boolean isDayOfCalendar(Date date){
    	boolean reval = false;    	
    	DayView dayView = null;
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    	for (int i = 1; i < 43; i++) {
    		dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
			  if (dayView == null)
			      continue;			   
			   if(df.format(dayView.getDate()).equalsIgnoreCase(df.format(date))){
				   reval = true;
				   break;
			   }
    	}
    	return reval;
    }
    
    public Date getStartDate(){
    	DayView dayView = arrDayCells[0];
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dayView.getDate());
    	cal.set(Calendar.HOUR, 1);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	
    	return cal.getTime();
    }
    public Date getEndDate(){
    	DayView dayView = arrDayCells[arrDayCells.length - 1];
    	return dayView.getDate();
    }
    
    
}
