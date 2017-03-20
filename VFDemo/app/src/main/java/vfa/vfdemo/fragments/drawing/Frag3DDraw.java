package vfa.vfdemo.fragments.drawing;

import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import vfa.vfdemo.R;
import vfa.vfdemo.fragments.drawing.glObjects.Cube;
import vfa.vfdemo.fragments.drawing.glObjects.Plane;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

/**
 * Created by Vitalify on 3/20/17.
 */

public class Frag3DDraw extends VFFragment {

    private GLSurfaceView glView;
    private MyGLRenderer glRender;
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

        glRender = new MyGLRenderer(getContext());
        glView = new GLSurfaceView(getContext());           // Allocate a GLSurfaceView
        glView.setRenderer(glRender); // Use a custom renderer
        addContentView(glView);

        View v = ViewHelper.getView(getContext(), R.layout.view_3d_tool);
        addContentView(v);

        rootView.findViewById(R.id.btCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.debug("camera");
                glRender.zoom += -1.0f;
            }
        });

        rootView.findViewById(R.id.btRotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glRender.angleCube += 30f;
            }
        });

        glView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        glRender.addObject(cube);

//        glRender.addObject(plane);
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
