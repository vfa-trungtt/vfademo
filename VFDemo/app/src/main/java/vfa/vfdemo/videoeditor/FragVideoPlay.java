package vfa.vfdemo.videoeditor;

import android.widget.VideoView;

import vfa.vfdemo.R;


public class FragVideoPlay extends BaseMovieFragment {

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_play;
    }

    @Override
    public void onViewLoaded() {
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        playRepeatMovieWithDelay(500);
    }
}
