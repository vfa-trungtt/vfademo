package vfa.vfdemo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import vfa.vfdemo.ActivitySlideMenu;
import vfa.vfdemo.R;
import vfa.vfdemo.dialogs.DialogActionSheet;
import vfa.vfdemo.dialogs.DialogMovieActionSheet;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class ActivityVideoDemo extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootFragment(new FragVideoRoot());
    }

    public void recordMovie(){

    }

    int REQUEST_TAKE_GALLERY_VIDEO = 1011;

    public void chooseMovie(){
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_GALLERY_VIDEO){
            if(resultCode == RESULT_OK){

            }
            return;
        }
    }
}
