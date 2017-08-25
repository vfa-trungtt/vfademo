package vfa.vfdemo.activity.nifty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import vfa.vfdemo.R;
import vfa.vfdemo.fragments.map.ActivityMap;
import vfa.vfdemo.fragments.nifty.FragListSchoolByDistance;
import vfa.vfdemo.fragments.nifty.FragNiftySlideMenu;
import vfa.vfdemo.utils.ViewHelper;



public class ActivityNiftyMap extends ActivityMap {
    View mapPointer;
    TextView tvLatLong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpActionBarMain();
        setSlideMenuFagment(new FragNiftySlideMenu());

        setActionBarText("Nifty-Map Demo");
//        mapPointer = ViewHelper.getView(this,R.layout.view_map_pointer);
//        tvLatLong = (TextView) mapPointer.findViewById(R.id.tvLatLong);
//
//        addViewIntoFragmentContainer(mapPointer);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        LatLng latLng = new LatLng(34.674174,135.517034);
        moveToLocation(latLng);
    }

    public void setUpActionBarMain(){
        View view = ViewHelper.getView(this, R.layout.actionbar_nifty_map);
        view.findViewById(R.id.btSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragment(new FragListSchoolByDistance());
            }
        });
        setActionBarRightContent(view);
    }

    @Override
    public void onSlideMenuSelected(int menuItemIndex) {


        switch (menuItemIndex){
            case 0:

                break;
            case 1:
                break;
        }
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();
        getMap().clear();
        getMap().addMarker(new MarkerOptions().position(getCenterCamera())
                .title(""+getCenterCamera().latitude + "," + getCenterCamera().longitude));
//        tvLatLong.setText(""+getCenterCamera().latitude + "," + getCenterCamera().longitude);
    }
}
