package vfa.vflib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import vfa.vflib.R;

public class VFActivity extends AppCompatActivity {

    private int fragmentContainerId = 0;

    private Fragment _rootFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupActionBarView(ViewGroup view) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setElevation(0);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        android.support.v7.app.ActionBar.LayoutParams layoutParams =
                new android.support.v7.app.ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT,
                        android.app.ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(view, layoutParams);
        actionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        parent.setPadding(0, 0, 0, 0);
    }

    /*==== FRAGMENT METHODS=====*/
    public void setFragmentContainerId(int frcontainer){
        fragmentContainerId = frcontainer;
    }
    public void setRootFragment(Fragment fg){
        setRootFragment(fg,fragmentContainerId);
    }

    public void setRootFragment(Fragment fg,int frcontainerId){
        fragmentContainerId = frcontainerId;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(fragmentContainerId, fg ,fg.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
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


    /*==== ACTIVITY METHODS=====*/
    public void startActivity(Class<?> act){
        Intent intent = new Intent(this,act);
        startActivity(intent);
    }
    public void pushActivity(Class<?> act){
        Intent intent = new Intent(this,act);
        startActivity(intent);
//        overridePendingTransition(R.anim.push_in_right,R.anim.nothing);
    }
}
