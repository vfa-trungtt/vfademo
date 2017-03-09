package vfa.vfdemo.fragments.sql;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.fragments.FragBaseListView;

public class FragRowDetail extends FragBaseListView<String> {
    public RowEntity rowData;
    public List<String> cols;

    @Override
    public int getItemLayoutId() {
        return R.layout.item_rowdata;
    }

    @Override
    public void onViewLoaded() {
        super.onViewLoaded();
    }

    @Override
    public List<String> getDataSource() {
        return rowData.dataRow;
    }

    @Override
    public void onBindItemList(int pos, String entity, View view) {
        EditText edt = (EditText) view.findViewById(R.id.edtValue);
        edt.setText(""+entity);

        TextView tv = (TextView)view.findViewById(R.id.tvColTitle);
        tv.setText(cols.get(pos));
    }
}
