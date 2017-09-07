package vfa.vfdemo.videoeditor;

import android.content.Context;
import android.os.Environment;

public class MovieSettings {
    public static String OUT_PUT_FOLDER = "movie_output";
    public static String TMP_FOLDER     = "tmp";
    public static String CACHE_FOLDER   = "cache";

    public static String getOutPutFolderPath(Context context){
        return OUT_PUT_FOLDER;
    }

    public static String getOutPutMovieDefaultPath(Context context){
        String destPath = Environment.getExternalStorageDirectory().getPath() + "/ouput_"+System.currentTimeMillis()+".mp4";
        return OUT_PUT_FOLDER;
    }
    public static String getOutPutMovieDefaultPath(Context context,String prefix){
        String destPath = Environment.getExternalStorageDirectory().getPath() + prefix + System.currentTimeMillis()+".mp4";
        return destPath;
    }

    static void validFolder(Context context){

    }

}
