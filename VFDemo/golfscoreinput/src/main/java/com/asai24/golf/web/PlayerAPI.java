package com.asai24.golf.web;

import android.net.Uri;
import android.net.Uri.Builder;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;
import com.asai24.golf.domain.PlayerObj;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlayerAPI extends AbstractWebAPI {
	
	private Map<String, String> baseParams;

	public PlayerAPI(Map<String, String> baseParams) {
		this.baseParams = baseParams;
	}
	
	public ErrorServer put(PlayerObj player) throws ParseException, JSONException, IOException {
		ErrorServer errorServer = null;
		
		YgoHttpPut httpPut = new YgoHttpPut(makeUrl(player.getIdServer()));
		HttpClient httpclient= getDefaultHttpClient();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("display_as_friend", player.getDisplayAsFriend() ? "1" : "0"));
		params.add(new BasicNameValuePair("name", player.getName()));
		params.add(new BasicNameValuePair("email", player.getEmail()));
		httpPut.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		try {
			HttpResponse response = httpclient.execute(httpPut);
			if(!isSuccess(response)) {
				errorServer = parseErrorResponse(response);
			}
		} catch (IOException e) {
			errorServer = handleNetworkException(e);
		}
		
		return errorServer;
	}

	private String makeUrl(String playerId) {
		String baseUrl = Constant.URL_EDIT_PLAYER_NAME.replace("playerID", playerId);
		Builder uriBuilder = Uri.parse(baseUrl).buildUpon();
		for (Iterator<String> it = baseParams.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			uriBuilder = uriBuilder.appendQueryParameter(key, baseParams.get(key));
		}
		return uriBuilder.toString();
	}
}
