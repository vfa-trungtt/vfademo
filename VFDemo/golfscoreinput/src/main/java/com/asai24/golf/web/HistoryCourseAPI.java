package com.asai24.golf.web;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;
import com.asai24.golf.domain.Course;
import com.asai24.golf.domain.Hole;
import com.asai24.golf.domain.Tee;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class HistoryCourseAPI extends AbstractWebAPI {
	
	public static final String KEY_COURSE = "course";
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_EXT_ID = "ext_id";
	public static final String KEY_TEE = "tee";
	public static final String KEY_HOLES = "holes";
	public static final String KEY_HOLE_NUMBER = "hole_number";
	public static final String KEY_PAR = "par";
	public static final String KEY_WOMEN_PAR = "women_par";
	public static final String KEY_YARD = "yard";
	public static final String KEY_HANDICAP = "handicap";
	public static final String KEY_WOMEN_HANDICAP = "women_handicap";
	public static final String KEY_LNG = "lng";
	public static final String KEY_LAT = "lat";
	public static final String KEY_CODE = "code";
	
	public static final String KEY_PARAMS_AUTH_TOKEN = "auth_token";
	public static final String KEY_PARAMS_CLUB_ID = "id";
	public static final String KEY_PARAMS_EXT_TYPE= "ext_type";
	
	private ErrorServer mResult;

	public HistoryCourseAPI() {
		setmResult(ErrorServer.NONE);
	}

	public ArrayList<Course> getSearchResult(HashMap<String, String> params) {
		
		try {
			
			HttpResponse response = execSearch(params);
			
			if(null != response){
				final int statusCode = response.getStatusLine().getStatusCode();
				if(statusCode == 401) {
					setmResult(ErrorServer.ERROR_E0105);
				}
				else if(statusCode == 403) {
					String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					if(null != jsonText && ! jsonText.equals("")){
						JSONObject jsonObject = new JSONObject(jsonText);
						if(jsonObject.getString(KEY_CODE).equals("E0119")){
							setmResult(ErrorServer.ERROR_E0119);
						}
					} else {
						setmResult(ErrorServer.ERROR_E0121);
					}
				}
				else if (statusCode == 200) {
					
					String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					
					if(null != jsonText && ! jsonText.equals("")){
						
						JSONObject jsonObject = new JSONObject(jsonText);
						
						Course courseObj = new Course();
												
						JSONObject jsonCourse = jsonObject.getJSONObject(KEY_COURSE);
						JSONObject jsonTee = jsonObject.getJSONObject(KEY_TEE);
						JSONArray jsonArrHoles = jsonObject.getJSONArray(KEY_HOLES);
						
						if(null != jsonCourse && jsonCourse.length() > 0) {
							courseObj.setIdServer(jsonCourse.getString(KEY_ID));
							courseObj.setCourseName(jsonCourse.getString(KEY_NAME));
							if(params.get(KEY_PARAMS_EXT_TYPE).equals(Constant.EXT_TYPE_OOBGOLF)) {
								courseObj.setOobId(jsonCourse.getString(KEY_EXT_ID));
							}
							else {
								courseObj.setYourGolfId(jsonCourse.getString(KEY_EXT_ID));
							}
							
							if(null != jsonTee && jsonTee.length() > 0) {
								
								ArrayList<Tee> arrTees = new ArrayList<Tee>();
								
								Tee teeObj = new Tee();
								teeObj.setIdServer(jsonTee.getString(KEY_ID));
								teeObj.setName(jsonTee.getString(KEY_NAME));
								teeObj.setOobId(jsonTee.getString(KEY_EXT_ID));
								
								if(null != jsonArrHoles && jsonArrHoles.length() > 0) {
									
									ArrayList<Hole> arrHoles = new ArrayList<Hole>();
									
									for (int i = 0; i < jsonArrHoles.length(); i++) {
										JSONObject jsonHole = jsonArrHoles.getJSONObject(i);
										
										Hole holeObj = new Hole();
										holeObj.setHoleNumber(jsonHole.getInt(KEY_HOLE_NUMBER));
										holeObj.setPar(jsonHole.getInt(KEY_PAR));
										holeObj.setWomenPar(jsonHole.getInt(KEY_WOMEN_PAR));
										holeObj.setYard(jsonHole.getInt(KEY_YARD));
										holeObj.setHandicap(jsonHole.getInt(KEY_HANDICAP));
										holeObj.setWomenHandicap(jsonHole.getInt(KEY_WOMEN_HANDICAP));
										holeObj.setLongitude(jsonHole.getDouble(KEY_LNG));
										holeObj.setLatitude(jsonHole.getDouble(KEY_LAT));
										
										arrHoles.add(holeObj);
									}
									
									teeObj.setHoles(arrHoles);
								}
								
								arrTees.add(teeObj);
								courseObj.setTees(arrTees);
							}
						}
						
						ArrayList<Course> arrCourse = new ArrayList<Course>();
						arrCourse.add(courseObj);
						
						return arrCourse;
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
			e.printStackTrace();
			setmResult(ErrorServer.ERROR_GENERAL);
		}

		return null;
	}

	
	private HttpResponse execSearch(HashMap<String, String> params) throws Exception  {
				
		String url = Constant.URL_CLUBS_COURSE_HISTORY.replace(
						"clubID", params.get(KEY_PARAMS_CLUB_ID));
		url +=  "?" + KEY_PARAMS_AUTH_TOKEN + "=" + URLEncoder.encode(params.get(KEY_PARAMS_AUTH_TOKEN));
		url += "&" + Constant.KEY_APP + "=" + String.valueOf(params.get(Constant.KEY_APP));
        YgoLog.i("HistoryCourseAPI", "URL to get course array " + url);
		YgoHttpGet httpGet = new YgoHttpGet(url);
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
