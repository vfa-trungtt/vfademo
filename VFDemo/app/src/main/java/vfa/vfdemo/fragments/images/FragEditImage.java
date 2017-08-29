package vfa.vfdemo.fragments.images;

import android.text.TextUtils;

import vfa.vfdemo.AppSettings;
import vfa.vfdemo.R;
import vfa.vfdemo.ui.TouchImageView;
import vfa.vfdemo.view.FixedCropImageView;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragEditImage extends VFFragment {

    FixedCropImageView imageView;
    public String _originImagePath;
    public String _tmepPath;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_edit_image;
    }

    @Override
    public void onViewLoaded() {
        imageView = (FixedCropImageView) rootView.findViewById(R.id.imageViewEdit);
        if(!TextUtils.isEmpty(_originImagePath)){
            AppSettings.imageLoader.displayImage("file:///"+_originImagePath,imageView);
        }
    }
}
