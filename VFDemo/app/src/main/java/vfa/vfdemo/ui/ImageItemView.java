package vfa.vfdemo.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Vitalify on 10/26/16.
 */

public class ImageItemView extends RelativeLayout {
    private ImageView imageView;
    private CheckBox viewCheck;

    public ImageItemView(Context context) {
        super(context);
    }

    public ImageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(){

        imageView = new ImageView(getContext());
        viewCheck = new CheckBox(getContext());
        viewCheck.setText("");
    }

}
