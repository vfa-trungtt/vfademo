package vfa.vfdemo;

import android.content.Context;
import android.support.multidex.MultiDex;

import vfa.vfdemo.utils.ScreenUtils;
import vfa.vfdemo.utils.VFImageLoader;
import vfa.vflib.VFApplication;


public class VFApplicationDemo extends VFApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppSettings.imageLoader = new VFImageLoader(this);
        ScreenUtils.getDimensionScreen(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
