package vfa.vfdemo.fragments.images;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vfa.vfdemo.AppSettings;
import vfa.vfdemo.R;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vfa.vflib.fragments.VFFragment;

/**
 * Created by Vitalify on 3/7/17.
 */

public class FragGallery extends VFFragment {

    TextView tvTotal;
    GridView gridView;
    List<String> listGallery = new ArrayList<>();

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_gallery;
    }

    @Override
    public void onViewLoaded() {
        gridView = (GridView) rootView.findViewById(R.id.gridGallery);
        tvTotal  = (TextView)rootView.findViewById(R.id.tvTotal);

        loadGallery();
    }

    private void loadGallery(){
        listGallery.clear();
        new GalleryTask().execute();
    }


    private class GalleryTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Cursor imagecursor = null;
            try {
                final String[] columns = { MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media._ID ,MediaStore.Images.Media.ORIENTATION};
                final String orderBy = MediaStore.Images.Media._ID;


                imagecursor = getActivity().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                        null, null, orderBy);


                if (imagecursor != null && imagecursor.getCount() > 0) {
                    while (imagecursor.moveToNext()) {

                        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        String imagePath = imagecursor.getString(dataColumnIndex);
                        listGallery.add(imagePath);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(imagecursor != null)	imagecursor.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            GaleryImageAdapter adapter = new GaleryImageAdapter(getActivity(),listGallery);
            gridView.setAdapter(adapter);
            tvTotal.setText("Total:"+listGallery.size());
        }
    }

    class GaleryImageAdapter extends BaseArrayAdapter<String>{

        public GaleryImageAdapter(Context context, String[] objects) {
            super(context, objects);
        }

        public GaleryImageAdapter(Context context, List<String> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
            return R.layout.item_gallery;
        }

        @Override
        public void bindItem(int pos, View v) {
            ImageView iv = (ImageView) v.findViewById(R.id.imageView);
            AppSettings.imageLoader.displayImage("file:///"+getItem(pos),iv);
        }
    }

}
