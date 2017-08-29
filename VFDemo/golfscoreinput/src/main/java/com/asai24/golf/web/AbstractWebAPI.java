package com.asai24.golf.web;

import android.content.Context;
import android.net.Proxy;

import com.asai24.golf.Constant.ErrorServer;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLPeerUnverifiedException;

public class AbstractWebAPI {

	private static final int CONNECT_TIMEOUT = 5000;
	private static final int TIMEOUT = 30000;
	protected void setProxy(HttpClient httpclient, Context aContext) {
		String host = Proxy.getHost(aContext);
		int port = Proxy.getPort(aContext);
		if (host == null || host.length() == 0 || port == 0) {
			return;
		}
		HttpHost proxy = new HttpHost(host, port);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), 300);
	}


	// TODO should be protected
	public HttpClient getDefaultHttpClient(HttpGet request) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		return httpclient;
	}
	
	protected HttpClient getDefaultHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		return httpclient;
	}
	protected boolean isSuccess(HttpResponse response) {
		int code = response.getStatusLine().getStatusCode();
		return (code >= 200 && code < 400);
	}

	protected ErrorServer parseErrorResponse(HttpResponse response) throws ParseException, IOException, JSONException {
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode >= 400 && statusCode < 500) {
			String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			JSONObject jsonObject = new JSONObject(jsonText);
			String errorCode = jsonObject.getString("code");
			return ErrorServer.valueOf("ERROR_" + errorCode);
		}else{
			return ErrorServer.ERROR_E0001;
		}
	}
	
	protected ErrorServer handleNetworkException(IOException e) throws IOException {
		// could not connect server
		if(e instanceof HttpHostConnectException) {
			return ErrorServer.ERROR_CONNECT_TIMEOUT;
		}
		if(e instanceof UnknownHostException) {
			return ErrorServer.ERROR_CONNECT_TIMEOUT;
		}
		if(e instanceof ConnectTimeoutException) {
			return ErrorServer.ERROR_CONNECT_TIMEOUT;
		}
		// could connect server but got an exception
		if(e instanceof SocketTimeoutException) {
			return ErrorServer.ERROR_SOCKET_TIMEOUT;
		}
		if(e instanceof SSLPeerUnverifiedException) {
			return ErrorServer.ERROR_SOCKET_TIMEOUT;
		}
		// I have no idea about what happened.
		throw e;
	}

}
