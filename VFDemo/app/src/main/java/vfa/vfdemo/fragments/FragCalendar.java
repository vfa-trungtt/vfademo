package vfa.vfdemo.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vfa.vfdemo.ui.DragContainerView;
import vfa.vfdemo.utils.ScreenUtils;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

public class FragCalendar extends VFFragment {

    boolean isOpenListData = false;
    RelativeLayout containerList;
    AlertDialog dialog;
    int startY = 0;

//    ArrayList<DocumentModel> listData = new ArrayList<>();

    private FrameLayout frContainer;
    private ImageButton buttonHandler;
    private View frHandler;
    protected TextView tvDateTitle;
    private ImageButton buttonToday;
    protected ViewPager pager;
    private CalendarPagerAdapter _adapter;
    private int maxMonthCount = 10000;
    protected int currentPageIndex = 5000;
     Date currentSelectedDate = new Date();
    private boolean isDraging = false;
    protected boolean isFirstLoad = true;
    //Moving handler
    private GestureDetectorCompat gestureDetector;
    private int lastY = 0;
    private int heighOfList = 200;
    private int currentheighOfList = 0;

    private int animDuration = 300; //ms
//    private DragSortListView lvOtayori;


    protected static FragCalendar instance = null;
    private View viewNoRecord;
    private View buttonCamera;
    private boolean isAnimating = false;

    private DragContainerView dragContainerView;
//    DocumentModel selectDragOtayori;


    public boolean allowContextMenu = true;

    private OnTouchListener onHandlerTouch = new OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

			//ThanhNH  #13295 note #12
			boolean isFling = gestureDetector.onTouchEvent(event);
			switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					lastY = (int) event.getRawY();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
				{
					int curY = (int)event.getRawY();
					int dy = lastY - (int) event.getRawY();
					LogUtils.info("dy: " + dy);

					if(Math.abs(startY - (int) event.getRawY()) > 15){
						isDraging = true;
					}
					lastY = (int) event.getRawY();

					if(isDraging){
						frContainer.setVisibility(View.VISIBLE);
						tvDateTitle.setVisibility(View.VISIBLE);
						LayoutParams lp2 = (LayoutParams)frContainer.getLayoutParams();
						int currentHeight = (int)(ScreenUtils.height - event.getRawY());
						if(currentHeight > (heighOfList -50)){
							currentHeight = heighOfList;
							isOpenListData = true;
							buttonHandler.setSelected(false);
                            tvDateTitle.setVisibility(View.VISIBLE);

						} else if(currentHeight < 100) {
							currentHeight = 0;
							buttonHandler.setSelected(true);
							isOpenListData = false;
                            tvDateTitle.setVisibility(View.GONE);
						} else if ( currentHeight > heighOfList){
							currentHeight =  heighOfList;
							buttonHandler.setSelected(false);
							isOpenListData = true;
                            tvDateTitle.setVisibility(View.VISIBLE);
						}

//                        PreferenceUtil.setInfolistView(context, isOpenListData ? 1: 2);
//						rootView.setEnabled(true);
						frContainer.setEnabled(true);

						lp2.height = currentHeight;
						frContainer.setLayoutParams(lp2);
					}

				}
				break;
				case MotionEvent.ACTION_UP:{
					LayoutParams lp2 = (LayoutParams) frContainer.getLayoutParams();
					if(isFling || isDraging){
						if (lp2.height > heighOfList / 2) {
							lp2.height = heighOfList;
							buttonHandler.setSelected(false);
                            tvDateTitle.setVisibility(View.VISIBLE);
							isOpenListData = true;

						} else {
							lp2.height = 0;
							buttonHandler.setSelected(true);
                            tvDateTitle.setVisibility(View.GONE);
							isOpenListData = false;
						}
						frContainer.setLayoutParams(lp2);
//                        PreferenceUtil.setInfolistView(context, isOpenListData ? 1: 2);
					} else {
						if(isOpenListData){
							closeList();
						} else {
							openList();
						}
					}

					isDraging = false;
				}
				break;
			}
			return true;
			//ThanhNH comment code:  #13295 note #12
//            boolean isFling = gestureDetector.onTouchEvent(event);
//            if (isFling) return true;
//
//            switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                case MotionEvent.ACTION_DOWN:
//                    lastY = (int) event.getRawY();
//                    startY = (int) event.getRawY();
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    if (!isDraging) {
//                        toggList();
//                        isDraging = false;
//                        return true;
//                    }
//                    LogUtils.info("IS DRAGING..");
//
//                    LayoutParams lp1 = (LayoutParams) frContainer.getLayoutParams();
//                    if (lp1.height < heighOfList / 2) {
//                        isOpenListData = true;
//                        LogUtils.info("Open list.");
//                        closeList();
//                    } else {
//                        isOpenListData = false;
//                        //openList();
//                    }
//                    isDraging = false;
//                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    break;
//                case MotionEvent.ACTION_POINTER_UP:
//                    break;
//                case MotionEvent.ACTION_MOVE:
//
//                    int dy = lastY - (int) event.getRawY();
//                    LogUtils.info("dy: " + dy);
//
//                    if (!isOpenListData) {
//                        frContainer.setVisibility(View.VISIBLE);
//                        tvDateTitle.setVisibility(View.VISIBLE);
//                    }
//                    LayoutParams lp = (LayoutParams) frContainer.getLayoutParams();
//                    currentheighOfList = lp.height;
//                    if (dy > 0) {
//                        openList();
//                    }
//
//                    if (Math.abs(startY - (int) event.getRawY()) > 15) {
//                        isDraging = true;
//                    }
//                    lastY = (int) event.getRawY();
//                    break;
//            }
//            rootView.invalidate();
//            return true;
        }
    };

//    private DocAdapter adapter;

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		showLoading();
        LogUtils.info("onActivityCreated-Calendar");
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.info("onSaveInstanceState-Calendar");
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        LogUtils.info("pos:" + info.position);
//        DocumentModel doc = listData.get(info.position);
//
//        if(MBassHelper.IsSyning){
//            inflater.inflate(R.menu.context_menu1, menu);
//
//            if(doc.getIsBookmark() == 1){
//                menu.getItem(0).setTitle(getResources().getString(R.string.context_menu_clip_off));
//            }else{
//                menu.getItem(0).setTitle(getResources().getString(R.string.context_menu_clip_on));
//            }
//        }else{
//            inflater.inflate(R.menu.context_menu, menu);
//
//            if(doc.getIsBookmark() == 1){
//                menu.getItem(1).setTitle(getResources().getString(R.string.context_menu_clip_off));
//            }else{
//                menu.getItem(1).setTitle(getResources().getString(R.string.context_menu_clip_on));
//            }
//        }


//        inflater.inflate(R.menu.context_menu, menu);
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
//        LogUtils.info("pos:" + info.position);
//        DocumentModel doc = listData.get(info.position);
//        if (doc.getIsBookmark() == 1) {
//            menu.getItem(1).setTitle(getResources().getString(R.string.context_menu_clip_off));
//        } else {
//            menu.getItem(1).setTitle(getResources().getString(R.string.context_menu_clip_on));
//        }
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        LogUtils.debug("onContextItemSelected:"+this.getClass().getName());
//        if(!allowContextMenu) return false;
//
//        if (!(item.getMenuInfo() instanceof AdapterContextMenuInfo)) {
//            return false;
//        }
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//        LogUtils.info("pos:" + info.position);
//        DocumentModel doc = listData.get(info.position);
////        LogUtils.info("UPDATE DOCUMENT INTO CLOUD ID:" + doc.CloundId);
//        doc = dbController.getDocumentsById(doc.getId());
////        LogUtils.info("UPDATE DOCUMENT INTO CLOUD ID:" + doc.CloundId);
//        switch (item.getItemId()) {
//            case R.id.menu_clip:
//                if (doc.getIsBookmark() == 0) {
//                    doc.setIsBookmark(1);
//                } else {
//                    doc.setIsBookmark(0);
//                }
//
//                dbController.updateClipDocument(doc);
//                reloadCalendar();
////			adapter.notifyDataSetChanged();
//                LogUtils.info("UPDATE DOCUMENT INTO CLOUD ID:" + doc.CloundId);
//                if (PreferenceUtil.didLogin(getActivity())) {
//                    UploadCloudTask upload = new UploadCloudTask(getActivity());
//                    upload.upLoadDocument(doc);
//                }
//
//                return true;
//            case R.id.menu_delete:
//                confirmDeleteDoc(doc);
//                return true;
//            case R.id.menu_sort:
//                LogUtils.info("sort otayori...");
//                lvOtayori.setDragItem(info.position);
//                MBassHelper.IsSorting = true;
//
//                unregisterForContextMenu(lvOtayori);
//                lvOtayori.setDragEnabled(true);
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

//    private void confirmDeleteDoc(final DocumentModel doc) {
//        DialogHelper.showConfirmDeleteOtayori(getActivity(), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dbController.updateDeleteFlag(doc);
//                if (PreferenceUtil.didLogin(getActivity())) {
//                    doc.IsDelete = true;
//                    UploadCloudTask upload = new UploadCloudTask(getActivity());
//                    upload.upLoadDocument(doc);
//                }
//                reloadCalendar();
//            }
//        });
//
//    }


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragCalendar.instance = this;

        if (rootView != null) {
            ViewGroup parentView = ((ViewGroup) rootView.getParent());
            if (parentView != null) {
                parentView.removeView(rootView);
            }

            // Return it
            LogUtils.debug("reuse view calendar master");
            return rootView;
        }

//        rootView = getViewFromLayoutRes(R.layout.frag_calendar);
//
//        currentPageIndex = PreferenceUtil.CurrentCalendarIndex;
//
//        pager = (ViewPager) rootView.findViewById(R.id.pager);
//        _adapter = new CalendarPagerAdapter(getChildFragmentManager(), this);
//        pager.setAdapter(_adapter);
//        pager.setCurrentItem(currentPageIndex);
//
//
//        frContainer = (FrameLayout) rootView.findViewById(R.id.frList);
//        frContainer.addView(getViewInLayoutId(R.layout.frag_list));
//
//        setUpListView();
//
//        viewNoRecord = rootView.findViewById(R.id.tvNoRecord);
//        buttonCamera = rootView.findViewById(R.id.btnCamera);
//        buttonCamera.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                cancelDrag();
//
//                File dir = new File(ConfigLib.URL_TEMP);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                if (!PreferenceUtil.getBoolean(getActivity(), PreferenceUtil.KEY_FRIST_CAMERA_BUTTON_ADD_CLICK)) {
//                    Common.postGoogleAnalyticFlurry(getActivity(), ConfigLib.INFOLIST_FIRST_CAMERA_BTN);
//                    PreferenceUtil.setBoolean(getActivity(), PreferenceUtil.KEY_FRIST_CAMERA_BUTTON_ADD_CLICK, true);
//                }
//
//                postAnalyticEventName(R.string.ga_cat_camera, R.string.ga_cat_camera_action_take, R.string.ga_cat_camera_action_take_labelbutton);
//                Common.postGoogleAnalyticFlurry(getActivity(), ConfigLib.INFOLIST_CAMERA_BTN);
//
//                Intent intent = new Intent(context, CameraViewActivity.class);
//                intent.putExtra(CameraViewActivity.KEY_TAKE_PICTURE, true);
//                intent.putExtra(CameraViewActivity.KEY_SHOW_NUM_PHOTO, false);
//                context.startActivityForResult(intent, NIftyBaseActivity.REQUEST_RELOAD_DATA);
//            }
//        });
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        heighOfList = (int) getResources().getDimension(R.dimen.height_calendar_list);

//        buttonToday = (ImageButton) rootView.findViewById(R.id.ibToday);
        buttonToday.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentSelectedDate = new Date();
//                PreferenceUtil.CalendarSelected = currentSelectedDate;
//
//                loadOtayoriByDate();
//
//                pager.setCurrentItem(maxMonthCount / 2);
//                tvDateTitle.setText(Common.getDateStringJP(currentSelectedDate));
//
//                CalendarFragment fg = (CalendarFragment) _adapter.instantiateItem(pager, pager.getCurrentItem());
//                if (fg != null && !isFirstLoad) {
//                    if (fg.calendarView == null) {
//                        LogUtils.error("NULL CALENDAR");
//                        return;
//                    }
//                    fg.calendarView.setDateSelected(currentSelectedDate);
//                } else {
//                    isFirstLoad = false;
//                }
            }
        });

//        tvDateTitle = (TextView) rootView.findViewById(R.id.tvDateTitle);
//
//        if (PreferenceUtil.CalendarSelected == null) {
//            PreferenceUtil.CalendarSelected = new Date();
//        }
//        currentSelectedDate = PreferenceUtil.CalendarSelected;
//        tvDateTitle.setText(Common.getDateStringJP(currentSelectedDate));
//
//        pager.setOnPageChangeListener(new OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int arg0) {
//                LogUtils.info("current page:" + arg0);
//                currentPageIndex = arg0;
//                CalendarFragment fg = (CalendarFragment) _adapter.instantiateItem(pager, pager.getCurrentItem());
////				fg.loadImge();
//                fg.calendarView.setDateSelected(currentSelectedDate);
////				fg.updateOta();
//                reloadCalendar();
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //pager.setOffscreenPageLimit(2);
//
//        frContainer = (FrameLayout) rootView.findViewById(R.id.frList);
//        frHandler = rootView.findViewById(R.id.frHandler);
//
//        PreferenceUtil.IsCalendarLarge = (PreferenceUtil.getInfolistView(context) == 2);
//        if (PreferenceUtil.IsCalendarLarge) {
//            frContainer.setVisibility(View.GONE);
//            tvDateTitle.setVisibility(View.GONE);
//            isOpenListData = false;
//            int value = PreferenceUtil.getInfolistView(context);
//            if(value == -1){
//                postAnalyticEventName(getString(R.string.ga_cat_start_app), getString(R.string.ga_cat_infolist), getString(R.string.ga_cat_calendar_big));
//            }
//            PreferenceUtil.setInfolistView(context, 2);
//        } else {
//            frContainer.setVisibility(View.VISIBLE);
//            tvDateTitle.setVisibility(View.VISIBLE);
//            isOpenListData = true;
//            int value = PreferenceUtil.getInfolistView(context);
//            if(value == -1){
//                postAnalyticEventName(getString(R.string.ga_cat_start_app), getString(R.string.ga_cat_infolist), getString(R.string.ga_cat_calendar_small));
//            }
//            PreferenceUtil.setInfolistView(context, 1);
//        }
//
//        containerList = (RelativeLayout) rootView.findViewById(R.id.relList);
//
//        buttonHandler = (ImageButton) rootView.findViewById(R.id.btHandler);
//        buttonHandler.setOnTouchListener(onHandlerTouch);
//        frHandler.setOnTouchListener(onHandlerTouch);
//        tvDateTitle.setOnTouchListener(onHandlerTouch);
//
//		buttonHandler.setSelected(!isOpenListData);
//        gestureDetector = new GestureDetectorCompat(getActivity(), new GestureListener());
//
//        LogUtils.memoryUsedInfo();
    }

    boolean isEndrag = false;
//    boolean isDraging;

    public void reloadCalendar() {
//        if(dbController == null ){ return; }
//
//        listData = dbController.getDocumentsByDate( Common.getDateStringDB(currentSelectedDate));
//        if (listData.size() == 0) {
//            viewNoRecord.setVisibility(View.VISIBLE);
//        } else {
//            viewNoRecord.setVisibility(View.INVISIBLE);
//        }
//
//        adapter = new DocAdapter(getActivity(), listData);
//        lvOtayori.setAdapter(adapter);
//
//        final CalendarFragment fg = (CalendarFragment) _adapter.instantiateItem(pager, pager.getCurrentItem());
//        if (fg != null) {
//            Thread th = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    fg.updateOtayori();
//                }
//            });
//            th.start();
//        }
//
//        PreferenceUtil.isNeedReloadData = false;
    }

    @Override
    public void onDestroy() {
        LogUtils.info("Destroy - " + getClass().getSimpleName());
        super.onDestroy();
    }

    public void openList() {

		if(isAnimating) return;
		if(isOpenListData) return;

//		PreferenceUtil.IsCalendarLarge = false;
//		rootView.setEnabled(false);
		frContainer.setEnabled(false);

		isOpenListData = true;
		isAnimating = true;
		LogUtils.info("Open List");
		buttonHandler.setSelected(false);
		frContainer.setVisibility(View.VISIBLE);
		tvDateTitle.setVisibility(View.VISIBLE);
		ValueAnimator animator =  ValueAnimator.ofObject (new HeightEvaluator(frContainer), 0, heighOfList);

		animator.setDuration(animDuration);
		animator.setInterpolator(new AccelerateInterpolator(1));
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{

			}
		});
		animator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				// done
				frContainer.setVisibility(View.VISIBLE);
				tvDateTitle.setVisibility(View.VISIBLE);

				currentheighOfList = heighOfList;
//				rootView.setEnabled(true);
				frContainer.setEnabled(true);
				buttonHandler.setSelected(false);
				isAnimating = false;

			}
		});
		animator.start ();

//		PreferenceUtil.setInfolistView(context, 1);
//		postAnalyticScreenName(R.string.ga_screen_calendar_small);
    }

    public void closeList() {

		if(isAnimating) return;
		if(!isOpenListData) return;
		isOpenListData = false;

		ValueAnimator animator =  ValueAnimator.ofObject (new HeightEvaluator(frContainer),  heighOfList, 0);

		animator.setDuration(animDuration);
		animator.setInterpolator(new AccelerateInterpolator(1));
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{

			}
		});
		animator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				// done
				frContainer.setVisibility(View.GONE);
				tvDateTitle.setVisibility(View.GONE);
				currentheighOfList = 0;
//				rootView.setEnabled(true);
				frContainer.setEnabled(true);
				isAnimating = false;
				buttonHandler.setSelected(true);

			}
		});
		animator.start ();
//        int value = PreferenceUtil.getInfolistView(context);
//		PreferenceUtil.setInfolistView(context, 2);
//		postAnalyticScreenName(R.string.ga_screen_calendar_larger);
    }

    public void toggList() {
        LogUtils.info("Togg List");
        if (isOpenListData) {
            closeList();
            dragContainerView.clear();
            dragContainerView.stopAnim();
        } else {
            openList();
        }
    }

    public void cancelDrag(){
//        if(MBassHelper.IsSorting){
//            lvOtayori.getDragOutListener().dragEnd();
//            lvOtayori.setDragEnabled(false);
//            registerForContextMenu(lvOtayori);
//            MBassHelper.IsSorting = false;
//        }
    }


    @Override
    public int onGetRootLayoutId() {
        return 0;
    }

    @Override
    public void onViewLoaded() {

    }

    private class GestureListener extends SimpleOnGestureListener {

        private final int SWIPE_MIN_DISTANCE = 5;
        private final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            LogUtils.info("VelocityX " + velocityX + " velocityY" + velocityY);

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // Bottom to top, your code here
                LogUtils.info("Fling up.");
				//ThanhNH comment code:  #13295 note #12
                //toggList();
				//End comment
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // Top to bottom, your code here
                LogUtils.info("Fling down.");
				//ThanhNH comment code:  #13295 note #12
                //toggList();
				//End comment
                return true;
            }
            return false;
        }
    }

//
//    public void unregistContextMenu(){
//        LogUtils.info("unregist menu.");
//        allowContextMenu = false;
//        lvOtayori.setOnCreateContextMenuListener(null);
//        this.unregisterForContextMenu(lvOtayori);
//    }
//    public void registerContextMenu(){
//        allowContextMenu = true;
//
//        this.registerForContextMenu(lvOtayori);
//    }

    /*======================================================================================*/
    class CalendarPagerAdapter extends FragmentStatePagerAdapter {

        protected FragCalendar context = null;
        public CalendarPagerAdapter(FragmentManager fm, FragCalendar act) {
            super(fm);
            context = act;
        }

        @Override
        public Fragment getItem(int position) {
            LogUtils.info("Load pos:" + position);
            int count = position - maxMonthCount / 2;
//            CalendarFragment fg = new CalendarFragment();
//            fg.monthCount = count;

            return null;
        }

        @Override
        public int getCount() {
            return maxMonthCount;
        }
    }
	 //ThanhNH add code:  #13295 note #12
	private static class HeightEvaluator extends IntEvaluator {

		private View mView;

		public HeightEvaluator(View v) {
			this.mView = v;
		}

		@Override
		public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
			int num = (Integer) super.evaluate(fraction, startValue, endValue);
			ViewGroup.LayoutParams params = mView.getLayoutParams();
			params.height = num;
			mView.setLayoutParams(params);
			return num;
		}

	}

}
