package vfa.vfdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class VFApplicationDemo extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        AppSettings.imageLoader = new VFImageLoader(this);
//        ScreenUtils.getDimensionScreen(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
