package vn.hdisoft.hdilib.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import vn.hdisoft.hdilib.R;
import vn.hdisoft.hdilib.fragments.HDiFragment;
import vn.hdisoft.hdilib.helpers.ViewHelper;
import vn.hdisoft.hdilib.utils.LogUtils;


public abstract class HDiCompatActivity extends AppCompatActivity {

	public static Bundle CommonBundle = new Bundle();
	
	private int _fragmentContainerId;
	private HDiFragment _rootFragment;
	
	private String _lastFragmentClass = "";
	
	private View _viewLoading;
	private View _viewActionBar;
	private int titleViewId;

    public FrameLayout contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
		_fragmentContainerId = R.id.fragContainer;
        contentView = (FrameLayout) findViewById(R.id.fragContainer);
		
		getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				LogUtils.info("addOnBackStackChangedListener");
				HDiFragment fg = (HDiFragment)getCurrentFragment();
		    	if(fg != null){
		    		fg.onFragmentVisble();
		    		_lastFragmentClass = fg.getClass().getName();
		    	}else{
		    		_rootFragment.onFragmentVisble();
		    		_lastFragmentClass = _rootFragment.getClass().getName();
		    	}
			}
		});


	}

    @Override
    protected void onResume() {
        super.onResume();
        HDiFragment fg = (HDiFragment)getCurrentFragment();
        if(fg != null){
            fg.onFragmentVisble();
        }
    }

    public HDiFragment getRootFragment(){
		return _rootFragment;
	}

	public Fragment createFragment(Class clsClass, Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = Fragment.instantiate(this, clsClass.getName(), bundle);
        fragmentTransaction.replace(_fragmentContainerId, fragment ,clsClass.getName());
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;

    }
	public void pushFragment(HDiFragment fg, boolean isUpDown){

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(isUpDown) {
			ft.setCustomAnimations(R.anim.push_in_bottom, R.anim.push_out_bottom, R.anim.slide_in_bottom, R.anim.push_out_bottom);
		} else {
			ft.setCustomAnimations(R.anim.push_in_right, R.anim.push_out_right, R.anim.slide_in_right, R.anim.push_out_right);
		}
		ft.add(_fragmentContainerId, fg, fg.getClass().getName());
		ft.addToBackStack(fg.getClass().getName());
		ft.commitAllowingStateLoss();
		_lastFragmentClass = fg.getClass().getName();

	}
	
	public void pushFragment(HDiFragment fg){
		pushFragment(fg, false);
	}

	public void dropFragment(HDiFragment fg){
		if(_lastFragmentClass.equalsIgnoreCase(fg.getClass().getName())){
			return;
		}
		
		_lastFragmentClass = fg.getClass().getName();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		ft.setCustomAnimations(R.anim.drop_in, R.anim.drop_out, R.anim.drop_in, R.anim.drop_out);
		
		ft.setCustomAnimations(R.anim.drop_in, R.anim.drop_out);
		ft.add(_fragmentContainerId, fg, fg.getClass().getName());
		ft.addToBackStack(fg.getClass().getName());
		ft.commitAllowingStateLoss();				
		
	}

	public void popFragment(HDiFragment fg){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		fragmentManager.popBackStack();
		ft.remove(fg);
		if(getCurrentFragment() != null){
			_lastFragmentClass = getCurrentFragment().getClass().getName();
		}else{
			_lastFragmentClass = "";
		}
		
		ft.commit();
	}
	
	public void replaceFragment(HDiFragment fg, boolean allowAnimation){
		_rootFragment = fg;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(allowAnimation){
			ft.setCustomAnimations(R.anim.push_in_right, R.anim.push_out_right, R.anim.slide_in_right, R.anim.push_out_right);
		}

		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
		    fm.popBackStack();
		}
		ft.replace(_fragmentContainerId, fg, fg.getClass().getName());
		ft.commit();
	}
	
	public void setRootFragment(HDiFragment fg){
		_rootFragment = fg;
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(_fragmentContainerId, fg ,fg.getClass().getName());
		fg.isRootFragment = true;
		fragmentTransaction.commit();
	}
	
	public void setRootFragment(HDiFragment fg, boolean canBack){
		_rootFragment = fg;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(_fragmentContainerId, fg ,fg.getClass().getName());
		fg.isRootFragment = true;
		if(canBack) ft.addToBackStack(fg.getClass().getName());
		
		ft.commit();
	}
	
	public void setRootFragment(HDiFragment fg, int fragId){
		_rootFragment = fg;
		_fragmentContainerId = fragId;
		fg.isRootFragment = true;
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(_fragmentContainerId, fg ,fg.getClass().getName());
		
		fragmentTransaction.commit();
	}
	
	public void addFragment(HDiFragment fg, int fragId){
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(fragId, fg ,fg.getClass().getName());
		
		ft.commit();
	}
	
	public View getViewFromLayoutRes(int layoutRes){
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(layoutRes, null);
		return v;
	}
	
	public void setOnClickForViewRes(int layoutRes,OnClickListener listener){
		View v = findViewById(layoutRes);
		if(v != null){
			v.setOnClickListener(listener);
		}
	}
	
	public void setOnClickForViewRes(View parrent, int layoutRes, OnClickListener listener){
		View v = parrent.findViewById(layoutRes);
		if(v != null){
			v.setOnClickListener(listener);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LogUtils.info("onBackPressed");

		HDiFragment fg = (HDiFragment)getCurrentFragment();
		if(fg != null){
			LogUtils.info("Current fragment:"+fg.getClass().getName() + ", isRoot:" + fg.isRootFragment);
			if(fg.onBackPressFragment()){

			}
			else {

			}
    	}

		super.onBackPressed();

		overridePendingTransition(R.anim.nothing, R.anim.push_out_right);
	}
	
	public void setFullScreen(boolean isFullScreen){
		if(isFullScreen){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}		
	}
	/*==========ACTIVITY METHODS==========*/
	public void startActivity(Class<?> act){
		Intent intent = new Intent(this,act);
		startActivity(intent);
	}
	public void pushActivity(Class<?> act){
		Intent intent = new Intent(this,act);
		startActivity(intent);
		overridePendingTransition(R.anim.push_in_right, R.anim.nothing);
	}
    public void pushActivityWithParams(Class<?> act, Object object){
        Intent intent = new Intent(this,act);
        startActivity(intent);
        overridePendingTransition(R.anim.push_in_right, R.anim.nothing);
    }

    public void pushActivityWithIntParams(Class<?> act, int param){
        Intent intent = new Intent(this,act);
        intent.putExtra("int_param",param);
        startActivity(intent);
        overridePendingTransition(R.anim.push_in_right, R.anim.nothing);
    }

	public void pushActivity(Intent intent){
		startActivity(intent);
		overridePendingTransition(R.anim.push_in_right, R.anim.nothing);
	}
	
	public void pushActivityForResult(Class<?> act, int requestCode){
		Intent intent = new Intent(this,act);
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.push_in_right, R.anim.nothing);
	}
	
	public void pushActivityForResult(Intent intent, int requestCode){
		startActivityForResult(intent,requestCode);
		overridePendingTransition(R.anim.push_in_right, R.anim.nothing);
	}
	public int getIntParamFromExtras(){
        return getIntent().getIntExtra("int_param",0);
    }
	/*==========*/
	
	public void showLoading(){
//		_viewLoading = getViewFromInflaterRes(R.layout.view_loading);
//	    LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//	    ViewGroup vg = (ViewGroup)(getWindow().getDecorView().getRootView());
//	    vg.addView(_viewLoading, layoutParams);
	}
	
	public void hideLoading(){
		ViewGroup vg = (ViewGroup)(getWindow().getDecorView().getRootView());
		if (_viewLoading != null && vg != null)
		vg.removeView(_viewLoading);
	}
	
	public View getViewFromInflaterRes(int viewId){
		LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(viewId, null);
		return v;
	}
	
	public Fragment getCurrentFragment(){
	    FragmentManager fm = getSupportFragmentManager();
	    if(fm == null) return null;
	    if(fm.getBackStackEntryCount() == 0) return _rootFragment;
	    
	    for(int i= 0;i<fm.getBackStackEntryCount();i++){
	    	LogUtils.info("- "+fm.getBackStackEntryAt(i).getName());
	    }
	    
	    String fragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
		LogUtils.debug("current fragment:"+fragmentTag);
	    if(fragmentTag == null) return null;
	    Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
	    return currentFragment;
	}

    public void setupActionBarLayout(int layoutId){
        View v = getViewFromInflaterRes(layoutId);
        setupActionBarView(v);
    }
	
	 public void setupActionBarView(View viewActionBar){
		_viewActionBar = viewActionBar;
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setElevation(0);

		actionBar.setDisplayShowCustomEnabled(true);

		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
		ActionBar.LayoutParams.MATCH_PARENT,
		ActionBar.LayoutParams.MATCH_PARENT);

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);

		actionBar.setCustomView(_viewActionBar,params);
		Toolbar parent = (Toolbar) viewActionBar.getParent();
		parent.setContentInsetsAbsolute(0, 0);

		parent.getContentInsetEnd();
		parent.setPadding(0, 0, 0, 0);
		actionBar.show();
	 }
	 
	 public View getActionBarView(){
		 return _viewActionBar;
	 }
	 
	 
	 public void setActionBarTitleViewId(int viewId){
		 titleViewId = viewId;
	 }
	 
	 public void setActionBarTitle(String title){
		 ViewHelper.setText(_viewActionBar, titleViewId, title);
	 }

	public void backToFragment(String fragmentTag){
		FragmentManager fm = getSupportFragmentManager();
		if(fm == null) return;
		if(fm.getBackStackEntryCount() == 0) return;

		boolean isFound = false;

		for(int i=fm.getBackStackEntryCount()-1;i>=0;i--){
			String tag = fm.getBackStackEntryAt(i).getName();
			LogUtils.info("check tag:"+tag);
			HDiFragment fg = (HDiFragment)fm.findFragmentByTag(fragmentTag);
			if(!tag.equalsIgnoreCase(fragmentTag)){
				if(fg != null) fg.popFragment();
			}else{
				isFound = true;
				LogUtils.info("found tag:"+fragmentTag);
				break;
			}
		}
		if(!isFound){
			//creare fragment from tag
			LogUtils.error("Not foud:"+fragmentTag);
		}
	}

	public void setUpMainActionBar(){

	}

    public void setActionBarItemClick(int viewId,OnClickListener onClick){
        if(_viewActionBar == null) return;
        vn.hdisoft.hdilib.helpers.ViewHelper.setOnClick(_viewActionBar,viewId,onClick);
    }


}
