package vfa.vfdemo.activity.nifty;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nifty.cloud.mb.core.NCMB;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.ActivityBookReader;
import vfa.vfdemo.ActivityDrawing;
import vfa.vfdemo.ActivityFileBrowser;
import vfa.vfdemo.ActivityHDiApp;
import vfa.vfdemo.ActivitySQLiteManager;
import vfa.vfdemo.ActivitySlideMenu;
import vfa.vfdemo.fragments.DemoEntity;
import vfa.vfdemo.fragments.VFFragDemoList;
import vfa.vfdemo.fragments.map.FragMapDemoList;
import vfa.vfdemo.fragments.nifty.FragListPublicSchool;
import vfa.vfdemo.fragments.nifty.FragListSchoolByDistance;


public class ActivityNiftyDemo extends ActivitySlideMenu{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpCloud();
        VFFragDemoList fg = new VFFragDemoList();
        fg.setDemoListData(createDemoList());
        setRootFragment(fg);
    }

    public List<DemoEntity> createDemoList(){
        List<DemoEntity> list = new ArrayList<>();
        DemoEntity demo = new DemoEntity();
        demo.DemoTitle  = "Get school by distance";
        demo.DemoDescription    = "A demo search school by distance";
        demo.LaucherDemoClass   = FragListSchoolByDistance.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle          = "Map of School";
        demo.DemoDescription    = "A demo display school on map view.";
        demo.LaucherDemoClass   = ActivityNiftyMap.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle          = "List of public School";
        demo.DemoDescription    = "List all public school on cloud.";
        demo.LaucherDemoClass   = FragListPublicSchool.class;
        demo.LauchType          = 1;
        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "File Browser";
//        demo.DemoDescription    = "A demo about file browser with basic function.";
//        demo.LaucherDemoClass = ActivityFileBrowser.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Book Reader";
//        demo.DemoDescription    = "A demo about book reader base on webview.";
//        demo.LaucherDemoClass = ActivityBookReader.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Map Demo";
//        demo.DemoDescription    = "A demo about map,location.";
//        demo.LaucherDemoClass   = FragMapDemoList.class;
//        demo.LauchType          = 1;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Draw Demo";
//        demo.DemoDescription    = "A demo about draw on canvas";
//        demo.LaucherDemoClass = ActivityDrawing.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Network Demo";
//        demo.DemoDescription    = "A demo about network with socket connection";
//        demo.LaucherDemoClass = ActivityDrawing.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "App Demo";
//        demo.DemoDescription    = "A demo for HDiApp use FireBase";
//        demo.LaucherDemoClass = ActivityHDiApp.class;
//        list.add(demo);

        return list;
    }

    private void setUpCloud(){
        String MBASSKEY                 = "88919f26cf707eb2b372141adf5ae728aba1a9e711245428357d9edb6fc4b1db";
        String MBASSKEY_CLIENT          = "63c21db86723e6f89d118b18ac34f804927e092b515ccb06f85a79f70b69cd10";
        NCMB.initialize(this, MBASSKEY,MBASSKEY_CLIENT);
    }
}
