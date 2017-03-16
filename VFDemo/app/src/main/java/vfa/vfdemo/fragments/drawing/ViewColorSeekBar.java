package vfa.vfdemo.fragments.drawing;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import vfa.vfdemo.R;
import vfa.vfdemo.utils.ViewHelper;

/**
 * Created by Vitalify on 3/16/17.
 */

public class ViewColorSeekBar extends FrameLayout {

    SeekBar seekBar;
    EditText inputValue;

    public ViewColorSeekBar(@NonNull Context context) {
        super(context);
    }

    public ViewColorSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewColorSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        View contentView = ViewHelper.getView(getContext(), R.layout.view_seek_bar_input);
        addView(contentView);

    }
}
