package com.asai24.golf.web;

import android.net.Uri;
import android.net.Uri.Builder;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;

public class BatchAPI extends AbstractWebAPI {

	public static final String KEY_OPS = "ops";
	public static final String KEY_METHOD = "method";
	public static final String KEY_URL = "url";
	public static final String KEY_PARAMS = "params";
    public static final String KEY_PLAYER_ID = "player_id";
    public static final String KEY_LIVE_ENTRY_ID = "live_entry_id";
    public static final String KEY_COURSE_ID = "course_id";
    public static final String KEY_SEQUENTIAL = "sequential";
    public static final String KEY_RESULTS = "results";
    public static final String KEY_BODY = "body";
    public static final String KEY_ID = "id";
    public static final String KEY_HEADERS = "headers";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static final String KEY_STATUS = "status";


	public static final String KEY_PARAMS_AUTH_TOKEN = "auth_token";

	private ErrorServer mResult;

    private String jsonRequest;

    private String jsonResponse;

	public BatchAPI() {
		setmResult(ErrorServer.NONE);
        setJsonRequest("");
        setJsonResponse("");
	}

	public HashMap<String, String> execBatch(HashMap<String, String> params, String[] playerIds) {
		
		try {
			
			HttpResponse response = exec(params, playerIds);
			
			if(null != response){
				final int statusCode = response.getStatusLine().getStatusCode();
				if(statusCode == 401) {
					setmResult(ErrorServer.ERROR_E0105);
				}
				if (statusCode == 200) {
					
					String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

					if(null != jsonText && ! jsonText.equals("")){

                        setJsonResponse(jsonText);

						JSONObject jsonObject = new JSONObject(jsonText);
						
						JSONArray arrJsonResponse = jsonObject.getJSONArray(KEY_RESULTS);
                        if(null != arrJsonResponse && arrJsonResponse.length() > 0){

                            HashMap<String, String> hashResult = new HashMap<String, String>();
                            for (int i = 0; i < arrJsonResponse.length(); i++){
                                JSONObject jsonResponse = arrJsonResponse.getJSONObject(i);

                                if(null != jsonResponse && jsonResponse.length() > 0
                                        && jsonResponse.getInt(KEY_STATUS) == 200){

                                    String strBody = jsonResponse.getString(KEY_BODY);

                                    JSONObject jsonBody = new JSONObject(strBody);

                                    hashResult.put(playerIds[i], jsonBody.getString(KEY_ID));
                                }
                                else {
                                    setmResult(ErrorServer.ERROR_GENERAL);
                                    hashResult.put(playerIds[i], "");
                                }
                            }
                            return hashResult;
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
		}

		return null;
	}

	
	private HttpResponse exec(HashMap<String, String> params, String[] playerIds) throws Exception  {
				
		Builder uriBuilder = Uri.parse(Constant.URL_LIVE_BATCH).buildUpon();

        JSONArray arrJsonPlayers = new JSONArray();

        for (int i = 0; i < playerIds.length; i++){

            JSONObject jsonPlayer = new JSONObject();

            jsonPlayer.put(KEY_METHOD, "post");
            jsonPlayer.put(KEY_URL, Constant.URL_LIVE_URL_PLAYER);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put(KEY_PARAMS_AUTH_TOKEN, URLEncoder.encode(params.get(KEY_PARAMS_AUTH_TOKEN)));
            jsonParam.put(KEY_PLAYER_ID, playerIds[i]);
            jsonParam.put(KEY_LIVE_ENTRY_ID, params.get(KEY_LIVE_ENTRY_ID));
            jsonParam.put(KEY_COURSE_ID, params.get(KEY_COURSE_ID));

            jsonPlayer.put(KEY_PARAMS, jsonParam);

            arrJsonPlayers.put(i, jsonPlayer);
        }

        JSONObject jsonPost = new JSONObject();
        jsonPost.put(KEY_OPS, arrJsonPlayers);
        jsonPost.put(KEY_SEQUENTIAL, true);

        setJsonRequest(jsonPost.toString());

        StringEntity strEntity = new StringEntity(jsonPost.toString(), "UTF-8");
        strEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

		YgoHttpPost httpPost = new YgoHttpPost(uriBuilder.toString());
        // Prepare JSON to send by setting the entity
        httpPost.setEntity(strEntity);

        // Set up the header types needed to properly transfer JSON
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept-Encoding", "application/json");


		HttpClient httpclient=getDefaultHttpClient();
		
		return httpclient.execute(httpPost);

	}

	public void setmResult(ErrorServer mResult) {
		this.mResult = mResult;
	}

	public ErrorServer getmResult() {
		return mResult;
	}

    public String getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

}
