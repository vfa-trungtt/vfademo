package vfa.vfdemo;

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
}
