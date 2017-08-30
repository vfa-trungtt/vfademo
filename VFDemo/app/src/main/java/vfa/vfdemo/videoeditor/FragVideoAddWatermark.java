package vfa.vfdemo.videoeditor;

import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import vfa.vfdemo.R;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragVideoAddWatermark extends VFFragment {
    private VideoView videoView;
    public String filePath;
    public Uri movieUri;

    @Override
    public void setUpActionBar() {
//        super.setUpActionBar();
        getVFActivity().setHomeActionBar();
    }

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_watermark;
    }

    @Override
    public void onViewLoaded() {
        if(movieUri != null){
            videoView = (VideoView) rootView.findViewById(R.id.videoView);
            videoView.setVideoURI(movieUri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoView.start();
        }

    }

    public void setMovieFilePath(String path){
        filePath = path;
        movieUri = Uri.parse(path);
    }

    public void drawWatermark(){

    }
}
