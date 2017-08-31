package vfa.vfdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.fragments.FragSlideMenuListView;
import vfa.vfdemo.fragments.files.FragFileBrowserListView;
import vn.hdisoft.hdilib.activities.ActivitySlideMenu;
import vn.hdisoft.hdilib.fragments.VFFragment;


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
