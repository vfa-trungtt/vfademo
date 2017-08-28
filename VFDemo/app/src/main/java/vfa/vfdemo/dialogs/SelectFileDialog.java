package vfa.vfdemo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;

/**
 * Created by Vitalify on 3/7/17.
 */

public class SelectFileDialog extends Dialog {

    public interface SelectFileListener{
        public void onSelectFile(String filePath);
    }
    SelectFileListener _listener;
    public void setSelectFileListener(SelectFileListener listener){
        _listener = listener;
    }

    String currentFolder = "/mnt/sdcard/db";
    List<String> listFileName = new ArrayList<>();

    TextView tvPath;
    ListView lvFile;
    EditText edtSearch;

    FileAdapter adapter;

    String selectFilePath;

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
//            adapter.clear();
//            adapter.notifyDataSetChanged();
//
//            adapter.addAll(getFilteredResults(s.toString()));
//            adapter.notifyDataSetChanged();
            if(TextUtils.isEmpty(s.toString())){
                loadFileList();
            }else {
                adapter = new FileAdapter(getContext(),getFilteredResults(s.toString()));
                lvFile.setAdapter(adapter);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    public SelectFileDialog(@NonNull Context context) {
        super(context);
    }

    public SelectFileDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected SelectFileDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_file);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setGravity(Gravity.CENTER);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(searchTextWatcher);


        lvFile = (ListView) findViewById(R.id.lvFile);
        tvPath = (TextView) findViewById(R.id.tvPath);
        tvPath.setText(currentFolder);

        lvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String fileName = listFileName.get(position);

                String fileName = (String) parent.getItemAtPosition(position);
                File f= new File(currentFolder + "/" + fileName);
                if(f.isDirectory()){
                    currentFolder += "/" + fileName;
                    loadFileList();
                }else {
                    selectFilePath = currentFolder += "/" + fileName;
                    if(_listener != null) _listener.onSelectFile(selectFilePath);
                    dismiss();
                }
            }
        });
        loadFileList();
    }

    private List<String> getFilteredResults(CharSequence keyword){
        List<String> list = new ArrayList<>();
        keyword = keyword.toString().toLowerCase();

        for(String s:listFileName){
            s = s.toLowerCase();
            if(s.contains(keyword)){
                list.add(s);
//                LogUtils.debug("found result:"+s);
            }
        }
//        LogUtils.debug("==search result:"+list.size());
        return list;
    }

    private void loadFileList(){

        tvPath.setText(currentFolder);
        File f = new File(currentFolder);
        listFileName.clear();
        for(int i = 0;i < f.list().length;i++){
            listFileName.add(f.list()[i]);
        }

        adapter = new FileAdapter(getContext(),f.list());
        lvFile.setAdapter(adapter);
    }

    class FileAdapter extends BaseArrayAdapter<String>{

        public FileAdapter(Context context, String[] objects) {
            super(context, objects);
        }

        public FileAdapter(Context context, List<String> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
            return R.layout.item_file;
        }

        @Override
        public void bindItem(int pos, View v) {
            TextView tv = (TextView) v.findViewById(R.id.tvFileName);
            ImageView iv = (ImageView)v.findViewById(R.id.ivFile);
            String fileName = getItem(pos);
            File f= new File(currentFolder + "/" + fileName);
            if(f.exists()){
                if(f.isDirectory()){
                    tv.setTextColor(Color.BLUE);
                    iv.setBackgroundResource(R.drawable.bg_round_cyan);
                }else {
                    iv.setBackgroundResource(R.drawable.bg_round_gray);
                    tv.setTextColor(Color.BLACK);
                }
            }


            tv.setText(""+getItem(pos));
        }

//        @NonNull
//        @Override
//        public Filter getFilter() {
//            return new Filter() {
//                @SuppressWarnings("unchecked")
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
////                    Log.d(Constants.TAG, "**** PUBLISHING RESULTS for: " + constraint);
////                    myData = (List<String>) results.values;
//                    FileAdapter.this.notifyDataSetChanged();
//                }
//
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
////                    Log.d(Constants.TAG, "**** PERFORM FILTERING for: " + constraint);
//                    List<String> filteredResults = getFilteredResults(constraint);
//
//                    FilterResults results = new FilterResults();
//                    results.values = filteredResults;
//
//                    return results;
//                }
//            };
//        }
    }
}
