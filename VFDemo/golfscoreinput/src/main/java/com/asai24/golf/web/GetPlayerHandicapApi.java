package com.asai24.golf.web;

import android.net.Uri;
import android.net.Uri.Builder;

import com.asai24.golf.Constant;
import com.asai24.golf.domain.PlayerObj;
import com.asai24.golf.utils.YgoLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author thuna
 *
 */
public class GetPlayerHandicapApi extends AbstractWebAPI {

    private String KEY_EMAIL= "email";
    private String KEY_FLOAT_PLAYER_HDCP = "float_player_hdcp";
	
	private Map<String, String> baseParams;

	public GetPlayerHandicapApi(Map<String, String> baseParams) {
		this.baseParams = baseParams;
	}

	public PlayerObj get() throws ParseException, JSONException, IOException {
		PlayerObj result = new PlayerObj();
		
		YgoHttpGet httpGet = new YgoHttpGet(makeUrl());
		YgoLog.d("GolfLoginAct","url: "+makeUrl());
		HttpClient httpclient= getDefaultHttpClient();

		try {
			HttpResponse response = httpclient.execute(httpGet);
			if(isSuccess(response)) {
				String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				YgoLog.d("GolfLoginAct","json: "+jsonText);
				result = parse(jsonText);
			}else{
				result.setErrorStatus(parseErrorResponse(response));
					YgoLog.d("GolfLoginAct","json: "+parseErrorResponse(response).toString());
			}
		} catch (IOException e) {
			result.setErrorStatus(handleNetworkException(e));
		}
		
		return result;
	}
	
	public PlayerObj get(Map<String, String> params) throws ParseException, JSONException, IOException {
		baseParams.putAll(params);
		return get();
	}

	private PlayerObj parse(String jsonText) throws ParseException, JSONException {
		PlayerObj result = new PlayerObj();
		
		JSONObject jsonObject = new JSONObject(jsonText);
		result.setEmail(jsonObject.getString(KEY_EMAIL));

        String hdcp = jsonObject.getString(KEY_FLOAT_PLAYER_HDCP);
        if(!(null == hdcp || hdcp.equals("") || hdcp.equals("null"))){
            // HDCP has format with XX.X
            hdcp = hdcp.trim();
            String element[] = hdcp.split("\\.");
            if (element.length > 1) {
                if (element[1].length() > 1) { // Ignore XX.XX
                    hdcp = element[0] + "." + element[1].substring(0, 1);
                }
            } else {
                hdcp += ".0";
            }
        }
		result.setPlayerHdcp(hdcp);
		return result;
	}
	
	private String makeUrl() {
		Builder uriBuilder = Uri.parse(Constant.URL_SELECT_PLAYER_HANDICAP.replace(":id", baseParams.get("id"))).buildUpon();
		for (Iterator<String> it = baseParams.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			uriBuilder = uriBuilder.appendQueryParameter(key, baseParams.get(key));
		}
        YgoLog.i("GetPlayerHandicapAPi", "++++++++++ url ++++++" + uriBuilder.toString());
		return uriBuilder.toString();
	}
}
