package com.asai24.golf.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	/**
	 * inTimeを、fromZoneのタイムゾーンからtoZoneのタイムゾーンに変換します
	 * 
	 * @param inTime
	 * @param fromZone
	 * @param toZone
	 * @return
	 */
	public static long convertTime(long inTime, TimeZone fromZone,
			TimeZone toZone) {
		int toOffset = toZone.getOffset(inTime);
		int fromOffset = fromZone.getOffset(inTime);
		int offset = fromOffset - toOffset;

		return (inTime + offset);
	}
    public static Date getCurrentDate(){
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }
    public static Date getCurrentJPDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Japan"));
        Date curDate = null;
        try {
            curDate = dateFormat.parse(dateFormat.format(new Date()));
            return curDate;
        }catch (Exception e){
//            if(BuildConfig.DEBUG){
//                e.printStackTrace();
//            }
        }
        return curDate;
    }
    public static String parseDateToString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = df.format(date.getTime());
        return formattedDate;
    }
    public static Date parseStringToDate(String date){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateObj = Calendar.getInstance().getTime();
        try {
            dateObj = df.parse(date);
            return dateObj;
        }catch (Exception e){
//            if(BuildConfig.DEBUG){
//                e.printStackTrace();
//            }
        }
       return null;
    }
    public static Date parseStringToDate(Date date,String format){
        String dateformat = parseDateToString(date);
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dateObj = getCurrentDate();
        try {
            dateObj = df.parse(dateformat);
            return dateObj;
        }catch (Exception e){

        }
        return null;
    }
    public static long pareStringToLog(String date){
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateObj = getCurrentDate();
        try {
            dateObj = df.parse(date);
            return dateObj.getTime();
        }catch (Exception e){
            YgoLog.d("Error",e.getMessage());
        }
       return 0;
    }
    public static Date pareStringToDate(String date){
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateObj = getCurrentDate();
        try {
            dateObj = df.parse(date);
            return dateObj;
        }catch (Exception e){

        }
       return null;
    }
    public static String pareDateToString(Date date, String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(date);
        return formattedDate;

    }
    public static Date pareStringToDateFormat(String date, String format){
        SimpleDateFormat df = new SimpleDateFormat( format,Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateObj = getCurrentDate();
        try {
            dateObj = df.parse(date);
            return dateObj;
        }catch (Exception e){
//            if(BuildConfig.DEBUG){
//                e.printStackTrace();
//            }
        }
        return null;
    }

    public static Date pareStringToJPDateFormat(String date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Japan"));

        Date dateObj = getCurrentJPDate();
        try {
            dateObj = dateFormat.parse(date);
            return dateObj;
        }catch (Exception e){
//            if(BuildConfig.DEBUG){
//                e.printStackTrace();
//            }
        }
        return null;
    }

    public static boolean printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

       YgoLog.i("PHOTO_TEST",
                "%d days, %d hours, %d minutes, %d seconds%n"+
                elapsedDays+
                elapsedHours+ elapsedMinutes+ elapsedSeconds);
        if(elapsedDays >= 1)
            return true;
    return false;
    }

    public static long calDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return elapsedDays;
    }

    public static String parseDateToStringWithFormatAndTimeZone(Date date,String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = df.format(date.getTime());
        return formattedDate;
    }

    public static String parseDateToStringWithFormat(Date date,String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(date.getTime());
        return formattedDate;
    }
    public static String parseDateToStringWithFormatPickup(Date date,String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date.getTime());
        YgoLog.d("DATE - parse format: ", formattedDate);
        return formattedDate;
    }
    public static long minusDate(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

//        YgoLog.i("PHOTO_TEST",
//                "%d days, %d hours, %d minutes, %d seconds%n"+
//                        elapsedDays+
//                        elapsedHours+ elapsedMinutes+ elapsedSeconds);
       return elapsedDays;
    }

    public static long minusHours(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = different / hoursInMilli;

//        YgoLog.i("PHOTO_TEST",
//                "%d days, %d hours, %d minutes, %d seconds%n"+
//                        elapsedDays+
//                        elapsedHours+ elapsedMinutes+ elapsedSeconds);
        return elapsedHours;
    }

    public static long minusMinutes(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;

        long elapsedMinutes = different / minutesInMilli;

        return elapsedMinutes;
    }

    public static boolean minusSmallerDate(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        YgoLog.i("PHOTO_TEST","-"+String.valueOf(elapsedDays)+"-"+
                String.valueOf(elapsedHours)+"-"+ String.valueOf(elapsedMinutes)+ "-"+String.valueOf(elapsedSeconds));
        if(elapsedDays > 0){
            return false;
        }else if(elapsedHours > 0){
            return false;
        }else if (elapsedMinutes > 0){
            return false;
        }else if (elapsedSeconds > 0){
            return false;
        }else{
            return true;
        }
    }

    public static Date parseISOStringToDate(String iso8601string){
        Date date = null;
        String s = iso8601string.replace("Z", "+0000");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(s);
        } catch (Exception e) {

        }
        return date;
    }

    public static Date parseISOStringToDateWithFormat(String iso8601string,String format){
        Date date = null;
        format ="yyyy-mm-dd'T'HH:mm:ss.SSSZ";
        String s = iso8601string.replace("Z", "+0000");
        try {
//            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
            date = new SimpleDateFormat(format,Locale.ENGLISH).parse(s);
        } catch (Exception e) {
//            if(BuildConfig.DEBUG){
//                e.printStackTrace();
//            }
        }
        return date;
    }

    public static boolean isBetweenDates(Date startDate, Date endDate){
        Date curDate = getCurrentJPDate();
        if(curDate.after(startDate) && curDate.before(endDate)){
            return true;
        }
        return false;
    }

}
