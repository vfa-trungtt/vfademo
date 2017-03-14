package vfa.vfdemo.fragments.nifty;

import android.view.View;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.fragments.FragBaseListView;
import vfa.vfdemo.viewadapter.VFSimpleItemListView;
import vfa.vflib.utils.LogUtils;


public class FragListPublicSchool extends FragBaseListView<SchoolEntity> {
    List<SchoolEntity> schools = new ArrayList<>();

    @Override
    public List<SchoolEntity> getDataSource() {
        return schools;
    }

    @Override
    public void onViewLoaded() {
        super.onViewLoaded();
        queryPublicSchool();
    }

    private void queryPublicSchool(){

        NCMBQuery<NCMBObject> query = new NCMBQuery<NCMBObject>("SchoolClass");
        query.setLimit(900);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                if(e == null){
                    LogUtils.debug("found:"+list.size()+" public school");
                    schools = SchoolEntity.getListFromQuery(list);
                    setDataSource(schools);

                }else {

                    LogUtils.error(e);
                }
            }
        });

    }

    @Override
    public void onBindItemList(int pos, SchoolEntity entity, View view) {
        if(view instanceof VFSimpleItemListView){
            VFSimpleItemListView itemListView = (VFSimpleItemListView)view;
            itemListView.setText(entity.SchoolName);
        }
    }
}
