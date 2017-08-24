package com.asai24.golf.web;

import com.asai24.golf.Constant;
import com.asai24.golf.domain.GoraGolfCourseDetailResult;
import com.asai24.golf.utils.YgoLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GoraGolfCourseDetailApi extends AbstractWebAPI {

	private static final String TAG = GoraGolfCourseDetailApi.class.getSimpleName();

	public GoraGolfCourseDetailResult get(String rakutenId) {
		GoraGolfCourseDetailResult result = new GoraGolfCourseDetailResult();
		
		YgoHttpGet httpGet = new YgoHttpGet(makeUrl(rakutenId));
		HttpClient httpclient= getDefaultHttpClient();
		
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if(isSuccess(response)) {
				String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                YgoLog.i(TAG, "json: " + jsonText);
				result = parse(jsonText);
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			YgoLog.e(TAG, "", e);
			result.setSuccess(false);
		}
		
		return result;
	}

	private GoraGolfCourseDetailResult parse(String jsonText) throws JSONException {
		GoraGolfCourseDetailResult result = new GoraGolfCourseDetailResult();
		
		JSONObject rootJson = new JSONObject(jsonText);
        //ticket 7227
//		String reserveCalUrl = rootJson.getJSONObject("Body")
//				.getJSONObject("GoraGolfCourseDetail").getJSONArray("ItemListRakuten")
//				.getJSONObject(0).getString("reserveCalUrl");
        String reserveCalUrl = rootJson.getJSONObject("ItemListRakuten").getString("reserveCalUrl");

		result.setReserveCalUrl(reserveCalUrl);
		
		return result;
	}

	private String makeUrl(String rakutenId) {
        YgoLog.i(TAG, "Rakuten URL: " + Constant.URL_GORA_GOLF_COURSE_DETAIL + rakutenId);
		return Constant.URL_GORA_GOLF_COURSE_DETAIL + rakutenId;
	}

}
