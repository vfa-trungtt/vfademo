package vfa.vfdemo.fragments.map;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.activity.map.*;
import vfa.vfdemo.fragments.DemoEntity;
import vfa.vfdemo.fragments.FragBaseListView;
import vfa.vfdemo.fragments.map.ActivityMap;
import vfa.vfdemo.R;
import vfa.vfdemo.fragments.map.FragGetLastLocation;

public class FragMapDemoList extends FragBaseListView<DemoEntity> {

    @Override
    public int getItemLayoutId() {
        return R.layout.item_demo;
    }
    @Override
    public void onViewLoaded() {
        super.onViewLoaded();
    }

    @Override
    public List<DemoEntity> getDataSource() {
        List<DemoEntity> list = new ArrayList<>();
        DemoEntity demo = new DemoEntity();
        demo.DemoTitle  = "Get Long,Lat";
        demo.DemoDescription    = "A demo request Long,Lat";
        demo.LaucherDemoClass = FragGetLastLocation.class;
        demo.LauchType  = 1;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "My Place Map";
        demo.DemoDescription    = "A demo current location";
        demo.LaucherDemoClass = ActivityMap.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Marker Map";
        demo.DemoDescription    = "A demo marker location";
        demo.LaucherDemoClass = vfa.vfdemo.activity.map.ActivityMapMarker.class;
        list.add(demo);

        return list;
    }

    @Override
    public void onClickItemList(int pos, DemoEntity entity) {
        if(entity != null){
            if(entity.LauchType == 1){
                getVFActivity().pushFragment(entity.LaucherDemoClass);
            }else {
                getVFActivity().pushActivity(entity.LaucherDemoClass);
            }
        }
    }

    @Override
    public void onBindItemList(int pos, DemoEntity entity, View view) {
        super.onBindItemList(pos, entity, view);

        TextView tvTitle    = (TextView) view.findViewById(R.id.tvDemoTitle);
        TextView tvDesc     = (TextView) view.findViewById(R.id.tvDemoDesc);

        tvTitle.setText(entity.DemoTitle);
        tvDesc.setText(entity.DemoDescription);
    }

}
