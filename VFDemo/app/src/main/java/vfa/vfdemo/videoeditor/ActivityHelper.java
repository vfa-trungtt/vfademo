package vfa.vfdemo.videoeditor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class ActivityHelper {

    public static void startActivity(Activity activity,Class<?> target){
        Intent intent = new Intent(activity,target);
        activity.startActivity(intent);
    }

    public static void addFragment(FragmentActivity activity,int containerId, Fragment fg){

    }
}
