package vfa.vfdemo.fragments;

import android.widget.ImageView;

import vfa.vfdemo.activity.AppSettings;
import vfa.vfdemo.R;
import vfa.vfdemo.data.ImageEntity;
import vn.hdisoft.hdilib.fragments.VFFragment;


/**
 * Created by Vitalify on 3/1/17.
 */

public class FragPage extends VFFragment{
    public ImageEntity imageEntity;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_page;
    }

    @Override
    public void onViewLoaded() {
        ImageView iv = (ImageView) rootView.findViewById(R.id.imageView);
        if(imageEntity == null){
            return;
        }
        AppSettings.imageLoader.displayImage(imageEntity.url,iv);

//        TextView tv = (TextView) rootView.findViewById(R.id.tvGuide);
//
//        BubbleDrawable myBubble = new BubbleDrawable(BubbleDrawable.CENTER);
////        myBubble.setCornerRadius(20);
//        myBubble.setCornerRadius(7);
////        myBubble.setPointerAlignment(BubbleDrawable.RIGHT);
//        myBubble.setPadding(5, 5, 5, 5);
//        tv.setBackgroundDrawable(myBubble);
//        tv.setBackground(myBubble);
    }
}
