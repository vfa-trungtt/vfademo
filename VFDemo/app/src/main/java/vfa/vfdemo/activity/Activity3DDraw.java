package vfa.vfdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.BaseActivity;
import vfa.vfdemo.fragments.DemoEntity;
import vfa.vfdemo.fragments.VFFragDemoList;
import vfa.vfdemo.fragments.drawing.opengles.FragTriAngle;



public class Activity3DDraw extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VFFragDemoList fg = new VFFragDemoList();
        fg.setDemoListData(getDemoList());
        setRootFragment(fg);

//        getSupportActionBar().hide();
//        setRootFragment(new Frag3DDraw());
    }

    public List<DemoEntity> getDemoList() {
        List<DemoEntity> list = new ArrayList<>();
        DemoEntity demo = new DemoEntity();
        demo.DemoTitle  = "Draw TriAngle";
        demo.DemoDescription    = "A demo about draw 3D shape in openGLES 2.0";
        demo.LauchType          = 1;
        demo.LaucherDemoClass = FragTriAngle.class;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Draw Image";
        demo.DemoDescription    = "A demo about draw image in openGLES 2.0.";
        demo.LaucherDemoClass = ActivitySQLiteManager.class;
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
//        demo.DemoTitle          = "Android UI Design";
//        demo.DemoDescription    = "A demo about UI component.";
//        demo.LaucherDemoClass   = ActivityUIDesign.class;
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
