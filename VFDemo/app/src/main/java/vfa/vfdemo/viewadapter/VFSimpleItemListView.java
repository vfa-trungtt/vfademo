package vfa.vfdemo.viewadapter;


import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;

import vfa.vfdemo.utils.ViewHelper;



public class VFSimpleItemListView extends android.support.v7.widget.AppCompatTextView {
    public VFSimpleItemListView(Context context) {
        super(context);
        init();
    }

    public VFSimpleItemListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VFSimpleItemListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        int pad = (int) ViewHelper.convertDpToPixel(10,getContext());
        setPadding(pad,pad,pad,pad);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//        setTextSize(ViewHelper.convertDpToPixel(18,getContext()));
    }
}
