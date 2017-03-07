package vfa.vfdemo.utils;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Vitalify on 3/1/17.
 */

public class VFImageLoader extends ImageLoader {

    public VFImageLoader(Context context){

        ImageLoaderConfiguration configuration = new  ImageLoaderConfiguration.Builder(context)
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        init(configuration);
    }
}
