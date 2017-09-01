package vfa.vfdemo.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;


public class FileUtis {
    static String currentDir="/";
    public interface OnCreateImageFileListener{
        public void onCreateFileDone(String filePath);
    }

    public void setCurrentDir(String _currentDir){
        currentDir = _currentDir;
    }
    public static void createFileFromBitmap(final Bitmap bmp,final String filePath,final OnCreateImageFileListener listener){
        new AsyncTask() {
            String filePath;
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    File file = new File (filePath);
                    FileOutputStream out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    bmp.recycle();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener != null) listener.onCreateFileDone(filePath);
            }
        }.execute();
    }

    public static boolean isValidMovieFile(String filePath){
        File f = new File(filePath);
        if(!f.exists()){
            return false;
        }
        return true;
    }
}
