package vfa.vfdemo.fragments.drawing;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import vfa.vfdemo.R;
import vfa.vfdemo.fragments.drawing.glObjects.*;
import vfa.vfdemo.fragments.drawing.glObjects.MyGLRenderer;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

/**
 * Created by Vitalify on 3/20/17.
 */

public class Frag3DDraw extends VFFragment {

    private GLSurfaceView glView;
//    private MyGLRenderer glRender;

    private GL2Render glRender;

    Cube cube = new Cube();
    Plane plane = new Plane();

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

//        glRender = new MyGLRenderer(getContext());
//        glRender = new GL2Render();
        glView = new GLSurfaceView(getContext());           // Allocate a GLSurfaceView

//        glView.setRenderer(glRender); // Use a custom renderer
        final ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            glView.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
//            glView.setRenderer(new LessonOneRenderer());
            glView.setRenderer(new GL2Render());
//            glView.setRenderer(new MyGLRenderer());
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }
//        glView.setRenderer(new LessonOneRenderer()); // Use a custom renderer

        addContentView(glView);

        View v = ViewHelper.getView(getContext(), R.layout.view_3d_tool);
        addContentView(v);

        rootView.findViewById(R.id.btCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.debug("camera");
//                glRender.zoom += -1.0f;
            }
        });

        rootView.findViewById(R.id.btRotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                glRender.angleCube += 30f;
//                LogUtils.debug("rotate:"+glRender.angleCube);
            }
        });

        glView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


//        glRender.addObject(cube);
//        glRender.addObject(plane);
//        cube.setPosition(0,0,5);
//        glRender.addObject(cube);
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
