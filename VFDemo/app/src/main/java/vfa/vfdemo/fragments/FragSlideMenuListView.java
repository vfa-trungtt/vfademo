package vfa.vfdemo.fragments;

import android.view.View;

import vfa.vfdemo.activity.ActivitySlideMenu;
import vfa.vfdemo.viewadapter.VFSimpleItemListView;


/**
 * Created by Vitalify on 3/8/17.
 */

public abstract class FragSlideMenuListView extends FragBaseListView<String> {


    @Override
    public void onBindItemList(int pos, String entity, View view) {
        if(view instanceof VFSimpleItemListView){
            VFSimpleItemListView itemListView = (VFSimpleItemListView)view;
            itemListView.setText(entity);
        }
    }

    @Override
    public void onClickItemList(int pos, String entity) {
        super.onClickItemList(pos, entity);
        ActivitySlideMenu act = (ActivitySlideMenu) getActivity();
        act.toggSlideMenu();
        act.onSlideMenuSelected(pos);
    }
}
