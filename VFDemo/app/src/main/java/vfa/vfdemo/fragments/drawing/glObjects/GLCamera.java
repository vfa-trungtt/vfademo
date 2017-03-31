package vfa.vfdemo.fragments.drawing.glObjects;

import android.opengl.Matrix;

/**
 * Created by Vitalify on 3/22/17.
 */

public class GLCamera {

    public float eyeX = 0.0f;
    public float eyeY = 1.0f;
    public float eyeZ = -3.0f;

    // We are looking toward the distance
    public float lookX = 0.0f;
    public float lookY = 0.0f;
    public float lookZ = 0.0f;

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    public float upX = 0.0f;
    public float upY = 1.0f;
    public float upZ = 0.0f;

    public GLCamera(){

    }

    public void setUp(float[] mViewMatrix){
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void rotateX(){

    }

    public void rotateY(){

    }

    public void rotateZ(){

    }

    public void move(){

    }
}
