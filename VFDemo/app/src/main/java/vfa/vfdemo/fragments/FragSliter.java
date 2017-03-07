package vfa.vfdemo.fragments;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import vfa.vfdemo.R;
import vfa.vfdemo.utils.ScreenUtils;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;


public class FragSliter extends VFFragment {

    private ViewGroup viewBelow;
    private ViewGroup viewAbove;
    private View viewSpliter;

    private int minHeightBelow = 0;
    private int minHeightAbove = 0;

    private int lastY;
    private int startY;
    boolean isDraging;
    private int heighOfList = 200;


    private View.OnTouchListener onHandlerTouch = new View.OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    lastY = (int) event.getRawY();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    LogUtils.debug("Y:"+event.getRawY());
//                    int curY = (int)event.getRawY();
//                    int dy = lastY - (int) event.getRawY();
//                    LogUtils.info("dy: " + dy);
//
//                    if(Math.abs(startY - (int) event.getRawY()) > 15){
//                        isDraging = true;
//                    }
//                    lastY = (int) event.getRawY();
//
//                    if(isDraging){
//
//
//                        int currentHeight = (int)(1677 - event.getRawY());
////                        int currentHeight = (int)(1677 - event.getRawY());
//                        changeHeightView(viewBelow,currentHeight);
//                    }

                    int newHeight = (int)(ScreenUtils.height - event.getRawY());
                    LogUtils.debug("new height:"+newHeight);
                    changeHeightView(viewBelow,newHeight);

//                    setTopView(viewSpliter,(int) event.getY());

                }
                break;
                case MotionEvent.ACTION_UP:{
                    isDraging = false;
                }
                break;
            }
            return true;

        }
    };
    @Override
    public int onGetRootLayoutId() {
//        return R.layout.frag_spliter_vertical;
        return R.layout.frag_spliter_vertical1;
    }

    @Override
    public void onViewLoaded() {
        heighOfList = (int) getResources().getDimension(R.dimen.height_calendar_list);

        viewAbove   = (ViewGroup) rootView.findViewById(R.id.frAbove);
        viewBelow   = (ViewGroup) rootView.findViewById(R.id.frBelow);
        viewSpliter = rootView.findViewById(R.id.viewSpliter);

        viewSpliter.setOnTouchListener(onHandlerTouch);
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int height = oldBottom - bottom;
//                LogUtils.debug("on layout changed root view!"+bottom + ",old " +oldBottom + ", distance:"+height);
            }
        });
    }

    public void changeHeightView(View view,int newHeight){
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)view.getLayoutParams();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)view.getLayoutParams();
        lp.height = newHeight;
        view.setLayoutParams(lp);
    }

    public void setTopView(View view,int newTop){
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)view.getLayoutParams();
        lp.height = 20;
        view.setLayoutParams(lp);
        view.setTop(newTop);
    }
}
