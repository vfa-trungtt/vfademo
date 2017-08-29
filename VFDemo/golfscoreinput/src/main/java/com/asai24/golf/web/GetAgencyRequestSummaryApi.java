package com.asai24.golf.web;

import android.net.Uri;

import com.asai24.golf.Constant;
import com.asai24.golf.domain.AgencyRequestSummaryObj;
import com.asai24.golf.utils.YgoLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GetAgencyRequestSummaryApi extends AbstractWebAPI {

    private Constant.ErrorServer mResult;

    public GetAgencyRequestSummaryApi() {
        setmResult(Constant.ErrorServer.NONE);
    }

    public AgencyRequestSummaryObj get(String token) {

        // TayPVS - Fix encode token
        Uri.Builder uriBuilder = Uri.parse(Constant.URL_AGENCY_REQUEST_SUMMARY).buildUpon();
        uriBuilder = uriBuilder.appendQueryParameter("auth_token", token);
        String url = uriBuilder.toString();
        // End TayPVS - Fix encode token

        YgoHttpGet httpGet = new YgoHttpGet(url);
        HttpClient httpclient = getDefaultHttpClient();

        try {
            HttpResponse response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 400) {

                String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                JSONObject jsonObject = new JSONObject(jsonText);
                YgoLog.i("GetAgencyRequestSummaryApi",jsonText);
                AgencyRequestSummaryObj summaryObj = new AgencyRequestSummaryObj();
                summaryObj.setCanceledNumber(jsonObject.getInt("cnt_canceled"));
                summaryObj.setRequestedNumber(jsonObject.getInt("cnt_all"));
                summaryObj.setMax(jsonObject.getInt("max"));
                summaryObj.setCampaignUserExperience(jsonObject.getBoolean("entered_default_campaign"));

                return summaryObj;
            } else if (statusCode == 401) {
                setmResult(Constant.ErrorServer.ERROR_E0105);
            } else {
                setmResult(Constant.ErrorServer.ERROR_GENERAL);
            }
        } catch (Exception e) {
            setmResult(Constant.ErrorServer.ERROR_CONNECT_TIMEOUT);
        }

        return null;
    }

    public void setmResult(Constant.ErrorServer mResult) {
        this.mResult = mResult;
    }

    public Constant.ErrorServer getmResult() {
        return mResult;
    }

}
