package vn.hdisoft.hdilib.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import vn.hdisoft.hdilib.activities.HDiCompatActivity;
import vn.hdisoft.hdilib.utils.LogUtils;

public abstract class HDiFragment extends Fragment implements OnClickListener {

	public String TAG = getClass().getSimpleName();
	
	public interface OnFragmentRemoveListener{
		public void onRemove(int resultCode);
	}
	
	public static int RESULT_OK 	= 100;
	public static int RESULT_CANCEL = 101;
	
	public ViewGroup rootView;
	public boolean isRootFragment = false;
	
	private int _layoutRes;
	public boolean isLayoutXml = false;

	private String _tilte = "";
	
	public HDiFragment parrentFragment;
	
	private OnFragmentRemoveListener _listener;
	private int _resultCode;
	
	public OnClickListener rightButtonClick;
	
	public abstract int onGetLayoutResId();
	public abstract void onSetupLayout();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isRootFragment) onFragmentVisble();
	}

	@Override
	public void onDestroy() {
		if(_listener != null){
			_listener.onRemove(_resultCode);
		}
		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_layoutRes 	= onGetLayoutResId();
		if(_layoutRes == 0){
            isLayoutXml = false;
			rootView = new FrameLayout(getActivity());
			rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rootView.setBackgroundColor(Color.WHITE);

        }else{

            isLayoutXml = true;
			rootView 	= (ViewGroup)inflater.inflate(_layoutRes, container, false);
			
			Drawable bg = rootView.getBackground();
			if(bg == null){
				rootView.setBackgroundColor(Color.WHITE);
			}
			
		}				
		
		rootView.setClickable(true);		
		onSetupLayout();
		
		return rootView;		
	}

    public void setSemiTransparentBackGound(){
        int  clr = Color.argb(0,0,0,0);
        rootView.setBackgroundColor(clr);
    }

	public void setSemiTransparentBackGound(int opacity){
		int  clr = Color.argb(opacity,0,0,0);
		rootView.setBackgroundColor(clr);
	}

    public void setContentView(int viewId){
        rootView = (ViewGroup)getViewFromInflaterRes(viewId);
    }

    public void setContentView(View contentView){
        rootView = (ViewGroup) contentView;
    }

    public void setTitle(String title){
        _tilte = title;
    }

	public void setActionBarTitle(String title){
		_tilte = title;
		if(getActionBarActivity() != null) getActionBarActivity().setActionBarTitle(_tilte);
	}
	
	public String getTitle(){
		return _tilte;
	}
	
	public void setOnFragmentRemoveListener(OnFragmentRemoveListener listener){
		_listener = listener;
	}
	
	public void setResultCode(int resultCode){
		_resultCode = resultCode;
	}
	
	public void setOnClickForViewResId(int viewRes){
		View v = rootView.findViewById(viewRes);
		if(v != null){
			v.setOnClickListener(this);
		}
	}
	public void setOnClickForViewResId(int viewRes,OnClickListener onClick){
		View v = rootView.findViewById(viewRes);
		if(v != null){
			v.setOnClickListener(onClick);
		}
	}
	
	@Override
	public void onClick(View v) {

	}

    public void backToRootFragment(){

    }

    public void backToFragment(String className){

    }

	public HDiCompatActivity getHDiActivity(){
		if(getActivity() instanceof HDiCompatActivity){
			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
			return activity;
		}
		return null;
	}
	public void pushFragment(HDiFragment frag){

		if(getActivity() instanceof HDiCompatActivity){
			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
			activity.pushFragment(frag);
		}
	}

	public void pushFragment(HDiFragment frag, boolean isUpDown){
		if(getActivity() instanceof HDiCompatActivity){
			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
			activity.pushFragment(frag, isUpDown);
		}
	}


	
	public void dropFragment(HDiFragment frag){
		if(getActivity() instanceof HDiCompatActivity){
			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
			activity.dropFragment(frag);
		}
	}
	
	public void replaceFragment(HDiFragment frag){
		if(getActivity() instanceof HDiCompatActivity){
			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
			activity.replaceFragment(frag, true);
		}
		
	}
	
	public void popFragment(){
        hideKeyboard();
        getActivity().onBackPressed();

//		if(getActivity() instanceof HDiCompatActivity){
//			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
//			activity.popFragment(this);
//		}
		
	}
	
	public void showFragment(HDiFragment frag){
		if(getActivity() instanceof HDiCompatActivity){
			HDiCompatActivity activity =(HDiCompatActivity)getActivity();
			activity.setRootFragment(frag);
		}
		
	}
	
	public void hideActionBar(){
		((HDiCompatActivity)getActivity()).getSupportActionBar().hide();
	}
	public void showActionBar(){
		((HDiCompatActivity)getActivity()).getSupportActionBar().show();
	}
	
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int viewId) {
		return (V) rootView.findViewById(viewId);
	}

	
	public void onFragmentVisble(){
		LogUtils.info("onFragmentVisble - "+this.getClass().getName());
		hideKeyboard();

        onSetUpActionBar();
	}

    public void onSetUpActionBar(){

    }

	public void hideKeyboard(){
		if (rootView != null) {
			try {
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		}
	}

	public View getViewFromInflaterRes(int viewId){
		LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(viewId, null);
		return v;
	}
	

    public void setActionBarLayout(int layoutId){
        getActionBarActivity().setupActionBarLayout(layoutId);
    }

    public void setActionBarItemClick(int viewId,OnClickListener onClickListener){
        getActionBarActivity().setActionBarItemClick(viewId,onClickListener);
    }

	public Bundle getShareBundle(){
		if(getActivity() instanceof HDiCompatActivity){
			return HDiCompatActivity.CommonBundle;
		}
		return null;
	}
	
	public void setNeedReloadAfterPop(boolean value){
		if(getShareBundle() != null){
			getShareBundle().putBoolean("need_reload",false);
		}
	}

	public void setText(int id,String text){
		TextView tv = findView(id);
		if(tv != null && text != null){
			tv.setText(text.toString().trim());
		}
	}
	
	public String getTextValue(int id){
		TextView tv = findView(id);
		if(tv != null){
			return tv.getText().toString().trim();
		}
		return "";
	}

	public HDiCompatActivity getActionBarActivity(){
		return (HDiCompatActivity)getActivity();
	}
	
	public void setBackGroundColorRes(int colorId){
		rootView.setBackgroundColor(ContextCompat.getColor(getActivity(), colorId));
	}

	public int getOffset(int page, int limit) {
		int offset = (page - 1) * limit;
		if (offset < 0)
			offset = 0;
		return offset;
	}


	public boolean onBackPressFragment(){
		return true;
	}

	public void addView(View v){
		if(v != null){
			rootView.addView(v);
		}
	}

	public void setChildFragment(Fragment fg, int containerId){
		FragmentManager fm = getChildFragmentManager();
		fm.beginTransaction().replace(containerId,fg).commitNow();

	}
}
