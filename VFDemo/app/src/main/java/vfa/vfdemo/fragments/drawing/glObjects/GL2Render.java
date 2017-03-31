package vfa.vfdemo.fragments.drawing.glObjects;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Vitalify on 3/21/17.
 */

public class GL2Render implements GLSurfaceView.Renderer {

    private final float[] mMVPMatrix = new float[16];

    public static float[] ProjectionMatrix  = new float[16];
    public static float[] ViewMatrix        = new float[16];

    Square square;// = new Square();
    Box box;

//    Cube2 cube;

    public GLCamera glCamera = new GLCamera();
    public GL2Render(){

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        square = new Square();
        square.setPosition(0,0,2.0f);

        box = new Box();
//        cube = new Cube2();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates

//        GLES20.glViewport(0, 0, width, height);
//
//        // Create a new perspective projection matrix. The height will stay the same
//        // while the width will vary as per aspect ratio.
//        final float ratio = (float) width / height;
//        final float left = -ratio;
//        final float right = ratio;
//        final float bottom = -1.0f;
//        final float top = 1.0f;
//        final float near = 1.0f;
//        final float far = 10.0f;

//        Matrix.frustumM(ProjectionMatrix, 0, left, right, bottom, top, near, far);
//        Setting the perspective projection
//        Matrix.frustumM(ProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.frustumM(ProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 100);
//        Matrix.frustumM(ProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        glCamera.setUp(ViewMatrix);
        square.draw2GL();
        box.draw2GL();

//        cube.draw2GL();
    }

}
