package vfa.vfdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Vitalify on 3/3/17.
 */

public class VFBaseDrawView extends FrameLayout {
    public VFBaseDrawView(@NonNull Context context) {
        super(context);
    }

    public VFBaseDrawView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VFBaseDrawView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VFBaseDrawView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initDraw(){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
