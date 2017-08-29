package vfa.vfdemo.fragments.drawing.opengles;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdilib.utils.LogUtils;


/**
 * Created by Vitalify on 3/20/17.
 */

public abstract class FragBase3DDraw extends VFFragment {
    private GLSurfaceView glView;

    public abstract GLSurfaceView.Renderer getGLRender();

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private final int SWIPE_MIN_DISTANCE = 5;
        private final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            LogUtils.info("VelocityX " + velocityX + " velocityY" + velocityY);

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                LogUtils.info("Fling up.");
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                LogUtils.info("Fling down.");
                return true;
            }
            return false;
        }
    }

    @Override
    public void onViewLoaded() {

        glView = new GLSurfaceView(getContext());           // Allocate a GLSurfaceView
        final ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            glView.setEGLContextClientVersion(2);
            glView.setRenderer(getGLRender());
        }
        else {
            return;
        }

        addContentView(glView);
//        View v = ViewHelper.getView(getContext(), R.layout.view_3d_tool);
//        addContentView(v);
//
//        rootView.findViewById(R.id.btCamera).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.debug("camera");
//            }
//        });
//
//        rootView.findViewById(R.id.btRotate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        glView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        glView.onPause();
    }

    // Call back after onPause()
    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
    }
}
