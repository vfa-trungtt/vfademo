package vfa.vfdemo.fragments.drawing.glObjects;

import javax.microedition.khronos.opengles.GL10;


public class BaseGLObject {

    private Postion3D _postion = new Postion3D();

    public void setPosition(float x,float y,float z){
        _postion.x = x;
        _postion.y = y;
        _postion.z = z;
    }

    public void draw(GL10 gl) {
    }
}
