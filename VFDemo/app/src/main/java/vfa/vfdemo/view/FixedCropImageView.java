package vfa.vfdemo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.isseiaoki.simplecropview.CropImageView;

/**
 * Created by huynq on 3/23/17.
 */
public class FixedCropImageView extends CropImageView {
    private boolean inited = false;
    public FixedCropImageView(Context context) {
        super(context);
    }

    public FixedCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!inited) {
            super.onLayout(changed, l, t, r, b);
        }
        inited = true;
    }
}