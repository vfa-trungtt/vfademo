package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.activity.nifty.ActivityNiftyDemo;
import vfa.vfdemo.fragments.DemoEntity;
import vfa.vfdemo.fragments.VFFragDemoList;
import vfa.vfdemo.fragments.map.FragMapDemoList;
import vfa.vfdemo.fragments.uidesign.FragButton;

/**
 * Created by Vitalify on 3/17/17.
 */

public class ActivityUIDesign extends ActivitySlideMenu {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VFFragDemoList fg = new VFFragDemoList();
        fg.setDemoListData(getDemoList());
        setRootFragment(fg);
    }

    public List<DemoEntity> getDemoList() {
        List<DemoEntity> list = new ArrayList<>();
        DemoEntity demo = new DemoEntity();
        demo.DemoTitle          = "Android UI design";
        demo.DemoDescription    = "A demo about Button style";
        demo.LaucherDemoClass = FragButton.class;
        demo.LauchType          = 1;
        list.add(demo);

//        demo = new DemoEntity();
//        demo.DemoTitle  = "SQLite Manager";
//        demo.DemoDescription    = "A demo about Sqlite database,insert,delete,update.";
//        demo.LaucherDemoClass = ActivitySQLiteManager.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "File Browser";
//        demo.DemoDescription    = "A demo about file browser with basic function.";
//        demo.LaucherDemoClass = ActivityFileBrowser.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Android UI Design";
//        demo.DemoDescription    = "A demo about UI component.";
//        demo.LaucherDemoClass = ActivityFileBrowser.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Android Icon";
//        demo.DemoDescription    = "A demo create icon for app";
//        demo.LaucherDemoClass = ActivityCreateIcon.class;
//        list.add(demo);
//
//        demo = new DemoEntity();
//        demo.DemoTitle  = "Musics";
//        demo.DemoDescription    = "A demo about some feature in music.";
//        demo.LaucherDemoClass = ActivityMusic.class;
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
}