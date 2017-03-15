package vfa.vfdemo.fragments.files;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.ActivityFileBrowser;
import vfa.vfdemo.R;
import vfa.vfdemo.utils.RootUtils;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;


public class FragFileBrowserListView extends VFFragment {

    private ListView listView;
    private TextView tvFilePath;
    private TextView tvStatus;
    String textCount = "";

    FileDataAdapter adapterFile;
    List<FileEntity> _data = new ArrayList<>();

    private String rootPath = "/";//use for root devices
    private String _currentPath = "/";
    private int count;
    private boolean isSelectMode = false;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_file_browser;
    }

    @Override
    public void onViewLoaded() {

        listView    = (ListView) rootView.findViewById(R.id.lvFile);
        tvFilePath  = (TextView) rootView.findViewById(R.id.tvFilePath);
        tvStatus    = (TextView) rootView.findViewById(R.id.tvStatus);
        tvStatus.setText(textCount);

        LogUtils.debug("==getDataDirectory          :"+Environment.getDataDirectory());
        LogUtils.debug("==getExternalStorageState   :"+Environment.getExternalStorageState());
        LogUtils.debug("==getDownloadCacheDirectory :"+Environment.getDownloadCacheDirectory());
        LogUtils.debug("==getExternalStorageDirectory       :"+Environment.getExternalStorageDirectory());
        LogUtils.debug("==getExternalStoragePublicDirectory :"+Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED));

        if(!RootUtils.isDeviceRooted()){
            rootPath        = Environment.getExternalStorageDirectory().getAbsolutePath();
            _currentPath    = rootPath;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isSelectMode) return;

                FileEntity fileEntity = _data.get(position);
                if(fileEntity.isDirectory()){
                    _currentPath += "/" + fileEntity.fileName;
                    reloadData();
                }
            }
        });

        reloadData();
    }

    @Override
    public void setUpActionBar() {
        super.setUpActionBar();
        ((ActivityFileBrowser)getVFActivity()).setHomeActionBar();
        ((ActivityFileBrowser)getVFActivity()).setupActionbar();

        getVFActivity().setActionBarOnClick(R.id.btSearch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.debug("search file...");
                pushFragment(new FragSearchFile());
            }
        });

        getVFActivity().setActionBarOnClick(R.id.btSelect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.debug("search file...");
                isSelectMode = !isSelectMode;
                adapterFile.notifyDataSetChanged();

            }
        });
    }

    @Override
    public boolean onBackPress() {
        if(_currentPath.equalsIgnoreCase(rootPath)){
            return true;
        }else {
            File f = new File((_currentPath));
            _currentPath = f.getParentFile().getAbsolutePath();
            reloadData();
            return false;
        }
    }

    public void reloadData(){
        _data.clear();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                File f = new File(_currentPath);
                if(f.exists()){
                    textCount ="Total:"+ f.list().length+"items.";
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
        adapterFile = new FileDataAdapter(getContext(),0,_data);
        listView.setAdapter(adapterFile);

        tvFilePath.setText(_currentPath);
        tvStatus.setText(textCount);
    }



    class FileDataAdapter extends ArrayAdapter<FileEntity>{

        private Drawable iconFolder;
        private Drawable iconFile;

        public FileDataAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FileEntity> objects) {
            super(context, resource, objects);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iconFolder  = context.getDrawable(R.drawable.bg_round_cyan);
                iconFile    = context.getDrawable(R.drawable.bg_round_gray);
            } else {
                iconFolder = context.getResources().getDrawable(R.drawable.bg_round_cyan);
                iconFile = context.getResources().getDrawable(R.drawable.bg_round_gray);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

            final FileEntity file = getItem(position);
            holder.tvFileName.setText(""+file.fileName);
            if(file.isDirectory()){
                holder.ivFileThumb.setBackground(iconFolder);
                holder.tvFileExt.setVisibility(View.GONE);
            }else {
                holder.ivFileThumb.setBackground(iconFile);
                holder.tvFileExt.setVisibility(View.VISIBLE);
                holder.tvFileExt.setText(file.getExtension());
            }

            holder.chkSelectFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    file.isSelected = isChecked;
                }
            });

            if(isSelectMode){
                holder.chkSelectFile.setVisibility(View.VISIBLE);
                holder.chkSelectFile.setChecked(file.isSelected);

            }else {
                holder.chkSelectFile.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    class ItemFileHolder extends RecyclerView.ViewHolder{

        public ImageView ivFileThumb;
        public TextView tvFileName;
        public TextView tvFileExt;
        public CheckBox chkSelectFile;

        public ItemFileHolder(View itemView) {
            super(itemView);
            ivFileThumb     = (ImageView) itemView.findViewById(R.id.ivFile);
            tvFileName      = (TextView) itemView.findViewById(R.id.tvFileName);
            tvFileExt       = (TextView) itemView.findViewById(R.id.tvFileExt);
            chkSelectFile   = (CheckBox)itemView.findViewById(R.id.chkSelectFile);
        }
    }
}
