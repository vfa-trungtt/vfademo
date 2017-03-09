package vfa.vfdemo.fragments.images;

import android.text.TextUtils;

import vfa.vfdemo.AppSettings;
import vfa.vfdemo.R;
import vfa.vfdemo.ui.TouchImageView;
import vfa.vflib.fragments.VFFragment;


public class FragEditImage extends VFFragment {

    TouchImageView imageView;
    public String _originImagePath;
    public String _tmepPath;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_edit_image;
    }

    @Override
    public void onViewLoaded() {
        imageView = (TouchImageView) rootView.findViewById(R.id.imageViewEdit);
        if(!TextUtils.isEmpty(_originImagePath)){
            AppSettings.imageLoader.displayImage("file:///"+_originImagePath,imageView);
        }
    }
}
