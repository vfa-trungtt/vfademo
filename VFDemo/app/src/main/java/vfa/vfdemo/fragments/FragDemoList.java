package vfa.vfdemo.fragments;

import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.ActivityBookReader;
import vfa.vfdemo.ActivityDrawing;
import vfa.vfdemo.ActivityFileBrowser;
import vfa.vfdemo.ActivityMap;
import vfa.vfdemo.ActivitySQLiteManager;
import vfa.vfdemo.R;


public class FragDemoList extends FragBaseListView<DemoEntity> {

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
        demo.DemoTitle  = "SQLite Manager";
        demo.DemoDescription    = "A demo about Sqlite database,insert,delete,update.";
        demo.LaucherDemoClass = ActivitySQLiteManager.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "File Browser";
        demo.DemoDescription    = "A demo about file browser with basic function.";
        demo.LaucherDemoClass = ActivityFileBrowser.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Book Reader";
        demo.DemoDescription    = "A demo about book reader base on webview.";
        demo.LaucherDemoClass = ActivityBookReader.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Map Demo";
        demo.DemoDescription    = "A demo about map,location.";
        demo.LaucherDemoClass   = FragMapDemoList.class;
        demo.LauchType          = 1;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Draw Demo";
        demo.DemoDescription    = "A demo about draw on canvas";
        demo.LaucherDemoClass = ActivityDrawing.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Network Demo";
        demo.DemoDescription    = "A demo about network with socket connection";
        demo.LaucherDemoClass = ActivityDrawing.class;
        list.add(demo);
        return list;
    }


    @Override
    public void onClickItemList(int pos, DemoEntity entity) {
        if(entity != null){
            if(entity.LauchType == 0){
                getVFActivity().pushActivity(entity.LaucherDemoClass);
            }else {
                getVFActivity().pushFragment(entity.LaucherDemoClass);
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
