package vfa.vfdemo.fragments.drawing.glObjects;

import android.opengl.GLES20;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;


public class BaseGLObject {

    private Postion3D _postion = new Postion3D();

    public void setPosition(float x,float y,float z){
        _postion.x = x;
        _postion.y = y;
        _postion.z = z;
    }

    public void setColor(int color){

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
