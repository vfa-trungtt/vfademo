package vfa.vfdemo.fragments.sql;

import android.content.Context;
import android.graphics.Canvas;
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


public class RowView extends LinearLayout {
    public int _colCount = 0;
    List<String> _cols = new ArrayList<>();

    ArrayList<ViewItemRow> colsView = new ArrayList();

    public RowView(Context context,int colCount) {
        super(context);
        _colCount = colCount;
        initView();
    }
    public RowView(Context context) {
        super(context);
        initView();
    }

    public RowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView(){
        setOrientation(HORIZONTAL);
        for(int i = 0; i < _colCount; i++){

            ViewItemRow item = new ViewItemRow(getContext());
            colsView.add(item);
            addView(item);

        }
    }


    public void setRowData(RowEntity rowData){
        _cols   = rowData.dataRow;
        for(int i = 0; i < _colCount; i++){

            ViewItemRow item = colsView.get(i);
            item.setCaption(_cols.get(i));
        }
    }


    class ViewItemRow extends FrameLayout{

        TextView _tvCaption;

        private View viewContent;
        public ViewItemRow(@NonNull Context context) {
            super(context);
            initItemView();
        }

        public ViewItemRow(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public ViewItemRow(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initItemView();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewItemRow(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initItemView();
        }

        public void initItemView(){
            viewContent = ViewHelper.getView(getContext(),R.layout.item_row);
            this.addView(viewContent);
            _tvCaption = (TextView) findViewById(R.id.tvContent);
        }

        public void setCaption(String text){
            _tvCaption.setText(text);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }
}
