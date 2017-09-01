package vn.hdisoft.hdilib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import vn.hdisoft.hdilib.R;
import vn.hdisoft.hdilib.fragments.VFFragment;

public class VFActivity extends AppCompatActivity {

    private int fragmentContainerId = 0;
    private Fragment _rootFragment;

    private ViewGroup viewActionBar;
    private ViewGroup viewActionBarRoot;

    private static Bundle bundleParams = new Bundle();

    public boolean hasParams = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                Fragment fg = getCurrentFragment();
                if(fg != null && fg instanceof VFFragment){
                    ((VFFragment)fg).onFragmentVisible();
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setActionBarViewContent(int viewId){
        if(viewActionBar == null) return;
        viewActionBarRoot = (ViewGroup) viewActionBar.findViewById(viewId);
    }
    public void replaceActionBar(int viewId){
        if(viewActionBarRoot == null) return;
        viewActionBarRoot.removeAllViews();
        viewActionBarRoot.addView(getLayoutInflater().inflate(viewId,null));
    }
    public void setupActionBarView(int viewId) {
        setupActionBarView((ViewGroup) getLayoutInflater().inflate(viewId,null));
    }

    public void setupActionBarView(ViewGroup view) {
        viewActionBar = view;
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setElevation(0);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams layoutParams =
                new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT,
                        android.app.ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(view, layoutParams);
        actionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        parent.setPadding(0, 0, 0, 0);
    }

    public void setActionBarOnClick(int viewId, View.OnClickListener onClick){
        if(viewActionBar == null) return;
        View v = viewActionBar.findViewById(viewId);
        if(v != null) v.setOnClickListener(onClick);
    }

    public void setHomeActionBar(){

    }
    /*==== FRAGMENT METHODS=====*/
    public Fragment getCurrentFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager == null) return null;
        if(fragmentManager.getBackStackEntryCount() == 0) return null;

        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        return currentFragment;
    }
    public Fragment getRootFragment(){
        return _rootFragment;
    }
    public void setFragmentContainerId(int frcontainer){
        fragmentContainerId = frcontainer;
    }
    public void setRootFragment(Fragment fg){
        setRootFragment(fg,fragmentContainerId);
    }

    public void setRootFragment(Class fgClass){
        Fragment fg = Fragment.instantiate(this, fgClass.getName(), null);
        _rootFragment = fg;
        setRootFragment(fg,fragmentContainerId);
    }

    public void setRootFragment(Fragment fg,int frcontainerId){
        _rootFragment = fg;
        fragmentContainerId = frcontainerId;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        fragmentTransaction.addToBackStack(fg.getClass().getName());

        fragmentTransaction.replace(fragmentContainerId, fg ,fg.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
        if(fg instanceof VFFragment){

        }
    }

    public void pushFragment(Class fgClass){
        Fragment fg = Fragment.instantiate(this, fgClass.getName(), null);
        pushFragment(fg);
    }

    public void pushFragment(Fragment fg){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = fg.getClass().getName();
        fragmentTransaction.setCustomAnimations(R.anim.push_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.push_out_right);


        fragmentTransaction.add(fragmentContainerId, fg, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void startFragment(Fragment fg,int containerId){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = fg.getClass().getName();

        fragmentTransaction.replace(containerId, fg, tag);
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void addViewIntoFragmentContainer(View view){
        if(fragmentContainerId == 0) return;
        ViewGroup container = (ViewGroup) findViewById(fragmentContainerId);
        container.addView(view);
    }

    /*==== ACTIVITY METHODS=====*/
    public void startActivity(Class<?> act){
        Intent intent = new Intent(this,act);
        startActivity(intent);
    }

    public void startActivityWithString(Class<?> act,String params){
        Intent intent = new Intent(this,act);
        startActivity(intent);
    }


    public void pushActivity(Class<?> act){
        Intent intent = new Intent(this,act);
        startActivity(intent);
//        overridePendingTransition(R.anim.push_in_right,R.anim.nothing);
    }

    public void setPublicString(String key,String value){
        bundleParams.putString(key,value);
    }
    public String getPublicString(String key){
        return bundleParams.getString(key,"");
    }
}
