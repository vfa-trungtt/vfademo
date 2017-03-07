package vfa.vfdemo.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiInfoReceiver extends BroadcastReceiver {
	
	
	
    public void onReceive(Context c, Intent intent) {    	
    	
    	String action = intent.getAction();
    	
    	NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
          // Do your work. 

          // e.g. To check the Network Name or other info:
          WifiManager wifiManager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
          WifiInfo wifiInfo = wifiManager.getConnectionInfo();
          String ssid = wifiInfo.getSSID();
          
          Log.i("TrungTT","Connect to :"+ssid);
        }
    	
//        sb = new StringBuilder();
//        wifiList = mainWifi.getScanResults();
//        for (int i = 0; i < wifiList.size(); i++){
//            sb.append(new Integer(i+1).toString() + ".");
//            sb.append((wifiList.get(i)).SSID);
//            sb.append("\n");
     
        
    }

}
