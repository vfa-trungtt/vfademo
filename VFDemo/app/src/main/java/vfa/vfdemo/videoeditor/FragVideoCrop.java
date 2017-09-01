package vfa.vfdemo.videoeditor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;


import vfa.vfdemo.R;
import vfa.vfdemo.utils.FileUtis;
import vfa.vfdemo.utils.ViewHelper;
import vn.hdisoft.hdilib.utils.LogUtils;
import vn.hdisoft.hdimovie.FFMpegHelper;


public class FragVideoCrop extends BaseMovieFragment {
    private CropView cropView;

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
            metaRetriever.setDataSource(srcPath);
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


//            if(h > )

//            rootView.requestLayout();
            cropView.setTempImage(w,h);
            cropView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(cropView.mViewWidth, cropView.mViewHeight);
                    lp.gravity = Gravity.CENTER;
                    videoView.setLayoutParams(lp);

                    playRepeatMovieWithDelay(500);
                }
            });

//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(cropView.getLayoutParams().width, cropView.getLayoutParams().height);
//            lp.gravity = Gravity.CENTER;
//            videoView.setLayoutParams(lp);
//
//            playRepeatMovieWithDelay(500);
        }
    }

    public void adjustVideoView(){

    }

    public void playRepeatMovie(){
//        movieUri = Uri.parse("/storage/emulated/0/Download/sample.mp4");
        videoView.setVideoURI(movieUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
    }


    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2011;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void cropMovie(){
        LogUtils.info(""+cropView.getActualCropRect().toString());
//        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
//        if (!root.exists()) {
//            root.mkdirs();
//            return;
//        }


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }else {
            LogUtils.info("has permission...");
        }

        ffHelper.setOnProcessVideo(new FFMpegHelper.OnProccessVideoListener() {
            @Override
            public void onProcessDone(int errorCode, String errorMessage) {
                if(errorCode == 0){
                    videoView.stopPlayback();
//                    FragVideoAddWatermark fg  = new FragVideoAddWatermark();
//                    fg.setMovieFilePath(destPath);
//                    pushFragment(fg);
                    if(FileUtis.isValidMovieFile(destPath)){
                        getVFActivity().setPublicString("movie_path",destPath);
                        getVFActivity().startActivity(ActivityPlayMovie.class);
                    }else {
                        Toast.makeText(getContext(),"Error file:"+destPath,Toast.LENGTH_LONG).show();
                    }

                }else {

                }
            }
        });
        destPath = Environment.getExternalStorageDirectory().getPath() + "/crop_"+System.currentTimeMillis()+".mp4";
        LogUtils.info("output:"+destPath);
        ffHelper.cropVideo(srcPath,cropView.getActualCropRect(),destPath);


    }
}
