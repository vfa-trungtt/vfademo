package vfa.vfdemo.fragments.files;

import android.view.View;

import vfa.vfdemo.R;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragSearchFile extends VFFragment{
    @Override
    public int onGetRootLayoutId() {
        return 0;
    }

    @Override
    public void onViewLoaded() {
    }

    @Override
    public void setUpActionBar() {
        super.setUpActionBar();
        getVFActivity().replaceActionBar(R.layout.actionbar_search);
        getVFActivity().setActionBarOnClick(R.id.btBack, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void searchFile(String keyword){

    }
}
