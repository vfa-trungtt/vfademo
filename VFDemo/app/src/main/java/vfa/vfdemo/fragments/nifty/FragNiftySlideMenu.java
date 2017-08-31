package vfa.vfdemo.fragments.nifty;

import android.view.View;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.activity.ActivitySlideMenu;
import vfa.vfdemo.fragments.FragBaseListView;
import vfa.vfdemo.viewadapter.VFSimpleItemListView;

public class FragNiftySlideMenu extends FragBaseListView<String> {
    @Override
    public List<String> getDataSource() {
        List<String> menuSlide = new ArrayList<>();
        menuSlide.add("Save Location");
        menuSlide.add("Goto Location");

        return menuSlide;
    }

    @Override
    public void onViewLoaded() {
        super.onViewLoaded();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

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
