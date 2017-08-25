package vfa.vfdemo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.ActivitySlideMenu;
import vfa.vfdemo.R;
import vfa.vflib.fragments.VFFragment;

public class ActivityVideoDemo extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootFragment(new FragVideoRoot());
    }

    public void recordVideo(){

    }

    public void selectVideo(){

    }

    @SuppressLint("ValidFragment")
    class FragVideoRoot extends VFFragment{

        public FragVideoRoot(){
//            R.layout.frag_spliter_vertical
        }

        @Override
        public int onGetRootLayoutId() {
            return R.layout.frag_spliter_vertical;
        }

        @Override
        public void onViewLoaded() {

        }
    }
}
