package vfa.vfdemo.videoeditor;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;

import vfa.vfdemo.ui.HorizonBarView;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdilib.utils.LogUtils;


public class FragGalleryVideo extends VFFragment {

    private TextView tvTotal;
    private GridView gridView;

    RecentVideo recentVideo;
    List<MovieEntity> listMovie = new ArrayList<>();
    public interface OnSelectMovieListener{
        public void onSelectMovie(MovieEntity movie);
    }

    @Override
    public void setUpActionBar() {
        getVFActivity().setHomeActionBar();
    }

    private OnSelectMovieListener _listener;



    public void setOnSelectMovie(OnSelectMovieListener listener){
        _listener = listener;
    }

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_gallery;
    }

    @Override
    public void onViewLoaded() {
        gridView = (GridView) rootView.findViewById(R.id.gridGallery);
        tvTotal  = (TextView)rootView.findViewById(R.id.tvTotal);
        recentVideo = new RecentVideo(getContext());
        HorizonBarView viewRecents = (HorizonBarView) rootView.findViewById(R.id.viewRecents);
        viewRecents.setItemByVideoFiles(recentVideo.getList());

        loadGallery();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieEntity entity = listMovie.get(position);
                if(entity!= null && _listener != null){
                    _listener.onSelectMovie(entity);
                }
            }
        });
    }


    private void loadGallery(){
        listMovie.clear();
        showLoading();
        new GalleryTask().execute();
    }

    public void queryVideo() {
        String[] video_query = new String[] {
                MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.RESOLUTION };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri, video_query, null, null, orderBy + " DESC");
        while (cursor.moveToNext()) {

            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(dataColumnIndex);
            MovieEntity entity = new MovieEntity();
            entity.path = imagePath;

            listMovie.add(entity);

            String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
            entity.resolutionString = resolution;

            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            LogUtils.info("path:"+imagePath);
            LogUtils.info("resolution:"+resolution);
            LogUtils.info("duration:"+duration);
        }

        cursor.close();
    }

    private class GalleryTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            queryVideo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            GaleryImageAdapter adapter = new GaleryImageAdapter(getActivity(),listMovie);
            gridView.setAdapter(adapter);
            tvTotal.setText("Total:"+listMovie.size());
            hideLoading();
        }
    }


    public  String getThumbnailPath(Context context, String path)
    {
        long imageId = -1;

        String[] projection = new String[] { MediaStore.MediaColumns._ID };
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[] { path };
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst())
        {
            imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
        }

        String result = null;
        cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
            cursor.close();
        }

        return result;
    }

    class GaleryImageAdapter extends BaseArrayAdapter<MovieEntity>{
        public GaleryImageAdapter(Context context, List<MovieEntity> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
            return R.layout.item_gallery_video;
        }

        @Override
        public void bindItem(int pos, View v) {
            ImageView iv = (ImageView) v.findViewById(R.id.imageView);
            MovieEntity entity = getItem(pos);
            Glide.with(getContext())
                    .load(entity.path)
                    .into(iv);
            ((TextView)v.findViewById(R.id.tvInfo)).setText(entity.resolutionString);

        }
    }
    //handle loading dialog...
    ProgressDialog progressDialog;
    public void showLoading(){
        if(progressDialog == null){
            progressDialog = ProgressDialog.show(getContext(),"","Process...",true);
        }
    }

    public void hideLoading(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
