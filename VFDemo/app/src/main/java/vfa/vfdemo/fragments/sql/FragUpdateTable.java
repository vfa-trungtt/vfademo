package vfa.vfdemo.fragments.sql;

import java.util.ArrayList;
import java.util.List;

import vfa.vflib.fragments.VFFragment;

/**
 * Created by Vitalify on 3/14/17.
 */

public class FragUpdateTable extends VFFragment {

    List<ColumnEntity> columns = new ArrayList<>();

    @Override
    public int onGetRootLayoutId() {
        return 0;
    }

    @Override
    public void onViewLoaded() {

    }
}
