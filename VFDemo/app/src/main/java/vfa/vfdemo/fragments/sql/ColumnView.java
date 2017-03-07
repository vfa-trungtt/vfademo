package vfa.vfdemo.fragments.sql;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.utils.ViewHelper;

/**
 * Created by Vitalify on 3/7/17.
 */

public class ColumnView extends LinearLayout {
    public int colCount = 0;
    List<String> _cols = new ArrayList<>();

    public ColumnView(Context context) {
        super(context);
    }

    public ColumnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColumnView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView(){
        if(isInEditMode()){
            colCount = 10;

        }else{

        }
    }

    public void setColNameList(List<String> data){
        colCount = data.size();
        _cols   = data;
        removeAllViews();
        for(int i=0;i < colCount;i++){

            ViewItemColumn item = new ViewItemColumn(getContext());
            item.setCaption(_cols.get(i));
            addView(item);

        }
    }

    public TextView getColItemView(){
        TextView tv = new TextView(getContext());
        return tv;
    }

    class ViewItemColumn extends FrameLayout{

        TextView _tvCaption;

        private View viewContent;
        public ViewItemColumn(@NonNull Context context) {
            super(context);
            initItemView();
        }

        public ViewItemColumn(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public ViewItemColumn(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initItemView();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewItemColumn(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initItemView();
        }

        public void initItemView(){
            viewContent = ViewHelper.getView(getContext(),R.layout.item_column);
            addView(viewContent);
            _tvCaption = (TextView) findViewById(R.id.tvCaption);
        }

        public void setCaption(String text){
            _tvCaption.setText(text);
        }
    }
}
