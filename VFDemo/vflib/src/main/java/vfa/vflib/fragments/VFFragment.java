package vfa.vflib.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import vfa.vflib.activity.VFActivity;

public abstract class VFFragment extends Fragment{
    public ViewGroup rootView;
    private int _rootLayoutId = 0;
    private int fragmentBGColor = Color.WHITE;

    public abstract int onGetRootLayoutId();

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

        rootView.setBackgroundColor(fragmentBGColor);
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
}
