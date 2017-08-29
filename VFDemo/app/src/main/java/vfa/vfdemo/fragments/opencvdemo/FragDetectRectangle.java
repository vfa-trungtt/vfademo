package vfa.vfdemo.fragments.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import vfa.vfdemo.R;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragDetectRectangle extends VFFragment{
    Bitmap originBmp;
    ImageView ivSource;
    ImageDetectorLib mDetector;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_detect_rect;
    }

    @Override
    public void onViewLoaded() {

        ivSource = (ImageView) rootView.findViewById(R.id.ivSourceDetect);
        rootView.findViewById(R.id.btDetect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectRect();
            }
        });

        rootView.findViewById(R.id.btSelectImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rootView.findViewById(R.id.btCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        try{
            originBmp = ((BitmapDrawable)ivSource.getDrawable()).getBitmap();

        }catch (Exception ex){

        }
        if(originBmp == null){
//            LogUtils.error("null bitmap...");
            return;
        }


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        originBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mBuffer = stream.toByteArray();

        if(mBuffer == null){
//            LogUtils.error("no data dectect...");
            return;
        }
        mDetector = new ImageDetectorLib(originBmp.getWidth(),originBmp.getHeight());
    }

    Vector<PointF> mLastFrame;
    public byte[] mBuffer;

    private void detectRect(){
//        LogUtils.debug("start detect..");
        mLastFrame = mDetector.detectOnLastFrame(mBuffer);
        if(mLastFrame == null){
//            LogUtils.debug("detect not found...");
            return;
        }
//        LogUtils.debug("detect end...size:"+mLastFrame.size());

        for(PointF pt:mLastFrame){
//            LogUtils.info("("+pt.x+","+pt.y+")");
        }
    }
}
