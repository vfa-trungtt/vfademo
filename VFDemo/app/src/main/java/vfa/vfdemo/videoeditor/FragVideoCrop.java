package vfa.vfdemo.videoeditor;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import vfa.vfdemo.R;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdilib.utils.LogUtils;
import vn.hdisoft.hdimovie.FFMpegHelper;
import vn.hdisoft.hdimovie.MovieHelper;


public class FragVideoCrop extends VFFragment {
    private VideoView videoView;
    public String filePath;
    public Uri movieUri;
    CropView cropView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ffHelper = new FFMpegHelper(getActivity());
    }

    @Override
    public void setUpActionBar() {
        ViewGroup viewActionBar = ViewHelper.getViewGroup(getActivity(),R.layout.actionbar_video_crop);
        viewActionBar.findViewById(R.id.buttonMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewActionBar.findViewById(R.id.buttonCrop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.info("click crop");
                cropMovie();
            }
        });
        getVFActivity().setupActionBarView(viewActionBar);
//        getVFActivity().setHomeActionBar();
    }

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_crop;
    }

    @Override
    public void onViewLoaded() {
        setUpActionBar();
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        cropView  = (CropView) rootView.findViewById(R.id.cropView);

        if(movieUri != null){

            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(filePath);
            String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String rotation =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

            LogUtils.info("meta:"+width+","+height+"....rotation:"+rotation);
            int w = Integer.parseInt(width);
            int h = Integer.parseInt(height);
            int r = Integer.parseInt(rotation);
            if(r == 0){

            }else if (r == 90){
                int temp = w;
                if(w > h){
                    w = h;
                    h = temp;
                }
            }

            //adjust layout with small,video

            cropView.setTempImage(w,h);

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
        filePath = path;
        movieUri = Uri.parse(path);
    }

    FFMpegHelper ffHelper;
    String destPath;
    public void cropMovie(){
        LogUtils.info(""+cropView.getActualCropRect().toString());


        ffHelper.setOnProcessVideo(new FFMpegHelper.OnProccessVideoListener() {
            @Override
            public void onProcessDone(int errorCode, String errorMessage) {
                if(errorCode == 0){
                    videoView.stopPlayback();
                    FragVideoAddWatermark fg  = new FragVideoAddWatermark();
                    fg.setMovieFilePath(destPath);
                    pushFragment(fg);

                }else {

                }
            }
        });
        destPath = Environment.getExternalStorageDirectory().getPath() + "/crop_"+System.currentTimeMillis()+".mp4";
        LogUtils.info("output:"+destPath);
//        ffHelper.cropVideo(filePath,"",destPath);
        ffHelper.cropVideo(filePath,cropView.getActualCropRect(),destPath);


    }
}
