package vn.hdisoft.hdilib.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import vn.hdisoft.hdilib.utils.LogUtils;
import vn.hdisoft.hdilib.viewadapters.BaseArrayAdapter;

/**
 * Created by TrungTT on 9/11/16.
 */

public abstract class HDiListViewFragment<E> extends HDiFragment {

    public int displayVersion = 0;
    public int updateVersion  = 0;

    public abstract List<E> onGetDataList();
    public ListView listView;

    private int _itemLayoutId = 0;

    private List<E> _data;

    public BaseAdapterList<E> adapter;

    @Override
    public int onGetLayoutResId() {
        return 0;
    }

    @Override
    public void onSetupLayout() {
        if(!isLayoutXml){
            listView = new ListView(getContext());
            addView(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onListItemClick(i);
                }
            });

            reloadData();
        }

    }

    @Override
    public void onFragmentVisble() {
        super.onFragmentVisble();
        //get update version
//        updateVersion = SqliteDb.updateVersionData;
        if(updateVersion > displayVersion){
            refreshData();
        }

    }

    public void setListViewId(int listViewId){
        listView = findView(listViewId);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemClick(i);
            }
        });
    }

    public abstract void  onListItemClick(int pos);

    public E getEntity(int pos){
        return _data.get(pos);
    }

    public void setItemLayoutId(int id){
        _itemLayoutId = id;
        reloadData();

    }

    public abstract void onDisplayViewItem(View itemView, int pos);

    public void reloadData(){
        LogUtils.info("reload data!version:"+updateVersion);
        _data = onGetDataList();

        if(_itemLayoutId > 0){
            adapter= new BaseAdapterList<>(getContext(),_data,_itemLayoutId);
        }else {
            adapter = new BaseAdapterList<E>(this,_data);
        }

        listView.setAdapter(adapter);
        displayVersion = updateVersion;
    }

    public void refreshData(){
        _data = onGetDataList();
        adapter.clear();
        adapter.addAll(_data);
        adapter.notifyDataSetChanged();

    }

    class BaseAdapterList<T> extends BaseArrayAdapter<T> {

        public BaseAdapterList(Fragment fg, List<T> list){
            super(fg.getContext(),list,0);
        }

        public BaseAdapterList(Context context, List<T> list){
            super(context,list,0);
        }

        public BaseAdapterList(Context context, List<T> objects, int itemLayoutRes) {
            super(context, objects, itemLayoutRes);
        }

        @Override
        public void onBindItem(View v, int pos) {
            onDisplayViewItem(v,pos);
        }
    }
}
