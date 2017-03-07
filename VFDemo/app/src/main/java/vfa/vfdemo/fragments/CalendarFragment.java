package vfa.vfdemo.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

/**
 * Created by ThanhNH on 8/9/16.
 */
public class CalendarFragment extends VFFragment {

    public int currentMonth;
    public int currentYear;
    public int monthCount;
    private FragCalendar context = null;

    public View fragView;
    boolean isLoadingImage = false;
    int count = 0;
//    public CalendarFragment(FragCalendar act ){
//        context = act;
//    }
    public CalendarFragment(){

    }
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        LogUtils.debug("onDetach - CalendarFragment ");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LogUtils.debug("onDestroy - CalendarFragment ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (fragView != null) {
            ViewGroup parentView = ((ViewGroup) fragView.getParent());
            if (parentView != null) {
                parentView.removeView(fragView);
            }

            LogUtils.debug("reuse view calendar:" + monthCount);
            return fragView;
        }

        if(context == null){
            context = FragCalendar.instance;
        }



        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                updateOtayori();
            }
        });
        th.start();
        return fragView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateOtayoriByCurrentDate(){
        if (!this.isAdded()) return;



    }

    public void reloadOtayoriByDate(Date date){

    }

    public void updateOtayori() {
        if (!this.isAdded()) return;

//            docs = dbController.getAllDocumentBetweenDate(calendarView.getStartDate(), calendarView.getEndDate());
//            LogUtils.info("update Otayori:" + monthCount + " size:" + docs.size() + " current:" + currentPageIndex);
//            calendarView.cleanDayData();
//            if (docs == null) return;
//
//            for (DocumentModel doc : docs) {
//                Date date = Common.getDateFromString(doc.getDateCreate());
//                if (date == null) continue;
//                String colorStr = Common.LISTCOLOR[doc.ChildColor];
//                calendarView.setColorForDate(Color.parseColor(colorStr), date);
//            }
//
//            calendarView.refresh();
//            loadImge();

        if (monthCount == (context.currentPageIndex - 5000)) {
            if (context.isFirstLoad) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    }
                });
            }
        }
        //calendarView.refreshCalendar(monthCount);

    }

    int imageCount = 0;


    @Override
    public int onGetRootLayoutId() {
        return 0;
    }

    @Override
    public void onViewLoaded() {

    }
}