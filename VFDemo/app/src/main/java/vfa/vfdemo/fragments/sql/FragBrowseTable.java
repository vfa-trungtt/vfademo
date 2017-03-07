package vfa.vfdemo.fragments.sql;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.data.VFSqliteDB;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;


public class FragBrowseTable extends VFFragment {
    String dbPath = "mnt/sdcard/db/sisetu_db.sqlite";


    ColumnView viewColumn;
    int columnCount = 0;
    List<String> listColumn = new ArrayList<>();

    String tableName = "sisetu";
    ListView lvData;

    List<RowEntity> _data = new ArrayList<>();

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_browse_table;
    }

    @Override
    public void onViewLoaded() {
        viewColumn = (ColumnView) rootView.findViewById(R.id.viewColumn);
        lvData      = (ListView) rootView.findViewById(R.id.lvData);


        reloadData();


    }

    public void reloadData(){
        File file = new File(dbPath);
//        if(!file.exists()) return;

        List<String> cols = VFSqliteDB.getColsTable(dbPath,tableName);
        for (String s:cols){
            LogUtils.debug("colsView[]:"+s);
        }

        viewColumn.setColNameList(cols);
        columnCount = cols.size();

        _data = VFSqliteDB.query(dbPath,tableName);
        LogUtils.debug("data:"+_data.size());
        TableAdapter adapter = new TableAdapter(getContext(),_data);
        lvData.setAdapter(adapter);
    }


    class TableAdapter extends BaseArrayAdapter<RowEntity>{

        public TableAdapter(Context context, RowEntity[] objects) {
            super(context, objects);
        }

        public TableAdapter(Context context, List<RowEntity> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
//            return R.layout.item_row;
            return 0;
        }

        @Override
        public void bindItem(int pos, View v) {
            RowEntity row = getItem(pos);
            if(v instanceof RowView){
                RowView itemView = (RowView)v;
                itemView.setRowData(row);
            }

        }

        @Override
        public View getItemView() {
            RowView view = new RowView(getContext(),columnCount);
            return view;
        }
    }
}
