package vfa.vfdemo.fragments.map;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import vfa.vfdemo.R;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

public class FragGetLastLocation extends VFFragment {
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    TextView tvLong;
    TextView tvLat;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_map_getlocation;
    }

    @Override
    public void onViewLoaded() {
        MapHelper.getCurrentLocation(getContext());

        tvLat   = (TextView) rootView.findViewById(R.id.tvLatValue);
        tvLong  = (TextView) rootView.findViewById(R.id.tvLongValue);

        requestLocation();
        rootView.findViewById(R.id.btReqestLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void requestLocation(){
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() ,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                LogUtils.debug("map connect fails");
                            }
                        } )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LogUtils.debug("map connected");
                        getDeviceLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        LogUtils.debug("map connect supspended");
                    }
                })
                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted = true;
    private CameraPosition mCameraPosition;

    private void getDeviceLocation() {
    /*
     * Before getting the device location, you must check location
     * permission, as described earlier in the tutorial. Then:
     * Get the best and most recent location of the device, which may be
     * null in rare cases when a location is not available.
     */
        ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            LogUtils.debug("==Long:"+mLastKnownLocation.getLongitude());
            LogUtils.debug("==Long:"+mLastKnownLocation.getLatitude());

            tvLong.setText("Long:"+mLastKnownLocation.getLongitude());
            tvLat.setText("Lat:"+mLastKnownLocation.getLatitude());


        }

        // Set the map's camera position to the current location of the device.
//        if (mCameraPosition != null) {
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
//        } else if (mLastKnownLocation != null) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(mLastKnownLocation.getLatitude(),
//                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//        } else {
////            Log.d(TAG, "Current location is null. Using defaults.");
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        }
    }

}
