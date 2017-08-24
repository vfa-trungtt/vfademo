package com.asai24.golf.web;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;
import com.asai24.golf.GolfApplication;
import com.asai24.golf.common.Distance;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;

public class CourseGetAPIJson  extends AbstractWebAPI {
	public static final String JS_AUTHOR_KEY="auth_token";
	
	private Context mContext;
	private ErrorServer mResult;
	
	public CourseGetAPIJson(Context context) {
		mContext = context;
		setmResult(ErrorServer.NONE);
	}
	
	public boolean APIDeleteHistoryJson(String id)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(CourseGetAPIJson.JS_AUTHOR_KEY, Distance.getAuthTokenLogin(mContext));
		params.put(Constant.KEY_APP,
				Constant.KEY_API_APP_VERSION.replace(Constant.KEY_VERSION_NAME, GolfApplication.getVersionName()));
		Builder uriBuilder = Uri.parse(Constant.URL_HISTORY_DELETE_LIST_HISTORY_JSON.replace("roundid", id)).buildUpon();
		for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			uriBuilder = uriBuilder.appendQueryParameter(key, params.get(key));
		}		
		HttpDelete httpJsonRequest = new HttpDelete(uriBuilder.toString());	
		HttpClient httpclientForJson = getDefaultHttpClient(null);
		 
		try {
			HttpResponse responseForJson = (HttpResponse) httpclientForJson.execute(httpJsonRequest);
			if(null !=responseForJson)
			{
				final int statusCode = responseForJson.getStatusLine().getStatusCode();
				
				switch (statusCode) {
				case 200:			
						return true;
				case 401:
					setmResult(ErrorServer.ERROR_E0105);
					return false;
				
				case 403:
//					setmResult(ErrorServer.ERROR_E0105);
//					String code=jsonObject.getJSONObject(JS_CODE).toString();
//					if(code.equals("E0111"))
					setmResult(ErrorServer.ERROR_E0111);
//					if(code.equals("E0114"))
//						setmResult(ErrorServer.ERROR_E0114);
					return false;
				}
			
			}
		}
		catch (SocketTimeoutException e) {
			setmResult(ErrorServer.ERROR_SOCKET_TIMEOUT);
		}
		catch (ConnectTimeoutException e) {
			setmResult(ErrorServer.ERROR_CONNECT_TIMEOUT);
		}
		catch (Exception e) {
			setmResult(ErrorServer.ERROR_GENERAL);
		}
		return false;
	}
	public void setmResult(ErrorServer mResult) {
		this.mResult = mResult;
	}

	public ErrorServer getmResult() {
		return mResult;
	}
}
