package vfa.vfdemo.fragments.calendar;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vn.hdisoft.hdilib.fragments.VFFragment;


/**
 * Created by Vitalify on 3/2/17.
 */

public class FragCalendarDemo extends VFFragment {
    public GridView gridCalendar;
    List<String> listDate = new ArrayList<>();

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_cal_demo;
    }

    @Override
    public void onViewLoaded() {

        gridCalendar = (GridView)rootView.findViewById(R.id.gridCalendar);
        gridCalendar.setNumColumns(7);


        createDateData(1);
        DateAdapter adapter = new DateAdapter(getContext(),listDate);

        gridCalendar.setAdapter(adapter);
    }

    private void createDateData(int month){
        for(int i = 1;i < 43;i++){
            listDate.add(""+i);
        }
    }

    class DateAdapter extends BaseArrayAdapter<String>{

        public DateAdapter(Context context, String[] objects) {
            super(context, objects);
        }

        public DateAdapter(Context context, List<String> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
            return R.layout.cell_day;
        }

        @Override
        public void bindItem(int pos, View v) {

        }
    }

}
