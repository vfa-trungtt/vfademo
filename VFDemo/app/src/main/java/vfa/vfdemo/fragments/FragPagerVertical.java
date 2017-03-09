package vfa.vfdemo.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import vfa.vfdemo.AppSettings;
import vfa.vfdemo.R;
import vfa.vfdemo.data.ImageEntity;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vfdemo.viewadapter.BaseArrayAdapter;
import vfa.vflib.fragments.VFFragment;
import vfa.vflib.utils.LogUtils;

/**
 * Created by Vitalify on 3/8/17.
 */

public class FragPagerVertical extends VFFragment {
    ListView listView;
    public int pageHeight=1677;

    @Override
    public int onGetRootLayoutId() {
        return 0;
    }

    @Override
    public void onViewLoaded() {

        listView = new ListView(getContext());
        rootView.addView(listView);



        View viewCotrol = ViewHelper.getView(getContext(),R.layout.view_page_control);
        rootView.addView(viewCotrol);

        listView.setSmoothScrollbarEnabled(true);
        rootView.findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollBy(pageHeight,800);
            }
        });

        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                LogUtils.debug("height:"+rootView.getHeight());
                pageHeight = rootView.getHeight();
            }
        });


        PageVerticalAdapter adapter = new PageVerticalAdapter(getContext(), ImageEntity.getDemoList());
        listView.setAdapter(adapter);
    }

    class PageVerticalAdapter extends BaseArrayAdapter<ImageEntity>{

        public PageVerticalAdapter(Context context, ImageEntity[] objects) {
            super(context, objects);
        }

        public PageVerticalAdapter(Context context, List<ImageEntity> objects) {
            super(context, objects);
        }

        @Override
        public int onGetItemLayoutId() {
//            return R.layout.item_page;
            return 0;
        }

        @Override
        public View getItemView() {
            View item = ViewHelper.getView(getContext(),R.layout.item_page);
            item.findViewById(R.id.imageView).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,pageHeight));
            return item;
        }

        @Override
        public void bindItem(int pos, View v) {
            ImageView iv = (ImageView) v.findViewById(R.id.imageView);
            AppSettings.imageLoader.displayImage(getItem(pos).url,iv);
        }
    }

    class PageHolder{
        public ImageView imageView;
        public PageHolder(View itemView){

        }
    }
}
