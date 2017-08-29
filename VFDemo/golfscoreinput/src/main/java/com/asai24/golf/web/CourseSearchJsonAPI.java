package com.asai24.golf.web;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.Html;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;
import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.domain.Course;
import com.asai24.golf.utils.YgoLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CourseSearchJsonAPI extends AbstractWebAPI {

    private String TAG = CourseSearchJsonAPI.class.getSimpleName()+"-golf";
	public static final String KEY_PAGE = "page";
	public static final String KEY_TOTAL = "total";
	public static final String KEY_CLUBS = "clubs";
	public static final String KEY_ID = "id";
	public static final String KEY_EXT_TYPE = "ext_type";
	public static final String KEY_NAME = "name";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_STATE = "state";
	public static final String KEY_CITY = "city";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_URL = "url";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_LNG = "lng";
	public static final String KEY_LAT = "lat";
	public static final String KEY_COURSES = "courses";
	public static final String KEY_HOLES = "holes";
	
	public static final String KEY_PARAMS_AUTH_TOKEN = "auth_token";
	public static final String KEY_PARAMS_KEYWORDS = "keywords";
	public static final String KEY_PARAMS_RADIUS = "radius";
	public static final String KEY_PARAMS_LAT = "lat";
	public static final String KEY_PARAMS_LNG = "lng";
	public static final String KEY_PARAMS_PAGE = "page";
	public static final String KEY_PARAMS_PAGE_SIZE = "page_size";
	public static final String KEY_ONLY_YOURGOLF = "only_yourgolf";
	private static final String KEY_RAKUTEN_ID = "rakuten_id";
	
	private ErrorServer mResult;

	public CourseSearchJsonAPI() {
		setmResult(ErrorServer.NONE);
	}

	public CourseSearchResult getSearchResult(HashMap<String, String> params) {
		
		CourseSearchResult searchResult = new CourseSearchResult();
		ArrayList<ClubObj> arrCLubObj = new ArrayList<ClubObj>();
		
		try {
			
			HttpResponse response = execSearch(params);
			
			if(null != response){
				final int statusCode = response.getStatusLine().getStatusCode();
				if(statusCode == 401) {
					setmResult(ErrorServer.ERROR_E0105);
				}
				else if(statusCode == 400) {
					setmResult(ErrorServer.ERROR_E0111);
				}
				else if (statusCode == 200) {
					
					String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					if(null != jsonText && ! jsonText.equals("")){
                        YgoLog.i(TAG,"getSearchResult json: " + jsonText);
						JSONObject jsonObject = new JSONObject(jsonText);
						
						searchResult.setCurrentPage(jsonObject.getInt(KEY_PAGE));
						searchResult.setTotal(jsonObject.getInt(KEY_TOTAL));
						
						JSONArray arrJsonClubs = jsonObject.getJSONArray(KEY_CLUBS);
						
						if(null != arrJsonClubs && arrJsonClubs.length() > 0) {
							
							for (int i = 0; i < arrJsonClubs.length(); i++) {
								
								JSONObject jsonClub = arrJsonClubs.getJSONObject(i);
								
								ClubObj clubObj = new ClubObj();
								clubObj.setExtId(jsonClub.getString(KEY_ID));
								clubObj.setExtType(jsonClub.getString(KEY_EXT_TYPE));
								
								String clubName = Html.fromHtml(jsonClub.getString(KEY_NAME)).toString();
								clubObj.setClubName(clubName);
								
								clubObj.setCountry(Html.fromHtml(jsonClub.getString(KEY_COUNTRY)).toString());
								clubObj.setState(Html.fromHtml(jsonClub.getString(KEY_STATE)).toString());
								clubObj.setCity(Html.fromHtml(jsonClub.getString(KEY_CITY)).toString());
								clubObj.setAddress(Html.fromHtml(jsonClub.getString(KEY_ADDRESS)).toString());
								clubObj.setUrl(jsonClub.getString(KEY_URL));
								clubObj.setPhoneNumber(jsonClub.getString(KEY_PHONE));
								
								String distance = jsonClub.getString(KEY_DISTANCE);
								clubObj.setDistance((null != distance && !distance.equals("")) ? new Long(distance) : 0);
								
								clubObj.setLat(jsonClub.getDouble(KEY_LAT));
								clubObj.setLng(jsonClub.getDouble(KEY_LNG));
								if(!jsonClub.isNull(KEY_RAKUTEN_ID)) {
									clubObj.setRakutenId(jsonClub.getString(KEY_RAKUTEN_ID));
								}
								
								
								JSONArray arrJsonCourse = jsonClub.getJSONArray(KEY_COURSES);
								
								if(null != arrJsonCourse && arrJsonCourse.length() > 0) {
									
									ArrayList<Course> arrCourseObj = new ArrayList<Course>();
									
									for (int j = 0; j < arrJsonCourse.length(); j++) {
										
										JSONObject jsonCourse = arrJsonCourse.getJSONObject(j);
										
										Course courseObj = new Course();
										courseObj.setClubName(clubObj.getClubName());
										if(clubObj.getExtType().equals(Constant.EXT_TYPE_OOBGOLF)) {
											courseObj.setOobId(jsonCourse.getString(KEY_ID));
										}
										else {
											courseObj.setYourGolfId(jsonCourse.getString(KEY_ID));
										}
										courseObj.setCourseName(Html.fromHtml(jsonCourse.getString(KEY_NAME)).toString());
										courseObj.setHoles(jsonCourse.getLong(KEY_HOLES));
										
										// Add course object to array
										arrCourseObj.add(courseObj);
									}
									
									// Add array course to club object
									clubObj.setCourses(arrCourseObj);
								}
								// Add club object to array
								arrCLubObj.add(clubObj);
							}
						}						
					}
				}
			}
		} 
		catch (SocketTimeoutException e) {
			setmResult(ErrorServer.ERROR_SOCKET_TIMEOUT);
		}
		catch (ConnectTimeoutException e) {
			setmResult(ErrorServer.ERROR_CONNECT_TIMEOUT);
		}
		catch (HttpHostConnectException e) {
			setmResult(ErrorServer.ERROR_CONNECT_TIMEOUT);
		}
		catch (Exception e) {
			setmResult(ErrorServer.ERROR_GENERAL);
			e.printStackTrace();
		}

		// Add array club to search result
		searchResult.setClubs(arrCLubObj);
		return searchResult;
	}
	
	private HttpResponse execSearch(HashMap<String, String> params) throws Exception  {
				
		Builder uriBuilder = Uri.parse(Constant.URL_COURSE_SEARCH).buildUpon();
		for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			uriBuilder = uriBuilder.appendQueryParameter(key, params.get(key));
		}
		YgoLog.i("CourseSearchJsonApi-golf", "URL for search club: " + uriBuilder.toString());
		YgoHttpGet httpGet = new YgoHttpGet(uriBuilder.toString());
		HttpClient httpclient=getDefaultHttpClient(httpGet);
		
		return httpclient.execute(httpGet);

	}

	public void setmResult(ErrorServer mResult) {
		this.mResult = mResult;
	}

	public ErrorServer getmResult() {
		return mResult;
	}
	
	
}
