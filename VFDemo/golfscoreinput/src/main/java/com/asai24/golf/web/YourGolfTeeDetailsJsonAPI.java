package com.asai24.golf.web;

import android.net.Uri;
import android.net.Uri.Builder;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class YourGolfTeeDetailsJsonAPI extends AbstractWebAPI {
	private String TAG = YourGolfTeeDetailsJsonAPI.class.getSimpleName() + "-golf";
	public static final String KEY_TEES = "tees";
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_HOLES = "holes";
	public static final String KEY_NUM = "num";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_MEN_PAR = "men_par";
	public static final String KEY_WOMEN_PAR = "women_par";
	public static final String KEY_MEN_HANDICAP = "men_handicap";
	public static final String KEY_WOMEN_HANDICAP = "women_handicap";
	public static final String KEY_HANDICAP = "handicap";
	
	public static final String KEY_PARAMS_AUTH_TOKEN = "auth_token";
	public static final String KEY_PARAMS_ID = "id";
	
	private ErrorServer mResult;

	public YourGolfTeeDetailsJsonAPI() {
		setmResult(ErrorServer.NONE);
	}

	public List<Tee> getSearchResult(HashMap<String, String> params, String courseId) {
		
		List<Tee> tees = new ArrayList<Tee>();
				
		try {
			
			HttpResponse response = execSearch(params, courseId);
			
			if(null != response){
				final int statusCode = response.getStatusLine().getStatusCode();
				if(statusCode == 401) {
					setmResult(ErrorServer.ERROR_E0105);
				}
				else if(statusCode == 403) {
					setmResult(ErrorServer.ERROR_E0136);
				}
				else if (statusCode == 200) {
					
					String jsonText = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    YgoLog.i(TAG, "getSearchResult json: " + jsonText);
					if(null != jsonText && ! jsonText.equals("")){
								
						JSONObject jsonObject = new JSONObject(jsonText);
												
						JSONArray arrJsonTees = jsonObject.getJSONArray(KEY_TEES);
						
						if(null != arrJsonTees && arrJsonTees.length() > 0) {
							
							for (int i = 0; i < arrJsonTees.length(); i++) {
								
								JSONObject jsonTee = arrJsonTees.getJSONObject(i);
								
								Tee teeObj = new Tee();
								teeObj.setOobId(jsonTee.getString(KEY_ID));
								teeObj.setName(jsonTee.getString(KEY_NAME));
								
								
								JSONArray arrJsonHoles = jsonTee.getJSONArray(KEY_HOLES);
								
								if(null != arrJsonHoles && arrJsonHoles.length() > 0) {
																		
									for (int j = 0; j < arrJsonHoles.length(); j++) {
										
										JSONObject jsonHole = arrJsonHoles.getJSONObject(j);
										
										Hole holeObj = new Hole();
										holeObj.setHoleNumber(jsonHole.getInt(KEY_NUM));
										holeObj.setYard(jsonHole.getInt(KEY_DISTANCE));
										int menPar = Hole.PAR_UNKNOWN;
										if(!jsonHole.getString(KEY_MEN_PAR).trim().equals("null")){
											menPar = jsonHole.getInt(KEY_MEN_PAR);
										}
										int womenPar = Hole.PAR_UNKNOWN;
										if(!jsonHole.getString(KEY_WOMEN_PAR).trim().equals("null")){
											womenPar = jsonHole.getInt(KEY_WOMEN_PAR);
										}
										int handicap = Hole.HANDICAP_UNNOWN;
										if(!jsonHole.getString(KEY_HANDICAP).trim().equals("null")){
											handicap = jsonHole.getInt(KEY_HANDICAP);
										}
										
										holeObj.setPar(menPar);
										holeObj.setWomenPar(womenPar);
										holeObj.setHandicap(handicap);
										holeObj.setWomenHandicap(Hole.HANDICAP_UNNOWN);
										
										// Add hole  object to tee object
										teeObj.addHole(holeObj);
									}
								}
								// Add club object to array
								tees.add(teeObj);
							}
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
			e.printStackTrace();
		}

		return formatTess(tees);
	}
	
	private List<Tee> formatTess(List<Tee> tees) {
		List<Tee> formatted = new ArrayList<Tee>();
		boolean validTeeFlg;
		boolean nineFlg;

		for (Tee tee : tees) {
			validTeeFlg = true;
			nineFlg = false;

			int teeSize = tee.getHoles().size();
			if (teeSize < 9) {
				validTeeFlg = false; // Teeの数が9未満の場合は無効とする
			} else if (teeSize == 9) {
				nineFlg = true;
			} else {
				// 1ホール目が有効かつ 10ホール目が無効の場合、9ホールのコースとする
				Hole holeOne = tee.getHoles().get(0);
				Hole holeTen = tee.getHoles().get(9);
				if (holeOne.isBlank() == false && holeTen.isBlank() == true) {
					nineFlg = true;
				}
			}

			if (validTeeFlg) {
				// 9ホールのコースの場合
				if (nineFlg) {
					ArrayList<Hole> newHoles = new ArrayList<Hole>();
					for (int i = 0; i < 9; i++) {
						newHoles.add(tee.getHoles().get(i));
					}
					tee.setHoles(newHoles);
				}

				// 値が取得できなかった項目について、仮のデータ（men-par, women-par, yard）を設定
				for (Hole hole : tee.getHoles()) {
					if (hole.getPar() == Hole.PAR_UNKNOWN
							&& hole.getWomenPar() == Hole.PAR_UNKNOWN) {
						// parが男性女性両方不明の場合、par4としてデータを作る
						tee.setContainTempData(true);
						hole.setPar(Hole.TEMP_PAR);
						hole.setWomenPar(Hole.TEMP_PAR);
					} else if (hole.getPar() != Hole.PAR_UNKNOWN
							&& hole.getWomenPar() == Hole.PAR_UNKNOWN) {
						// 男性parのみ分かる場合は、女性parに男性parの値を設定する
						hole.setWomenPar(hole.getPar());
					} else if (hole.getPar() == Hole.PAR_UNKNOWN
							&& hole.getWomenPar() != Hole.PAR_UNKNOWN) {
						// 女性parのみ分かる場合は、男性parに女性parの値を設定する
						hole.setPar(hole.getWomenPar());
					}

					if (hole.getHandicap() == Hole.HANDICAP_UNNOWN
							&& hole.getWomenHandicap() == Hole.HANDICAP_UNNOWN) {
						// 両方不明の場合は何もしない。そのまま不明として登録。
					} else if (hole.getHandicap() != Hole.HANDICAP_UNNOWN
							&& hole.getWomenHandicap() == Hole.HANDICAP_UNNOWN) {
						hole.setWomenHandicap(hole.getHandicap());
					} else if (hole.getHandicap() == Hole.HANDICAP_UNNOWN
							&& hole.getWomenHandicap() != Hole.HANDICAP_UNNOWN) {
						hole.setHandicap(hole.getWomenHandicap());
					}

					if (hole.getYard() == Hole.YARD_UNKNOWN) {
						// yardが不明の場合、400としてデータを作る
						tee.setContainTempData(true);
						hole.setYard(Hole.TEMP_YARD);
					}
				}
				formatted.add(tee);
			}
		}

		return formatted;
	}
	
	private HttpResponse execSearch(HashMap<String, String> params, String id) throws Exception  {
				
		Builder uriBuilder = Uri.parse(Constant.URL_TEE_SEARCH.replace("courseID", id)).buildUpon();
		for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			uriBuilder = uriBuilder.appendQueryParameter(key, params.get(key));
		}
        YgoLog.i(TAG, "execSearch url: " + uriBuilder.toString());
		YgoHttpGet httpGet = new YgoHttpGet(uriBuilder.toString());
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
