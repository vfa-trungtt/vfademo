package vfa.vfdemo.fragments.nifty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import vn.hdisoft.hdilib.utils.LogUtils;


public abstract class NiftyBaseFragment extends Fragment implements OnClickListener{

    public static Bundle commonParams = new Bundle();

    public static final int FRAGMENT_TYPE_ROOT = 0;
    public static final int FRAGMENT_TYPE_MASTER = 1;
    public static final int FRAGMENT_TYPE_DETAIL = 2;

    public int FragmentType = 0;
	private int _layoutResId = 0;

    public String titleFragment = "";
	private static String targetFragmentTag;

    public Fragment PreviousFragment;
	protected ViewGroup mWindowView;
    ViewGroup rootView;
	
	public void onFragmentVisble() {
		if(targetFragmentTag != null){
			if(!getTag().equalsIgnoreCase(targetFragmentTag)){
				getActivity().onBackPressed();
			}
		}else{
//			super.onFragmentVisble();
		}
        setUpActionBar();
	}

    public void setCommonBooleanParam(String key,boolean value){
        commonParams.putBoolean(key,value);
    }

    public boolean getCommonBooleanParam(String key){
        return commonParams.getBoolean(key,false);
    }

    public void setCommonIntParam(String key,int value){
        commonParams.putInt(key,value);
//        if(key.equalsIgnoreCase(NiftyCommonKey.LOGIN_FROM)){
//            NiftyApplication.flowLogin = value;
//        }
    }

    public int getCommonIntParam(String key){
        return commonParams.getInt(key,-1);
    }
	
	public int getLayoutResId(){
		return 0;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		targetFragmentTag = null;
		mWindowView = (ViewGroup)(getActivity().getWindow().getDecorView().getRootView());

	}

    private int maxBottom = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		_layoutResId = getLayoutResId();
		
		if(_layoutResId > 0){
			rootView = (ViewGroup) inflater.inflate(_layoutResId, container, false);
		}else{
			rootView = new FrameLayout(getActivity());
		}
		
		rootView.setBackgroundColor(Color.WHITE);
		rootView.setClickable(true);

        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(maxBottom == 0){
                    maxBottom = bottom;
                }

                int height = oldBottom - bottom;
//                LogUtils.debug("on layout changed root view!"+bottom + ",old " +oldBottom + ", distance:"+height);
                if(bottom != maxBottom){
//                if(height > 0){
                    if(isKeyboardShow == false)  onKeyboardShow();
                }else {
                    if(isKeyboardShow) onKeyboardHide();
                }
            }
        });
		
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onSetupLayout();
        displayData();
	}

    public void displayData(){

    }

	public void onSetupLayout(){
		
	}

	
	public void setOnClickForViewId(int id){
		
		if(rootView.findViewById(id) == null) return;
		rootView.findViewById(id).setOnClickListener(this);
	}

    public void onClickViewId(int id,OnClickListener onClickListener){

        if(rootView.findViewById(id) == null) return;
        rootView.findViewById(id).setOnClickListener(onClickListener);
    }
	
	public String getInputText(int id){
		EditText edt = (EditText)rootView.findViewById(id);
		if(edt != null){
			return edt.getText().toString().trim();
		}
		return "";
	}

	public void showOKAlert(String title, String msg, DialogInterface.OnClickListener onClickListener){

		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", onClickListener);
		alertDialog.show();
	}

	public void showOKAlert(String msg, DialogInterface.OnClickListener onClickListener){

		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", onClickListener);
		alertDialog.show();
	}


	public void showOKAler(String msg){
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle(null);
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	}
	
	public void showOKAlert(String title,String msg){
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	}
	
	public void showOKAler(int msgId){
		
	}
	
	public void setActionBarTitle(String text){
		
	}
	//TODO This method maby not correct,NEED CHECK CAREFULLY,CAUSE BY RESTART DEVICE
	public void backToFragment(String tag){
        LogUtils.error("***back to:"+tag);
		FragmentManager fragmentManager = getFragmentManager();
	    if(fragmentManager == null) return;
	    if(fragmentManager.getBackStackEntryCount() == 0) return;
        if(getClass().getName().equalsIgnoreCase(tag)){
            LogUtils.debug("found current fragment target:"+tag);
            return;
        }

	    int count = fragmentManager.getBackStackEntryCount();
        int index = 0;


	    while(true){
	    	String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
	    	if(fragmentManager.getBackStackEntryCount()==0) break;
	    	
	    	if(!fragmentTag.equalsIgnoreCase(tag)){
	    		getActivity().onBackPressed();
	    	}else{
                LogUtils.debug("found fragment target:"+tag);
	    		return;
	    	}
            index++;
            if(index >= count) {
                LogUtils.error("not found fragment target:"+tag);
                getActivity().onBackPressed();
                break;
            }
	    }
	    
	}
	
	@Override
	public void onPause() {
		closeKeyboard();
		super.onPause();
	}

    public void setUpActionBar(){
        switch (FragmentType){
            case FRAGMENT_TYPE_ROOT:
                break;
            case FRAGMENT_TYPE_MASTER:
                setUpActionBarMaster();
                break;
            case FRAGMENT_TYPE_DETAIL:
                break;
        }
    }
//
//    public MainActivitySlideLayout getMainActity(){
//        if(getActivity() instanceof MainActivitySlideLayout){
//            MainActivitySlideLayout activity = (MainActivitySlideLayout)getActivity();
//            return activity;
//        }else{
//            return null;
//        }
//    }

    public void setUpActionBarUpdate(OnClickListener onSave){
        if(!(getActivity() instanceof AppCompatActivity)) return;

//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.show();
//
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setElevation(0);
//        View v = getViewFromLayoutRes(R.layout.actionbar_update);
//
//        v.findViewById(R.id.buttonMenu).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(onFragmentBackPress()){
//                    if(getActivity() != null){
//                        getActivity().onBackPressed();
//                    }
//
//                }
//
//            }
//        });
//        v.findViewById(R.id.lnSave).setOnClickListener(onSave);
//
//        ((TextView)v.findViewById(R.id.tvTitleText)).setText(titleFragment);
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
//
//        actionBar.setCustomView(v, layoutParams);
//        actionBar.setDisplayShowCustomEnabled(true);
//
//        Toolbar parent = (Toolbar) v.getParent();
//        parent.setContentInsetsAbsolute(0, 0);
    }

    public void setUpActionBarMaster(){

//        ActionBar actionBar = getMainActity().getSupportActionBar();
//
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setElevation(0);
//        View v = getViewFromLayoutRes(R.layout.actionbar_update);
//
//        v.findViewById(R.id.buttonMenu).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getMainActity().onBackPressed();
//            }
//        });
//
//        v.findViewById(R.id.lnSave).setVisibility(View.GONE);
//
//        ((TextView)v.findViewById(R.id.tvTitleText)).setText(titleFragment);
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
//
//        actionBar.setCustomView(v, layoutParams);
//        actionBar.setDisplayShowCustomEnabled(true);
//
//        Toolbar parent = (Toolbar) v.getParent();
//        parent.setContentInsetsAbsolute(0, 0);
    }

	public void setUpActionBarUpdateWithClose(OnClickListener onSave){
		if(!(getActivity() instanceof AppCompatActivity)) return;

//		ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//		actionBar.show();
//
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setElevation(0);
//
//		View v = getViewFromLayoutRes(R.layout.actionbar_update_close);
//
//		v.findViewById(R.id.buttonMenu).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(onFragmentBackPress()){
//					if(getActivity() != null){
//						getActivity().onBackPressed();
//					}
//
//				}
//
//			}
//		});
//		v.findViewById(R.id.lnSave).setOnClickListener(onSave);
//
//		((TextView)v.findViewById(R.id.tvTitleText)).setText(titleFragment);
//		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
//
//		actionBar.setCustomView(v, layoutParams);
//		actionBar.setDisplayShowCustomEnabled(true);
//
//		Toolbar parent = (Toolbar) v.getParent();
//		parent.setContentInsetsAbsolute(0, 0);
	}

	public void setUpActionBarWithClose(){
		if(!(getActivity() instanceof AppCompatActivity)) return;

//		ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//		actionBar.show();
//
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setElevation(0);
//		View v = getViewFromLayoutRes(R.layout.actionbar_update_close);
//
//		v.findViewById(R.id.buttonMenu).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(onFragmentBackPress()){
//					if(getActivity() != null){
//						getActivity().onBackPressed();
//					}
//
//				}
//
//			}
//		});
//		v.findViewById(R.id.lnSave).setVisibility(View.GONE);
//
//		((TextView)v.findViewById(R.id.tvTitleText)).setText(titleFragment);
//		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
//
//		actionBar.setCustomView(v, layoutParams);
//		actionBar.setDisplayShowCustomEnabled(true);
//
//		Toolbar parent = (Toolbar) v.getParent();
//		parent.setContentInsetsAbsolute(0, 0);
	}

    public void setupActionBarView(ViewGroup view) {

//        if(!(getActivity() instanceof AppCompatActivity)) return;
//        if(getMainActity() != null){
//            getMainActity().viewActionBar = view;
//        }
//
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.show();
//
////        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setElevation(0);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT,
//                android.app.ActionBar.LayoutParams.MATCH_PARENT);
//
//        actionBar.setCustomView(view, layoutParams);
//        actionBar.setDisplayShowCustomEnabled(true);
//
//        Toolbar parent = (Toolbar) view.getParent();
//        parent.setContentInsetsAbsolute(0, 0);

    }


	public void hideAcionBar(){
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if(act != null){
            act.getSupportActionBar().hide();
        }
    }

    public void onActionBarClick(int viewId){

    }

    @Override
    public void onClick(View view) {

    }

    public boolean onFragmentBackPress(){
        return true;
    }

    public void setTextViewText(int textviewId,String text){
        TextView tv = (TextView) rootView.findViewById(textviewId);
        if(tv !=null){
            tv.setText(text);
        }
    }

    public void setVisivleById(int viewId,int visible){
        View v = rootView.findViewById(viewId);
        if(v == null) return;

        v.setVisibility(visible);
    }

	public void setClickableById(int viewId,boolean clickable){
		View v = rootView.findViewById(viewId);
		if(v == null) return;

		v.setClickable(clickable);
	}

    private boolean isKeyboardShow = false;
    public void onKeyboardShow(){
        isKeyboardShow = true;
    }

    public void onKeyboardHide(){
        isKeyboardShow = false;
    }

    public void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(rootView != null){
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        }
    }

    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
    }
}
