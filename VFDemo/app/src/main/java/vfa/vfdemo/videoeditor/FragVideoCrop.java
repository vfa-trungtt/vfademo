package vfa.vfdemo.videoeditor;

import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import static android.media.MediaMetadataRetriever.*;


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
                Rect rect = new Rect();
                cropView.getActualCropRect().round(rect);
                cropMovie(rect);
            }
        });
        getVFActivity().setupActionBarView(viewActionBar);
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

            validRotateVideo();
            cropView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(cropView.mViewWidth, cropView.mViewHeight);
                    lp.gravity = Gravity.CENTER;
                    videoView.setLayoutParams(lp);

                    playRepeatMovieWithDelay(500);
                }
            });
        }
    }

    public void validRotateVideo(){
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(srcPath);
        String height = metaRetriever.extractMetadata(METADATA_KEY_VIDEO_HEIGHT);
        String width = metaRetriever.extractMetadata(METADATA_KEY_VIDEO_WIDTH);
        String rotation = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            rotation = metaRetriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
        }

        LogUtils.info("meta:"+width+","+height+"....rotation:"+rotation);
        int w = Integer.parseInt(width);
        int h = Integer.parseInt(height);
        int r = Integer.parseInt(rotation);
        if (r == 90){
            int temp = w;
            if(w > h){
                w = h;
                h = temp;
            }
        }
        cropView.setTempImage(w,h);
    }

//    long startTime = 0;
//    public void cropMovie(){
//        LogUtils.info(""+cropView.getActualCropRect().toString());
//        ffHelper.setOnProcessVideo(new FFMpegHelper.OnProccessVideoListener() {
//            @Override
//            public void onProcessDone(int errorCode, String errorMessage) {
//                hideLoading();
//                long current = System.currentTimeMillis();
//                LogUtils.debug("Time spent:"+(current-startTime)/1000+" seconds.");
//                if(errorCode == 0){
//                    videoView.stopPlayback();
//                    if(FileUtis.isValidMovieFile(destPath)){
//                        getVFActivity().setPublicString("movie_path",destPath);
//                        getVFActivity().startActivity(ActivityPlayMovie.class);
//                    }else {
//                        Toast.makeText(getContext(),"Error file:"+destPath,Toast.LENGTH_LONG).show();
//                    }
//
//                }else {
//
//                }
//            }
//        });
//
//        destPath = Environment.getExternalStorageDirectory().getPath() + "/crop_"+System.currentTimeMillis()+".mp4";
//        LogUtils.info("output:"+destPath);
//        ffHelper.cropVideo(srcPath,cropView.getActualCropRect(),destPath);
//        startTime = System.currentTimeMillis();
//
//        showLoading();
//    }



    @Override
    public void onProcessVideoFinish() {
        super.onProcessVideoFinish();
        videoView.stopPlayback();
        if(FileUtis.isValidMovieFile(destPath)){
            getVFActivity().setPublicString("movie_path",destPath);
            getVFActivity().startActivity(ActivityPlayMovie.class);
        }else {
            Toast.makeText(getContext(),"Error file not found:"+destPath,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProcessVideoFail() {
        super.onProcessVideoFail();
    }
}
