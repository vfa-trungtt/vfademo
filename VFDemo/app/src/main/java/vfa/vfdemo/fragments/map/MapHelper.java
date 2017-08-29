package vfa.vfdemo.fragments.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



public class MapHelper {

    public static void getCurrentLocation(Context context){
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
//        LogUtils.debug("permisson check:"+permissionCheck);
        if(permissionCheck  == PackageManager.PERMISSION_GRANTED){

        }else {
            //request permission
            return;
        }


        LocationManager lm  = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location   = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            double longitude    = location.getLongitude();
            double latitude     = location.getLatitude();
//            LogUtils.debug("long:"+longitude+",lat:"+latitude);
        }else {
//            LogUtils.error("NULL location");
        }

    }

    public static void requestPermission(Context context){

    }
}
