package vfa.vfdemo.activity;

import android.net.Uri;
import android.widget.VideoView;

import vfa.vfdemo.R;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;

/**
 * Created by trungtt on 8/28/17.
 */

public class FragVideoPlay extends VFFragment {
    private VideoView videoView;
    public String filePath;
    public Uri movieUri;
    FixedCropImageView cropImageView;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_play;
    }

    @Override
    public void onViewLoaded() {
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        videoView.setVideoURI(movieUri);
        videoView.start();

        cropImageView = (FixedCropImageView)rootView.findViewById(R.id.cropImageScoreCardView);
        cropImageView.setMinFrameSizeInDp(100);
    }

    public void cropMovie(){

    }

    public void drawWatermark(){

    }
}
