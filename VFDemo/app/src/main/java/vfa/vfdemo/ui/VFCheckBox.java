package vfa.vfdemo.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class VFCheckBox extends CheckBox{
    public VFCheckBox(Context context) {
        super(context);
    }

    public VFCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VFCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VFCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
