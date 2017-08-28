package vfa.vfdemo.activity;

import android.content.Intent;
import android.view.View;

import vfa.vfdemo.R;
import vfa.vfdemo.dialogs.DialogActionSheet;
import vfa.vfdemo.dialogs.DialogMovieActionSheet;
import vn.hdisoft.hdilib.fragments.VFFragment;


public class FragVideoRoot extends VFFragment {

    public FragVideoRoot(){

    }

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_root;
    }

    @Override
    public void onViewLoaded() {
        setClickListener(R.id.buttonOpenVideo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMovieActionSheet dialog = new DialogMovieActionSheet(getActivity());
                dialog.show(new DialogActionSheet.DialogActionSheetListener() {
                    @Override
                    public void onSelectAction(int action) {

                        switch (action){
                            case DialogMovieActionSheet.ACTION_SHEET_CANCEL:
                                break;
                            case DialogMovieActionSheet.ACTION_SHEET_CAMERA:
                                ((ActivityVideoDemo)getActivity()).recordMovie();
                                break;
                            case DialogMovieActionSheet.ACTION_SHEET_GALLERY:
                                ((ActivityVideoDemo)getActivity()).chooseMovie();

                                break;
                        }
                    }
                });
            }
        });
    }
}

