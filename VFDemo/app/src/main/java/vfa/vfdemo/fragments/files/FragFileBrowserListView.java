package vfa.vfdemo.fragments.files;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;


public class FragFileBrowserListView extends VFFragment {

    private ListView listView;
    private TextView tvFilePath;

    FileDataAdapter adapter;
    List<FileEntity> _data = new ArrayList<>();

    private String _currentPath = "/";

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_file_browser;
    }

    @Override
    public void onViewLoaded() {

        listView    = (ListView) rootView.findViewById(R.id.lvFile);
        tvFilePath  = (TextView) rootView.findViewById(R.id.tvFilePath);

        LogUtils.debug("==getDataDirectory          :"+Environment.getDataDirectory());
        LogUtils.debug("==getExternalStorageState   :"+Environment.getExternalStorageState());
        LogUtils.debug("==getDownloadCacheDirectory :"+Environment.getDownloadCacheDirectory());
        LogUtils.debug("==getExternalStorageDirectory       :"+Environment.getExternalStorageDirectory());
        LogUtils.debug("==getExternalStoragePublicDirectory :"+Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileEntity fileEntity = _data.get(position);
                if(fileEntity.isDirectory()){
                    _currentPath += "/" + fileEntity.fileName;
                    reloadData();
                }
            }
        });

        reloadData();
    }

    public void reloadData(){
        _data.clear();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File f = new File(_currentPath);
                if(f.exists()){
                    for(String filename:f.list()){
                        FileEntity file = new FileEntity();
                        file.fileName   = filename;
                        file.filePath   = _currentPath + "/" + filename;
                        _data.add(file);
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                displayData();
            }
        }.execute();
    }

    private void displayData(){
        LogUtils.debug("data size:"+ _data.size());
        adapter = new FileDataAdapter(getContext(),0,_data);
        listView.setAdapter(adapter);

        tvFilePath.setText(_currentPath);
    }

    class FileDataAdapter extends ArrayAdapter<FileEntity>{
        public FileDataAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FileEntity> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            ItemFileHolder holder;

            if (convertView == null) {
                convertView = ViewHelper.getView(getContext(),R.layout.item_file);
                holder = new ItemFileHolder(convertView);
                convertView.setTag(holder);
            }
            else {
                holder = (ItemFileHolder) convertView.getTag();
            }

            FileEntity file = getItem(position);
            holder.tvFileName.setText(""+file.fileName);
            return convertView;
        }
    }

    class ItemFileHolder extends RecyclerView.ViewHolder{

        public ImageView ivFileThumb;
        public TextView tvFileName;

        public ItemFileHolder(View itemView) {
            super(itemView);
            ivFileThumb = (ImageView) itemView.findViewById(R.id.ivFile);
            tvFileName  = (TextView) itemView.findViewById(R.id.tvFileName);
        }
    }
}
