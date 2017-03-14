package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.hdiapp.FragAppList;
import vfa.vfdemo.fragments.hdiapp.FragLogin;


public class ActivityHDiApp extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRootFragment(new FragAppList());
        setRootFragment(new FragLogin());
    }
}
