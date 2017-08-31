package vfa.vfdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import vfa.vfdemo.AppSettings;
import vfa.vfdemo.utils.ScreenUtils;
import vfa.vfdemo.utils.VFImageLoader;


public class VFApplicationDemo extends Application {

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
