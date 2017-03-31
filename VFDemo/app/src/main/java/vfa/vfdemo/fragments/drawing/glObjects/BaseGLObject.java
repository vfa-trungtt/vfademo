package vfa.vfdemo.fragments.drawing.glObjects;


import android.opengl.GLES20;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;


public class BaseGLObject {

    static final int COORDS_PER_VERTEX = 3;
    //GL ES 2.0
    public int mPositionHandle;
    public int mColorHandle;
    public int program;


    private float[] _color = {1,1,1,1};//default white

    public float[] mViewMatrix = new float[16];
    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    public float[] mProjectionMatrix = new float[16];
    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    public float[] mMVPMatrix = new float[16];
    public final float[] modelMatrix = new float[16];

    private Postion3D _postion = new Postion3D();

    public void setPosition(float x,float y,float z){
        _postion.x = x;
        _postion.y = y;
        _postion.z = z;
    }

    public void setColor(int cl){

    }

    public void setColor(String clHex){

    }

    public void setColor(int r,int g,int b,int a){
        _color[0] = r/255;
        _color[1] = g/255;
        _color[2] = b/255;
        _color[3] = a/255;
    }

    public void setColor(float[] cl){
        _color = cl;
    }

    public float[] getColor(){
        return _color;
    }

    public void draw(GL10 gl) {
    }

    public void draw2GL(){

    }

    public  int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public  void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("3d", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
