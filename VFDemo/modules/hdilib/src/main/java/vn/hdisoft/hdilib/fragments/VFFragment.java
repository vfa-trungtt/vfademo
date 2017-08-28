package vn.hdisoft.hdilib.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import vn.hdisoft.hdilib.activities.VFActivity;

public abstract class VFFragment extends Fragment{
    public ViewGroup rootView;
    private int _rootLayoutId = 0;
    private int fragmentBGColor = Color.WHITE;

    public int onGetRootLayoutId(){
        return 0;
    }

    public abstract void onViewLoaded();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        _rootLayoutId = onGetRootLayoutId();

        if(_rootLayoutId > 0){
            rootView = (ViewGroup) inflater.inflate(_rootLayoutId, container, false);
        }else{
            rootView = new FrameLayout(getActivity());
        }

        if(rootView.getBackground() == null){
            rootView.setBackgroundColor(fragmentBGColor);
        }

        rootView.setClickable(true);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewLoaded();
    }

    public void pushFragment(Fragment fg){
        if(getActivity() instanceof VFActivity){
            VFActivity act = (VFActivity)getActivity();
            act.pushFragment(fg);
        }
    }

    public VFActivity getVFActivity(){
        if(getActivity() instanceof VFActivity){
            VFActivity act = (VFActivity)getActivity();
            return act;
        }
        return null;
    }

    public boolean onBackPress(){
        return true;
    }

    public void addContentView(int viewId){
//        View v = getI
    }

    public void addContentView(View view){
//        rootView.removeAllViews();
        rootView.addView(view);
    }

    public void onFragmentVisible(){
        setUpActionBar();
    }
    public void setUpActionBar(){

    }

    public void setClickListener(int viewId, View.OnClickListener onClick){
        View v = rootView.findViewById(viewId);
        if(v != null){
            v.setOnClickListener(onClick);
        }
    }
}
