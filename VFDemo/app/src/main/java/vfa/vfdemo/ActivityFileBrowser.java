package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.files.FragFileBrowser;
import vfa.vfdemo.fragments.files.FragFileBrowserListView;


public class ActivityFileBrowser extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRootFragment(new FragFileBrowser());
        setRootFragment(new FragFileBrowserListView());
    }
}
