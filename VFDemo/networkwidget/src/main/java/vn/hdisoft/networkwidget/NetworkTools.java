package vn.hdisoft.networkwidget;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

public class NetworkTools {

	public static String getIPaddress(){
		boolean useIPv4 = true;
		try {
			
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        	
        } // for now eat exceptions
        return "";
	}
	
	public static String[] getWifiList(){
		return null;
	}
	
	public static String wifiIpAddress(Context context) {
		
	    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

	    // Convert little-endian to big-endianif needed
	    if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
	        ipAddress = Integer.reverseBytes(ipAddress);
	    }

	    byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

	    String ipAddressString;
	    try {
	        ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
	    } catch (UnknownHostException ex) {
	        ipAddressString = null;
	    }

	    return ipAddressString;
	}
	
	public static String wifiHotSpotName(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("TrungTT", wifiInfo.toString());
        Log.d("TrungTT",wifiInfo.getSSID());
        
        return wifiInfo.getSSID();
	}

}
