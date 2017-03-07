package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.FragCalendarDemo;
import vfa.vfdemo.fragments.FragPager;
import vfa.vfdemo.fragments.FragSliter;
import vfa.vfdemo.fragments.images.FragGallery;
import vfa.vfdemo.fragments.map.FragMap;
import vfa.vfdemo.fragments.sql.FragBrowseTable;
import vfa.vfdemo.fragments.sql.FragOpenDb;
import vfa.vfdemo.utils.ScreenUtils;
import vfa.vfdemo.utils.VFImageLoader;


public class AppLauchActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.getDimensionScreen(this);

        AppSettings.imageLoader = new VFImageLoader(this);
//        setRootFragment(new FragPager());
//        setRootFragment(new FragSliter());
//        setRootFragment(new FragCalendarDemo());
//        setRootFragment(new FragOpenDb());
//        setRootFragment(new FragBrowseTable());
//          setRootFragment(new FragMap());
        setRootFragment(new FragGallery());
    }
}
