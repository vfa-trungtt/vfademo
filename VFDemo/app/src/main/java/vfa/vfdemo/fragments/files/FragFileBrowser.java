package vfa.vfdemo.fragments.files;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.ActivityFileBrowser;
import vfa.vfdemo.ActivitySlideMenu;
import vfa.vfdemo.R;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;


public class FragFileBrowser extends VFFragment {

    private RecyclerView recyclerView;
    FileDataAdapter adapter;
    List<FileEntity> _data = new ArrayList<>();

    private String _currentPath = "/";

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_recycleview;
    }

    @Override
    public void onViewLoaded() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                LogUtils.debug("data size:"+ _data.size());
                adapter = new FileDataAdapter(getContext());
                recyclerView.setAdapter(adapter);
            }
        }.execute();
    }

    @Override
    public void setUpActionBar() {
        super.setUpActionBar();
        ((ActivityFileBrowser)getVFActivity()).setupActionbar();

    }

    class FileDataAdapter extends RecyclerView.Adapter<ItemFileHolder>{

        private LayoutInflater inflater;

        public FileDataAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ItemFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_file,null);
            ItemFileHolder itemFileHolder = new ItemFileHolder(v);
            return itemFileHolder;
//            return null;
        }

        @Override
        public void onBindViewHolder(ItemFileHolder holder, int position) {
            FileEntity entity = _data.get(position);
            holder.tvFileName.setText(""+entity.fileName);
        }

        @Override
        public int getItemCount() {
            return _data.size();
        }
    }

    class ItemFileHolder extends RecyclerView.ViewHolder{

        public ImageView ivFileThumb;
        public TextView tvFileName;

        public ItemFileHolder(View itemView) {
            super(itemView);
            ivFileThumb = (ImageView) itemView.findViewById(R.id.ivFile);
            tvFileName  = (TextView) itemView.findViewById(R.id.tvFileName);
            itemView.setBackgroundResource(R.drawable.sel_gray);
        }
    }
}
