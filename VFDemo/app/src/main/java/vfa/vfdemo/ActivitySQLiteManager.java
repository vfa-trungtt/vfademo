package vfa.vfdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import vfa.vfdemo.fragments.sql.FragOpenDb;


public class ActivitySQLiteManager extends ActivitySlideMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootFragment(new FragOpenDb());
    }

    @Override
    public void onBeforeSetupActionBar() {
        this.title += "SQLite Database";
    }
}
