package vfa.vfdemo.utils;

import android.os.AsyncTask;

/**
 * Created by trungtt on 8/3/17.
 */

public abstract class VFTask extends AsyncTask<Void,Void,Void> {

    public static final int TASK_SUCCESSFUL = 0;
    public static final int TASK_FAILS      = 1;

    public interface VFTaskListener{
        public void onVFTaskFinish(int status);
    }

//    public int
    private VFTaskListener _listener;

    public void doTask(VFTaskListener listener){
        _listener = listener;
    }
}
