package vfa.vfdemo.videoeditor;

import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import vfa.vfdemo.R;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragVideoCrop extends VFFragment {
    private VideoView videoView;
    public String filePath;
    public Uri movieUri;
    FixedCropImageView cropImageView;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_crop;
    }

    @Override
    public void onViewLoaded() {
//        videoView = (VideoView) rootView.findViewById(R.id.videoView);
//        videoView.set

        if(movieUri != null){
            playRepeatMovie();
        }
    }

    public void playRepeatMovie(){
        videoView.setVideoURI(movieUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
    }

    public void setMovieFilePath(String path){
        movieUri = Uri.parse(path);
    }

    public void cropMovie(){

    }
}
