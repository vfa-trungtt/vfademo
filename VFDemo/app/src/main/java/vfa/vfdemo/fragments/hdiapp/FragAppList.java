package vfa.vfdemo.fragments.hdiapp;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.fragments.FragBaseListView;


/**
 * Created by Vitalify on 3/10/17.
 */

public class FragAppList extends FragBaseListView<AppEntity> {
//    @Override
//    public int onGetRootLayoutId() {
//        return R.layout.frag_app_list;
//    }

    @Override
    public void onViewLoaded() {
//        setL
    }

    @Override
    public List<AppEntity> getDataSource() {
        List<AppEntity> list = new ArrayList<>();
        AppEntity app = new AppEntity();
        list.add(app);

        return list;
    }
}
