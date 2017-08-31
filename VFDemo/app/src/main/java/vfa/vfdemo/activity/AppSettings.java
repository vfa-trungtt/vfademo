package vfa.vfdemo.activity;

import android.content.Context;

import vfa.vfdemo.utils.VFImageLoader;
import vn.hdisoft.hdilib.PrefAppSettings;


public class AppSettings extends PrefAppSettings{
    public static VFImageLoader imageLoader;

    public static void setCurrentDBPath(Context context,String dbpath){
        setString(context,"db_path",dbpath);
    }
    public static String getCurrentDBPath(Context context){
        return getString(context,"db_path");
    }
}
