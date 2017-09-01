package vfa.vfdemo.videoeditor;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class TaskCreateImageFile extends AsyncTask {
    private Bitmap bmp;
    private String filePath;

    public interface OnCreateImageFileListener{
        public void onCreateFileDone();
    }

    public TaskCreateImageFile(){

    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}
