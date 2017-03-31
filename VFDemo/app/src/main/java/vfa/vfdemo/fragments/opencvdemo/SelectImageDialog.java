package vfa.vfdemo.fragments.opencvdemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.GridView;

import vfa.vfdemo.R;

/**
 * Created by Vitalify on 3/31/17.
 */

public class SelectImageDialog extends Dialog{
    GridView gridView;

    public SelectImageDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_select_image);
        gridView = (GridView) findViewById(R.id.gridImages);
    }

}
