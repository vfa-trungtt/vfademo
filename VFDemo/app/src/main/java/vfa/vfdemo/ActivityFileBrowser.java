package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.fragments.FragSlideMenuListView;
import vfa.vfdemo.fragments.files.FragFileBrowserListView;
import vfa.vfdemo.fragments.files.FragSearchFile;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;


public class ActivityFileBrowser extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSlideMenuFagment(new FileMenuSlide());
        setRootFragment(new FragFileBrowserListView());
    }

    @Override
    public void onBackPressed() {
        if(getRootFragment() instanceof VFFragment){
            VFFragment fg = (VFFragment) getRootFragment();
            if(fg.onBackPress()){
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }

    }

    public void setupActionbar(){
        setActionBarText("File Browser");

        setActionBarRightContent(R.layout.actionbar_filebrowser);

    }

    public static class FileMenuSlide extends FragSlideMenuListView {

        @Override
        public List<String> getDataSource() {
            List<String> menuSlide = new ArrayList<>();

            menuSlide.add("Images");
            menuSlide.add("Musics");
            menuSlide.add("Video");
            menuSlide.add("Documents");
            menuSlide.add("Zip files");
            menuSlide.add("Share files");
            menuSlide.add("Send files");

            return menuSlide;
        }
    }
}
