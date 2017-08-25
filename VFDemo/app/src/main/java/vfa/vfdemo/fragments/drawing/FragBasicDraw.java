package vfa.vfdemo.fragments.drawing;

import android.view.View;

import vfa.vfdemo.R;
import vfa.vfdemo.ui.BaseDrawView;

import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragBasicDraw extends VFFragment {
    BaseDrawView viewDrawing;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_draw;
    }

    @Override
    public void onViewLoaded() {
        viewDrawing = (BaseDrawView) rootView.findViewById(R.id.viewDrawing);
        rootView.findViewById(R.id.btClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDrawing.clearDrawing();
            }
        });

        rootView.findViewById(R.id.btColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewDrawing.clearDrawing();
            }
        });
    }

    private void saveDrawing(){

    }
}
