package vfa.vfdemo.fragments;

import android.widget.ImageView;

import vfa.vfdemo.R;
import vfa.vflib.fragments.VFFragment;


public class FragEditImage extends VFFragment {
    private ImageView imageEditView;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_edit_image;
    }

    @Override
    public void onViewLoaded() {
        imageEditView = (ImageView) rootView.findViewById(R.id.imageViewEdit);
    }
}
