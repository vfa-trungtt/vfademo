package vfa.vfdemo.fragments.sql;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitalify on 3/14/17.
 */

public class TableListView extends HorizontalScrollView {

    List<RowEntity> data = new ArrayList<>();

    LinearLayout viewContent;
    ColumnView viewColumn;
    ListView listData;

    public TableListView(Context context) {
        super(context);
    }

    public TableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(){
        viewContent = new LinearLayout(getContext());
        viewContent.setOrientation(LinearLayout.VERTICAL);
        addView(viewContent);

        viewColumn = new ColumnView(getContext());
        viewContent.addView(viewColumn);

        listData = new ListView(getContext());
        viewContent.addView(listData);

    }

    public void reloadData(){

    }
}
