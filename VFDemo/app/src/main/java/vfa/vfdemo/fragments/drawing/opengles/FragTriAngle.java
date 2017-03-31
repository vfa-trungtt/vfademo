package vfa.vfdemo.fragments.drawing.opengles;

import android.opengl.GLSurfaceView;

import vfa.vfdemo.fragments.drawing.glObjects.GLRenderer;


public class FragTriAngle extends FragBase3DDraw {
    @Override
    public GLSurfaceView.Renderer getGLRender() {
        return new GLRenderer(getContext());
    }
}
