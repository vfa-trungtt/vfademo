package vfa.vfdemo.videoeditor;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;

import vfa.vfdemo.R;
import vfa.vfdemo.ui.HorizonBarView;
import vfa.vfdemo.utils.FileUtis;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdilib.utils.LogUtils;
import vn.hdisoft.hdimovie.FFMpegHelper;


public class FragVideoAddWatermark extends BaseMovieFragment {

    String watermarkPath;
    HorizonBarView waterMarkBar;

    int[] barItem = {R.drawable.takanotsume_thumb,R.drawable.fashion_thumb};
    int[] arrWatermarkRes = {R.layout.v_watermark_1,R.layout.v_watermark_2};

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
        super.onViewLoaded();
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        playRepeatMovieWithDelay(500);

        //create horizon bar
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

        prepareWatermark();
    }

    public void createWatermarkImage(){
        View v = rootView.findViewById(R.id.viewWatermark);
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
//        try {
//            String fname = "watermark-"+ System.currentTimeMillis() +".png";
//            File file = new File (Environment.getExternalStorageDirectory().getPath(), fname);
//
//            FileOutputStream out = new FileOutputStream(file);
//            b.compress(Bitmap.CompressFormat.PNG, 100, out);
//            out.flush();
//            out.close();
//
//            watermarkPath = file.getAbsolutePath();
//            v.setDrawingCacheEnabled(false);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String fname = "watermark-"+ System.currentTimeMillis() +".png";
        File file = new File (Environment.getExternalStorageDirectory().getPath(), fname);
        FileUtis.createFileFromBitmap(b,file.getAbsolutePath(), new FileUtis.OnCreateImageFileListener() {
            @Override
            public void onCreateFileDone(String filePath) {
                watermarkPath = filePath;
            }
        });
        v.setDrawingCacheEnabled(false);
    }

    public void drawWatermark(){
        videoView.stopPlayback();
        showLoading();
        createWatermarkImage();
//        destPath = getActivity().getCacheDir().getPath() + "/crop_"+System.currentTimeMillis()+".mp4";
        destPath = Environment.getExternalStorageDirectory().getPath() + "/watermark_"+System.currentTimeMillis()+".mp4";
        LogUtils.info("output:"+destPath);
        ffHelper.setOnProcessVideo(new FFMpegHelper.OnProccessVideoListener() {
            @Override
            public void onProcessDone(int errorCode, String errorMessage) {
                LogUtils.info("Done...");
                hideLoading();
                getVFActivity().setPublicString("movie_path",destPath);
                getVFActivity().startActivity(ActivityPlayMovie.class);
            }
        });
        ffHelper.addWatermark(srcPath,destPath,watermarkPath);
    }

    private void prepareWatermark(){

    }
    public void addWatermark(int watermarkIndex){
        int watermarkViewId = arrWatermarkRes[watermarkIndex];

        ViewGroup watermarkContainer = (ViewGroup) rootView.findViewById(R.id.viewWatermark);
        ViewGroup watermarkView = ViewHelper.getViewGroup(getActivity(),watermarkViewId);
        watermarkContainer.removeAllViews();
        watermarkContainer.addView(watermarkView);
    }
}
