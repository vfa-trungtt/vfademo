package com.asai24.golf.web;

import android.text.Html;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;
import com.asai24.golf.domain.ClubObj;
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

public class HistoryClubAPI extends AbstractWebAPI {
	
	public static final String KEY_PAGE = "page";
	public static final String KEY_TOTAL = "total";
	public static final String KEY_CLUBS = "clubs";
	public static final String KEY_ID = "id";
	public static final String KEY_EXT_TYPE = "ext_type";
	public static final String KEY_EXT_ID = "ext_id";
	public static final String KEY_URL = "url";
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_CITY = "city";
	public static final String KEY_STATE = "state";
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_TYPE = "type";
	public static final String KEY_RATING = "rating";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	
	public static final String KEY_PARAMS_AUTH_TOKEN = "auth_token";
	public static final String KEY_PARAMS_PAGE = "page";
	public static final String KEY_ONLY_YOURGOLF = "only_yourgolf2";
	
	private ErrorServer mResult;
	private int page;
	private int total;

	public HistoryClubAPI() {
		setmResult(ErrorServer.NONE);
		setPage(0);
		setTotal(0);
	}

	public ArrayList<ClubObj> getSearchResult(HashMap<String, Object> params) {
		
		try {
			
			HttpResponse response = execSearch(params);
			
			if(null != response){
				final int statusCode = response.getStatusLine().getStatusCode();
				if(statusCode == 401) {
					setmResult(ErrorServer.ERROR_E0105);
				}
				if (statusCode == 200) {
					
					String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					
					if(null != jsonText && ! jsonText.equals("")){
						
						JSONObject jsonObject = new JSONObject(jsonText);
						
						ArrayList<ClubObj> arrCLubs = new ArrayList<ClubObj>();
						
						setPage(jsonObject.getInt(KEY_PAGE));
						setTotal(jsonObject.getInt(KEY_TOTAL));
						
						JSONArray jsonArrCLubs = jsonObject.getJSONArray(KEY_CLUBS);
						
						if(null != jsonArrCLubs && jsonArrCLubs.length() > 0) {
							for (int i = 0; i < jsonArrCLubs.length(); i++) {
								
								JSONObject jsonCLub = jsonArrCLubs.getJSONObject(i);
								
								ClubObj clubObj = new ClubObj();
								
								clubObj.setIdServer(jsonCLub.getString(KEY_ID));
								clubObj.setExtType(jsonCLub.getString(KEY_EXT_TYPE));
								clubObj.setExtId(jsonCLub.getString(KEY_EXT_ID));
								clubObj.setUrl(jsonCLub.getString(KEY_URL));
								clubObj.setAddress(jsonCLub.getString(KEY_ADDRESS));
								
								clubObj.setClubName(Html.fromHtml(jsonCLub.getString(KEY_NAME)).toString());
								clubObj.setCity(jsonCLub.getString(KEY_CITY));
								clubObj.setState(jsonCLub.getString(KEY_STATE));
								clubObj.setCountry(jsonCLub.getString(KEY_COUNTRY));
								clubObj.setPhoneNumber(jsonCLub.getString(KEY_PHONE));
								clubObj.setType(jsonCLub.getString(KEY_TYPE));
								clubObj.setRating(jsonCLub.getString(KEY_RATING));
								clubObj.setDistance(jsonCLub.getLong(KEY_DISTANCE));
								clubObj.setLat(jsonCLub.getDouble(KEY_LAT));
								clubObj.setLng(jsonCLub.getDouble(KEY_LNG));
								
								arrCLubs.add(clubObj);
								
							}
						}
						
						return arrCLubs;
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

	
	private HttpResponse execSearch(HashMap<String, Object> params) throws Exception  {
				
		String url = Constant.URL_CLUBS_LIST_HISTORY;
		url +=  "?" + KEY_PARAMS_AUTH_TOKEN + "=" + URLEncoder.encode(String.valueOf(params.get(KEY_PARAMS_AUTH_TOKEN)));
		url += "&" + Constant.KEY_APP + "=" + String.valueOf(params.get(Constant.KEY_APP));
		url += "&" + KEY_PARAMS_PAGE + "=" + (Integer)params.get(KEY_PARAMS_PAGE);
		if(params.containsKey(KEY_ONLY_YOURGOLF)){
			url += "&" + KEY_ONLY_YOURGOLF + "=" + (Integer)params.get(KEY_ONLY_YOURGOLF);
		}
		YgoLog.i("HistoryClubAPI", "execSearch URL for club history is " + url);
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

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}
	
	
}
