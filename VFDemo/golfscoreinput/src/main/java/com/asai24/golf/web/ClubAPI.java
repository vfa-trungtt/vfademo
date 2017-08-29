package com.asai24.golf.web;

import android.net.Uri;
import android.net.Uri.Builder;

import com.asai24.golf.Constant;
import com.asai24.golf.db.Golf.Course.ExtType;
import com.asai24.golf.domain.ClubDownloadResult;
import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.domain.Course;
import com.asai24.golf.domain.GolfDayClub;
import com.asai24.golf.utils.YgoLog;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClubAPI extends AbstractWebAPI {

	private Map<String, String> baseParams;

	public ClubAPI(Map<String, String> baseParams) {
		this.baseParams = baseParams;
	}

	public ClubDownloadResult get(String clubId) throws ParseException, JSONException, IOException {
		ClubDownloadResult result = new ClubDownloadResult();
		
		YgoHttpGet httpGet = new YgoHttpGet(makeUrl(clubId));
		HttpClient httpclient= getDefaultHttpClient();
		
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if(isSuccess(response)) {
				String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				result = parse(jsonText);
                YgoLog.d("TayPVS" , "TayPVS - club obj - " + jsonText);
			}else{
				result.setErrorStatus(parseErrorResponse(response));
			}
		} catch (IOException e) {
			result.setErrorStatus(handleNetworkException(e));
		}
		
		return result;
	}

	private ClubDownloadResult parse(String jsonText) throws JSONException {
		ClubDownloadResult result = new ClubDownloadResult();
		ClubObj club = new ClubObj();
		List<Course> courses = new ArrayList<Course>();
        List<GolfDayClub> golfDayClubs = new ArrayList<GolfDayClub>();
		
		JSONObject clubJson = new JSONObject(jsonText);
		club.setAddress(clubJson.getString("address"));
		club.setCity(clubJson.getString("city"));
		club.setClubName(clubJson.getString("name"));
		club.setCountry(clubJson.getString("country"));
		club.setExtId(clubJson.getString("id"));
		club.setExtType(ExtType.YourGolf2.toString());
		club.setPhoneNumber(clubJson.getString("phone"));
		club.setState(clubJson.getString("state"));
		club.setUrl(clubJson.getString("url"));
		if(!clubJson.isNull("rakuten_id")) {
			club.setRakutenId(clubJson.getString("rakuten_id"));
		}
		
		if(!clubJson.isNull("loc")) {
			JSONArray locJson = clubJson.getJSONArray("loc");
			club.setLat(locJson.getDouble(0));
			club.setLng(locJson.getDouble(1));
		}
		
		if(!clubJson.isNull("courses")) {
			JSONArray coursesJson = clubJson.getJSONArray("courses");
			for (int i = 0; i < coursesJson.length(); i++) {
				Course course = new Course();
				JSONObject courseJson = coursesJson.getJSONObject(i);
				course.setYourGolfId(courseJson.getString("id"));
				course.setHoles(courseJson.getLong("hole_count"));
				course.setCourseName(courseJson.getString("name"));
				courses.add(course);
			}
		}

        if(!clubJson.isNull("golf_day_clubs1")) {
            JSONArray golfDaysJson = clubJson.getJSONArray("golf_day_clubs1");
            for (int i = 0; i < golfDaysJson.length(); i++) {
                GolfDayClub clubs = new GolfDayClub();
                JSONObject golfDayClubJson = golfDaysJson.getJSONObject(i);
                clubs.setClubId(golfDayClubJson.getString("club_id"));
                clubs.setClubName(golfDayClubJson.getString("club_name"));
                YgoLog.d("TayPVS", "TayPVS - golfDayClubJson " + golfDayClubJson.getString("club_name"));
                golfDayClubs.add(clubs);
            }
        }

		club.setCourses(courses);
        club.setGolfDayClubs(golfDayClubs);
		result.setClub(club);

		return result;
	}

	private String makeUrl(String clubId) {
		String baseUrl = Constant.URL_V1_CLUBS + "/" + clubId + ".json";
		Builder uriBuilder = Uri.parse(baseUrl).buildUpon();
		for (Iterator<String> it = baseParams.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			uriBuilder = uriBuilder.appendQueryParameter(key, baseParams.get(key));
		}
		
		return uriBuilder.toString();
	}

}
