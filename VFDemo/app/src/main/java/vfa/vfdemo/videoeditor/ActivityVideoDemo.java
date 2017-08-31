package vfa.vfdemo.videoeditor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import vfa.vfdemo.ActivitySlideMenu;

import vn.hdisoft.hdilib.utils.LogUtils;


public class ActivityVideoDemo extends ActivitySlideMenu {
    private Uri selectedVideoUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FragGallery fgGallery = new FragGallery();
//        setRootFragment(fgGallery);
//        setRootFragment(new FragVideoRoot());
//        setRootFragment(new FragVideoCrop());

        FragGalleryVideo fg = new FragGalleryVideo();
        fg.setOnSelectMovie(new FragGalleryVideo.OnSelectMovieListener() {
            @Override
            public void onSelectMovie(MovieEntity movie) {
                FragVideoCrop fg = new FragVideoCrop();
//                FragVideoAddWatermark fg = new FragVideoAddWatermark();
                fg.setMovieFilePath(movie.path);
                pushFragment(fg);
            }
        });
        setRootFragment(fg);
    }


    @Override
    public void onBeforeSetupActionBar() {
        this.title += "VideoEditor";
    }

    public void recordMovie(){

    }

    int REQUEST_TAKE_GALLERY_VIDEO = 1011;

    public void chooseMovie(){
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_GALLERY_VIDEO){
            if(resultCode == RESULT_OK){
                selectedVideoUri = data.getData();
                LogUtils.info("movie path:"+selectedVideoUri.toString());
                FragVideoPlay fg = new FragVideoPlay();
                fg.movieUri = selectedVideoUri;

                pushFragment(fg);
            }
            return;
        }
    }
}
