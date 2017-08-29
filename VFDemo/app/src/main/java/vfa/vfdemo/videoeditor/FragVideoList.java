package vfa.vfdemo.videoeditor;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import vfa.vfdemo.R;
import vn.hdisoft.hdilib.fragments.VFFragment;
import vn.hdisoft.hdilib.utils.LogUtils;

/**
 * Created by trungtt on 8/29/17.
 */

public class FragVideoList extends VFFragment{
    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_video_list;
    }

    @Override
    public void onViewLoaded() {

        fn_video();
    }

    public void fn_video() {

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name,column_id,thum;

        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media._ID,MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getActivity().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
//            Log.e("Column", absolutePathOfImage);
//            Log.e("Folder", cursor.getString(column_index_folder_name));
//            Log.e("column_id", cursor.getString(column_id));
//            Log.e("thum", cursor.getString(thum));

//            Model_Video obj_model = new Model_Video();
//            obj_model.setBoolean_selected(false);
//            obj_model.setStr_path(absolutePathOfImage);
//            obj_model.setStr_thumb(cursor.getString(thum));

//            al_video.add(obj_model);
            LogUtils.info("path:"+absolutePathOfImage);

        }


//        obj_adapter = new Adapter_VideoFolder(getApplicationContext(),al_video,VideoFolder.this);
//        recyclerView.setAdapter(obj_adapter);

    }
}
