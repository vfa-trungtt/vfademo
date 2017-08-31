package vfa.vfdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.fragments.DemoEntity;
import vfa.vfdemo.fragments.VFFragDemoList;
import vfa.vfdemo.fragments.drawing.Frag3DDraw;
import vfa.vfdemo.fragments.drawing.FragBasicDraw;
import vfa.vfdemo.fragments.drawing.FragColorPallete;
import vfa.vfdemo.fragments.drawing.FragGradiantColor;

/**
 * Created by Vitalify on 3/8/17.
 */

public class ActivityDrawing extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VFFragDemoList fg = new VFFragDemoList();
        fg.setDemoListData(getDemoList());
        setRootFragment(fg);

//        setRootFragment(new FragBasicDraw());
    }

    public List<DemoEntity> getDemoList() {
        List<DemoEntity> list = new ArrayList<>();
        DemoEntity demo = new DemoEntity();
        demo.DemoTitle  = "Draw object";
        demo.DemoDescription    = "A demo about draw object";
        demo.LaucherDemoClass = FragBasicDraw.class;
        demo.LauchType          = 1;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Color";
        demo.DemoDescription    = "A demo about color pallete.";
        demo.LaucherDemoClass = FragColorPallete.class;
        demo.LauchType          = 1;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Gradient color";
        demo.DemoDescription    = "A demo about gradient color.";
        demo.LaucherDemoClass = FragGradiantColor.class;
        demo.LauchType          = 1;
        list.add(demo);

        demo = new DemoEntity();
        demo.DemoTitle  = "Android 3D graphic";
        demo.DemoDescription    = "A demo about 3Dgraphics";
        demo.LaucherDemoClass = Frag3DDraw.class;
        demo.LauchType          = 1;
        list.add(demo);
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
