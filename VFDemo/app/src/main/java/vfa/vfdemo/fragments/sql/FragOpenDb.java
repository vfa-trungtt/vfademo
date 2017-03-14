package vfa.vfdemo.fragments.sql;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.AppSettings;
import vfa.vfdemo.R;
import vfa.vfdemo.data.VFSqliteDB;
import vfa.vfdemo.dialogs.SelectFileDialog;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

/**
 * Created by Vitalify on 3/7/17.
 */

public class FragOpenDb extends VFFragment {

    ListView lvTables;
    TextView tvDbPath;

    List<String> listTables = new ArrayList<>();
    String dbPath = "mnt/sdcard/db/sisetu_db.sqlite";

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_open_db;
    }

    @Override
    public void onViewLoaded() {

        lvTables = (ListView) rootView.findViewById(R.id.lvTables);
        tvDbPath = (TextView) rootView.findViewById(R.id.tvDbPath);
        tvDbPath.setText(dbPath);

        rootView.findViewById(R.id.btOpenDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        rootView.findViewById(R.id.btQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragment(new FragSqlQuery());
            }
        });

        lvTables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tableName = listTables.get(position);
                FragBrowseTable fg = new FragBrowseTable();
                fg.dbPath       = dbPath;
                fg.tableName    = tableName;
                pushFragment(fg);
            }
        });

        if(!TextUtils.isEmpty(AppSettings.getCurrentDBPath(getContext()))){
            dbPath = AppSettings.getCurrentDBPath(getContext());
        }

        loadTableName();
    }

    private void loadTableName(){
        File file = new File(dbPath);
        if(file.exists()){
            LogUtils.info("File exist.");
            listTables = VFSqliteDB.getListTables(getContext(),dbPath);
        }

        DataAdapter adapter = new DataAdapter(getContext(),listTables);
        lvTables.setAdapter(adapter);
    }

    public void selectFile(){
        SelectFileDialog dialog = new SelectFileDialog(getContext());
        dialog.setSelectFileListener(new SelectFileDialog.SelectFileListener() {
            @Override
            public void onSelectFile(String filePath) {
                dbPath = filePath;
                AppSettings.setCurrentDBPath(getContext(),dbPath);

                tvDbPath.setText(filePath);
                loadTableName();
            }
        });
        dialog.show();
    }

    class DataAdapter extends BaseArrayAdapter<String>{

        public DataAdapter(Context context, String[] objects) {
            super(context, objects);
        }

        public DataAdapter(Context context, List<String> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
            return R.layout.item_table;
        }

        @Override
        public void bindItem(int pos, View v) {
            TextView tv = (TextView) v.findViewById(R.id.tvTableName);
            tv.setText(getItem(pos));
        }
    }
}
