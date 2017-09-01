package vfa.vfdemo.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import vfa.vfdemo.utils.ViewHelper;


public class HorizonBarView extends HorizontalScrollView {

    public interface OnHorizonBarIemClick{
        public void onClick(int itemIndex);
    }

    private OnHorizonBarIemClick _litener;
    public void setOnItemClick(OnHorizonBarIemClick listener){
        _litener = listener;
    }
    private LinearLayout viewContainer;
    private int itemResid;

    private View lastItem;
    private OnClickListener onItemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = Integer.parseInt(v.getTag().toString());
            if(lastItem != null){
                lastItem.setSelected(false);
            }
            v.setSelected(true);
            lastItem = v;
            if(_litener != null) _litener.onClick(index);
        }
    };

    public HorizonBarView(Context context) {
        super(context);
        init();
    }

    public HorizonBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizonBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizonBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        viewContainer = new LinearLayout(getContext());
        viewContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(viewContainer);
        if(isInEditMode()){

        }
    }

    public void setItemLayoutResId(int resId){
        itemResid = resId;
    }

    public void setItems(){

    }

    public void setItemByImageResArray(int[] arrRes,int imageViewId){
        viewContainer.removeAllViews();
        for(int i = 0;i<arrRes.length;i++){
            View item = ViewHelper.getView(getContext(),itemResid);
            ImageView iv = (ImageView) item.findViewById(imageViewId);
            iv.setImageResource(arrRes[i]);

            item.setTag(""+i);
            item.setOnClickListener(onItemClick);
            viewContainer.addView(item);

            if(i == 0) {
                lastItem = item;
                lastItem.setSelected(true);
            }
        }
    }

    public void setItemByVideoFiles(List<String> listFiles){
        int index = 0;
        for(String filePath:listFiles){
            ImageView iv = new ImageView(getContext());
            Glide.with(getContext())
                    .load(filePath)
                    .into(iv);
            iv.setTag(""+index);
            iv.setOnClickListener(onItemClick);
            viewContainer.addView(iv);
            index++;
        }
    }
}
