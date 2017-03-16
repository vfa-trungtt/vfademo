package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.music.FragEditSong;

/**
 * Created by Vitalify on 3/16/17.
 */

public class ActivityMusic extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootFragment(new FragEditSong());
    }
}
