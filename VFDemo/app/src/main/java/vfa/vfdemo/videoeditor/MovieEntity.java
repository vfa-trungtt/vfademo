package vfa.vfdemo.videoeditor;

import android.graphics.Bitmap;

public class MovieEntity{
    public String path;
    public Bitmap thumbnail;
    public String thumbUrl;
    public String resolutionString = "";

    public int videoW;
    public int videoH;
    public long frameCount;
    public long size;
    public int duration;

    public MovieEntity(){

    }

    public MovieEntity(String moviePath){

    }
}