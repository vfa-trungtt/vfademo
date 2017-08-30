package vfa.vfdemo.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vn.hdisoft.hdilib.fragments.VFFragment;


public abstract class FragBaseListView<E> extends VFFragment{

    public ListView listView;
    BaseArrayAdapter adapter;
    List<E> _data = new ArrayList<>();

    @Override
    public int onGetRootLayoutId() {
        return 0;
    }

    @Override
    public void onViewLoaded() {
        if(onGetRootLayoutId() == 0){
            listView = new ListView(getContext());
            rootView.addView(listView);

        }else {
            if(getListViewId() > 0){
                listView = (ListView) rootView.findViewById(getListViewId());
            }

        }

        if(listView != null){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    E entity = _data.get(position);
                    onClickItemList(position,entity);
                }
            });

            reloadData();
            displayListView();
        }


    }


    private void displayListView(){
//        if(_data == null) return;{
//            E entity = _data.get(pos);
//            onBindItemList(pos,entity,v);
//        }

//        adapter = new BaseArrayAdapter(getContext(),_data) {
//            @Override
//            public int onGetItemLayoutId() {
//                return getItemLayoutId();
//            }
//
//            @Override
//            public void bindItem(int pos, View v)
//        };
//        listView.setAdapter(adapter);
    }

    public abstract List<E> getDataSource();

    public int getDataSize(){
        return 0;
    }

    public int getItemLayoutId(){
        return 0;
    }

    public int getListViewId(){
        return 0;
    }

    public void reloadData(){
        _data = getDataSource();
    }

    public void refreshData(){

    }

    public void setDataSource(List<E> listData){
        _data = listData;
        displayListView();
    }

    public void onClickItemList(int pos,E entity){

    }

    public void onBindItemList(int pos,E entity,View view){

    }

}
