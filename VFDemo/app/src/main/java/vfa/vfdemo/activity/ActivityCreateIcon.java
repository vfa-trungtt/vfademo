package vfa.vfdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.iconapp.FragIconList;

public class ActivityCreateIcon extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootFragment(new FragIconList());

        setActionBarText("Icon - Create");
    }
}
