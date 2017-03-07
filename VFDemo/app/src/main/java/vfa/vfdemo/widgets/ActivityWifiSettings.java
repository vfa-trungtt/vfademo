package vfa.vfdemo.widgets;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import vfa.vfdemo.R;
import vfa.vflib.utils.LogUtils;

public class ActivityWifiSettings extends FragmentActivity{

	String wifiName = "HD";
	String wifiPass = "hdisoft1508";
	WifiManager wifi;    
	BroadcastReceiver _receiver;
	List<ScanResult> results;
	
	private String android_id = "";
	TextView tvStatus;
//	ScanResult
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_wifi_settings);
		tvStatus = (TextView)findViewById(R.id.tvStatus);
		
		
		String currentSSID = NetworkTools.wifiHotSpotName(this);
		if(!currentSSID.equalsIgnoreCase(wifiName)){
			tvStatus.setText("Scan target:"+wifiName);
			scanWifi();
		}else{
			tvStatus.setText("Connected!");
		}
		
		android_id = Secure.getString(getContentResolver(),Secure.ANDROID_ID); 		
		TextView tv = (TextView)findViewById(R.id.tvAndroidID);
		tv.setText("AndroidID:" +android_id);
		
	}
	
	public void scanWifi(){
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }   
        
        _receiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				LogUtils.info("onReceive:"+intent.getAction());
				String action = intent.getAction();

				if(action.equalsIgnoreCase("android.net.wifi.SCAN_RESULTS")){
					results = wifi.getScanResults();
					for(ScanResult result:results){
						LogUtils.debug(""+result.SSID);
						if(result.SSID.equalsIgnoreCase(wifiName)){
							LogUtils.info("Found target:"+wifiName);
							connectWifi(wifiName, wifiPass);
							break;
						}
					}
				}
					
				
			}
		};
        registerReceiver(_receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); 
        
        wifi.startScan();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(_receiver != null){
			unregisterReceiver(_receiver);
		}
		
	}
	
	public void connectWifi(String ssid,String pass){
		LogUtils.debug("connect to :"+ssid);
		String networkSSID = ssid;
		String networkPass = pass;

		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
		
		
		conf.wepKeys[0] = "\"" + networkPass + "\""; 
		conf.wepTxKeyIndex = 0;
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40); 
		
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE); 
		wifiManager.addNetwork(conf);
		
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for( WifiConfiguration i : list ) {
		    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
		         wifiManager.disconnect();
		         wifiManager.enableNetwork(i.networkId, true);
		         wifiManager.reconnect();               

		         break;
		    }           
		 }
		
		tvStatus.setText("Connected!");
	}
}
