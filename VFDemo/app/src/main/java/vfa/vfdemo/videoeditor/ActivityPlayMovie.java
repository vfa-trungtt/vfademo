package vfa.vfdemo.videoeditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import vfa.vfdemo.ActivitySlideMenu;
import vn.hdisoft.hdilib.activities.VFActivity;

/**
 * Created by trungtt on 8/31/17.
 */

public class ActivityPlayMovie extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragVideoPlay fg = new FragVideoPlay();
        fg.setMovieFilePath(FragVideoAddWatermark.destPath);
        setRootFragment(fg);
    }
}
