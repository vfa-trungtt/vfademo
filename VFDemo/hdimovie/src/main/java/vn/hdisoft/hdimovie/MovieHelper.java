package vn.hdisoft.hdimovie;

import android.media.MediaMetadataRetriever;
import android.util.Size;

import vn.hdisoft.hdilib.utils.LogUtils;

/**
 * Created by trungtt on 8/30/17.
 */

public class MovieHelper {

    public static void getInfo(String moviePath){
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(moviePath);
        String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        LogUtils.info("w:"+width+",h:"+height);
    }


}
