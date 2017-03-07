package vfa.vfdemo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHelper {

    public static View getView(Context context, int layoutId){
        LayoutInflater inflator = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflator.inflate(layoutId, null);
    }

    public static ViewGroup getViewGroup(Context context, int layoutId){
        LayoutInflater inflator = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return (ViewGroup) inflator.inflate(layoutId, null);
    }
}
