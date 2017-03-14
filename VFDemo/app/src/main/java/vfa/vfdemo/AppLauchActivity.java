package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.FragDemoList;


public class AppLauchActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        startActivity(ActivitySlideMenu.class);
//        startActivity(ActivityFileBrowser.class);
//        finish();

        setRootFragment(new FragDemoList());
//        setRootFragment(new FragPager());
//        setRootFragment(new FragPagerVertical());
//        setRootFragment(new FragSliter());
//        setRootFragment(new FragCalendarDemo());
//        setRootFragment(new FragOpenDb());
//        setRootFragment(new FragBrowseTable());
//          setRootFragment(new FragMap());
//        setRootFragment(new FragGallery());
    }
}
