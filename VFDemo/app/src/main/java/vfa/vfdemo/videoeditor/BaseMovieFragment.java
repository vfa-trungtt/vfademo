package vfa.vfdemo.videoeditor;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdimovie.FFMpegHelper;

public class BaseMovieFragment extends VFFragment {
    private FFMpegHelper ffHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ffHelper = new FFMpegHelper(getActivity());
    }
    @Override
    public void onViewLoaded() {

    }
}
