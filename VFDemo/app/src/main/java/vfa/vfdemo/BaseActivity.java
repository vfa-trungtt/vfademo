package vfa.vfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import vfa.vfdemo.utils.ViewHelper;
import vfa.vflib.activity.VFActivity;

public class BaseActivity extends VFActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frament);
        setFragmentContainerId(R.id.frContainer);

        setHomeActionBar();
    }




    public void hideAcionBar(){
        getSupportActionBar().hide();
    }

    public void setHomeActionBar(){
        setupActionBarView(ViewHelper.getViewGroup(this,R.layout.actionbar_root));
    }
}
