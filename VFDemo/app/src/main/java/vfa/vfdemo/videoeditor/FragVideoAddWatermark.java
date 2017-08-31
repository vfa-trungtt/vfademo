package vfa.vfdemo.videoeditor;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;

import vfa.vfdemo.R;
import vfa.vfdemo.ui.HorizonBarView;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdilib.utils.LogUtils;
import vn.hdisoft.hdimovie.FFMpegHelper;


public class FragVideoAddWatermark extends VFFragment {
    private VideoView videoView;
    public String filePath;
    public Uri movieUri;

    HorizonBarView waterMarkBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ffHelper = new FFMpegHelper(getActivity());
    }
    @Override
    public void setUpActionBar() {

        ViewGroup viewActionBar = ViewHelper.getViewGroup(getActivity(),R.layout.actionbar_video_watermark);
        viewActionBar.findViewById(R.id.buttonMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewActionBar.findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.info("click save");
                drawWatermark();
            }
        });
        getVFActivity().setupActionBarView(viewActionBar);
    }

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_watermark;
    }

    @Override
    public void onViewLoaded() {
        if(movieUri != null){
//            movieUri = Uri.parse("/data/user/0/vfa.vfdemo/files/crop_1504169909795.mp4");
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

        int[] barItem = {R.drawable.takanotsume_thumb,R.drawable.fashion_thumb};

        waterMarkBar = (HorizonBarView) rootView.findViewById(R.id.waterMarkBar);
        waterMarkBar.setItemLayoutResId(R.layout.item_bar);
        waterMarkBar.setItemByImageResArray(barItem,R.id.imageView);
        waterMarkBar.setOnItemClick(new HorizonBarView.OnHorizonBarIemClick() {
            @Override
            public void onClick(int itemIndex) {
                addWatermark(itemIndex);
            }
        });
        addWatermark(0);
    }

    public void setMovieFilePath(String path){
        filePath = path;
        movieUri = Uri.parse(path);
    }

    public void createWatermarkImage(){
        View v = rootView.findViewById(R.id.viewWatermark);
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        try {
//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(root + "/saved_images");
//            myDir.mkdirs();
//            Random generator = new Random();
//            int n = 10000;
//            n = generator.nextInt(n);
            String fname = "watermark-"+ System.currentTimeMillis() +".png";
            File file = new File (Environment.getExternalStorageDirectory().getPath(), fname);

            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            watermarkPath = file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.info("");
    }

    public void drawWatermark(){
        createWatermarkImage();

//        destPath = getActivity().getCacheDir().getPath() + "/crop_"+System.currentTimeMillis()+".mp4";
        destPath = Environment.getExternalStorageDirectory().getPath() + "/watermark_"+System.currentTimeMillis()+".mp4";
        LogUtils.info("output:"+destPath);
        ffHelper.setOnProcessVideo(new FFMpegHelper.OnProccessVideoListener() {
            @Override
            public void onProcessDone(int errorCode, String errorMessage) {
                LogUtils.info("Done...");
                getVFActivity().startActivity(ActivityPlayMovie.class);
            }
        });
        ffHelper.addWatermark(filePath,destPath,watermarkPath);
    }

    FFMpegHelper ffHelper;
    public static String destPath;
    String watermarkPath;
//
//    public void cropMovie(){
////        LogUtils.info(""+cropView.getActualCropRect().toString());
//
//
//        ffHelper.setOnProcessVideo(new FFMpegHelper.OnProccessVideoListener() {
//            @Override
//            public void onProcessDone(int errorCode, String errorMessage) {
//                if(errorCode == 0){
//                    videoView.stopPlayback();
//                    FragVideoAddWatermark fg  = new FragVideoAddWatermark();
//                    fg.setMovieFilePath(destPath);
//                    pushFragment(fg);
//
//                }else {
//
//                }
//            }
//        });
//        destPath = Environment.getExternalStorageDirectory().getPath() + "/watermark_"+System.currentTimeMillis()+".mp4";
//        LogUtils.info("output:"+destPath);
////        ffHelper.cropVideo(filePath,"",destPath);
////        ffHelper.cropVideo(filePath,cropView.getActualCropRect(),destPath);
//
//    }

    public void addWatermark(int watermarkIndex){
        int watermarkViewId = R.layout.v_watermark_1;
        switch (watermarkIndex){
            case 0:
                watermarkViewId = R.layout.v_watermark_1;
                break;
            case 1:
                watermarkViewId = R.layout.v_watermark_2;
                break;
        }
        ViewGroup watermarkContainer = (ViewGroup) rootView.findViewById(R.id.viewWatermark);
        ViewGroup watermarkView = ViewHelper.getViewGroup(getActivity(),watermarkViewId);
        watermarkContainer.removeAllViews();
        watermarkContainer.addView(watermarkView);
    }
}
