package com.asai24.golf.web;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;

public class DeleteHistoryCLubAPI extends AbstractWebAPI {
	
	public static final String KEY_CODE = "code";
	
	public static final String KEY_PARAMS_AUTH_TOKEN = "auth_token";
	public static final String KEY_PARAMS_CLUB_ID = "id";
	
	private ErrorServer mResult;

	public DeleteHistoryCLubAPI() {
		setmResult(ErrorServer.NONE);
	}

	public boolean getSearchResult(HashMap<String, String> params) {
		
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
						setmResult(ErrorServer.ERROR_E0120);
					}
				}
				else if (statusCode == 200) {
					return true;
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

		return false;
	}

	
	private HttpResponse execSearch(HashMap<String, String> params) throws Exception  {
				
		String url = Constant.URL_CLUBS_DELETE_HISTORY.replace(
						"clubID", params.get(KEY_PARAMS_CLUB_ID));
		url +=  "?" + KEY_PARAMS_AUTH_TOKEN + "=" + URLEncoder.encode(params.get(KEY_PARAMS_AUTH_TOKEN));
		url +=  "&" + Constant.KEY_APP + "=" +params.get(Constant.KEY_APP);
		
		HttpDelete httpDelete = new HttpDelete(url);
		HttpClient httpclient=getDefaultHttpClient();
		
		return httpclient.execute(httpDelete);

	}

	public void setmResult(ErrorServer mResult) {
		this.mResult = mResult;
	}

	public ErrorServer getmResult() {
		return mResult;
	}
	
	
}
