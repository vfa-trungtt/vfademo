package vfa.vfdemo;

import android.content.Context;

import vfa.vfdemo.utils.VFImageLoader;
import vfa.vflib.VFAppSettings;

public class AppSettings extends VFAppSettings {
    public static VFImageLoader imageLoader;

    public static void setCurrentDBPath(Context context,String dbpath){
        setString(context,"db_path",dbpath);
    }
    public static String getCurrentDBPath(Context context){
        return getString(context,"db_path");
    }
}
