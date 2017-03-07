package vfa.vfdemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import vfa.vfdemo.fragments.FragEditImage;

public class ActivityEditImage extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragEditImage fg = new FragEditImage();
        setRootFragment(fg);
    }
}
