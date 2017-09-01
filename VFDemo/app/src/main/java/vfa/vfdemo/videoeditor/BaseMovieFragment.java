package vfa.vfdemo.videoeditor;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.VideoView;

import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdimovie.FFMpegHelper;

public class BaseMovieFragment extends VFFragment {
    public FFMpegHelper ffHelper;
    public VideoView videoView;
    public String srcPath;
    public String destPath;
    public Uri movieUri;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ffHelper = new FFMpegHelper(getActivity());
    }
    @Override
    public void onViewLoaded() {

    }

    public void setMovieFilePath(String path){
        srcPath = path;
        movieUri = Uri.parse(path);
    }
    public void playRepeatMovie(){
        if(movieUri != null){
            videoView.setVideoURI(movieUri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });

            videoView.start();
            videoView.setVisibility(View.VISIBLE);
        }
    }

    public void playRepeatMovieWithDelay(int miliSecond){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playRepeatMovie();
            }
        },miliSecond);
    }
    //handle loading dialog...
    ProgressDialog progressDialog;
    public void showLoading(){
        if(progressDialog == null){
            progressDialog = ProgressDialog.show(getContext(),"","Process...",true);
        }
    }

    public void hideLoading(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
