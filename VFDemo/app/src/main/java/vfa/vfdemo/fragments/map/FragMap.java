package vfa.vfdemo.fragments.map;

import com.google.android.gms.maps.MapView;

import vfa.vfdemo.R;
import vn.hdisoft.hdilib.fragments.VFFragment;


/**
 * Created by Vitalify on 3/7/17.
 */

public class FragMap extends VFFragment {

    private MapView mapView;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_map;
    }

    @Override
    public void onViewLoaded() {
        mapView = (MapView) rootView.findViewById(R.id.mapView);

    }
}
