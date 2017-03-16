package vfa.vfdemo.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.ActivityBookReader;
import vfa.vfdemo.ActivityDrawing;
import vfa.vfdemo.ActivityFileBrowser;
import vfa.vfdemo.ActivityHDiApp;
import vfa.vfdemo.ActivitySQLiteManager;
import vfa.vfdemo.R;
import vfa.vfdemo.fragments.map.FragMapDemoList;


public class VFFragDemoList extends FragBaseListView<DemoEntity> {

    List<DemoEntity> listDemo = new ArrayList<>();
    @Override
    public int getItemLayoutId() {
        return R.layout.item_demo;
    }

    @Override
    public void onViewLoaded() {
        super.onViewLoaded();
    }

    @Override
    public List<DemoEntity> getDataSource() {
        return listDemo;
    }


    @Override
    public void onClickItemList(int pos, DemoEntity entity) {
        if(entity != null){

            if(entity.LauchType == 0){
                getVFActivity().pushActivity(entity.LaucherDemoClass);
            }else {
                getVFActivity().pushFragment(entity.LaucherDemoClass);
            }

        }
    }

    @Override
    public void onBindItemList(int pos, DemoEntity entity, View view) {
        super.onBindItemList(pos, entity, view);
        TextView tvTitle    = (TextView) view.findViewById(R.id.tvDemoTitle);
        TextView tvDesc     = (TextView) view.findViewById(R.id.tvDemoDesc);

        tvTitle.setText(entity.DemoTitle);
        tvDesc.setText(entity.DemoDescription);

    }

    public void setDemoListData(List<DemoEntity> data){
        listDemo = data;
    }

}
