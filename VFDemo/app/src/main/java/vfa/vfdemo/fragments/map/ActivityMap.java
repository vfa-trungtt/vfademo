package vfa.vfdemo.fragments.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.utils.L;

import vfa.vfdemo.ActivitySlideMenu;
import vfa.vfdemo.R;




public class ActivityMap extends ActivitySlideMenu implements OnMapReadyCallback,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveListener {

    public static final String API_KEY = "AIzaSyBwynpiBBZL4xZZ8G-hADtYNISAV4TZlJU";
    public static final int REQUEST_MAP_ACCESS = 1001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1010;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    GoogleMap mMap;
    boolean mLocationPermissionGranted;
    Location mLastKnownLocation;
    LatLng mDefaultLocation;

    CameraPosition mCameraPosition;
    float DEFAULT_ZOOM = 16;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        requestLocation();
    }

    public void requestLocation(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this ,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                                LogUtils.debug("map connect fails");
                            }
                        } )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
//                        LogUtils.debug("map connected");
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(ActivityMap.this);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
//                        LogUtils.debug("map connect supspended");
                    }
                })
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkMapPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_MAP_ACCESS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_MAP_ACCESS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void getDeviceLocation() {
    /*
     * Before getting the device location, you must check location
     * permission, as described earlier in the tutorial. Then:
     * Get the best and most recent location of the device, which may be
     * null in rare cases when a location is not available.
     */
        ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
//            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    public void moveToLocation(LatLng latLng){
//        LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney),DEFAULT_ZOOM);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LogUtils.debug("map ready...");
        mMap = googleMap;
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);

        updateLocationUI();
        getDeviceLocation();
    }

    @Override
    public void onCameraIdle() {
//        LogUtils.debug("camra idle");
        mDefaultLocation = mMap.getCameraPosition().target;
//        LogUtils.debug("center:"+mDefaultLocation.latitude + "," + mDefaultLocation.longitude);
    }

    @Override
    public void onCameraMove() {
//        LogUtils.debug("camear move");
//        mDefaultLocation = mMap.getCameraPosition().target;
//        LogUtils.debug("center:"+mDefaultLocation.latitude + "," + mDefaultLocation.longitude);
    }

    public LatLng getCenterCamera(){
        return mMap.getCameraPosition().target;
    }

    public GoogleMap getMap(){
        return mMap;
    }
}
