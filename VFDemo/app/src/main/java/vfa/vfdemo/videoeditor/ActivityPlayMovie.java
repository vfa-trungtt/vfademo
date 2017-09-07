package vfa.vfdemo.videoeditor;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.ActivitySlideMenu;

public class ActivityPlayMovie extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragVideoPlay fg = new FragVideoPlay();

        fg.setMovieFilePath(getPublicString("movie_path"));
        setRootFragment(fg);
    }
}
