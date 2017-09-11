package vn.hdisoft.hdilib.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T>{
    protected LayoutInflater mInflater;
    public abstract int onGetItemLayoutId();
    public abstract void bindItem(int pos,View v);

    public BaseArrayAdapter(Context context, T[] objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(getContext());
    }

    public BaseArrayAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(getContext());
    }

    public View getItemView(){
        return null;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        if(v == null){
            int itemLayoutId = onGetItemLayoutId();
            if(itemLayoutId == 0){
                v =  getItemView();
                if(v == null){
//                    v = new VFSimpleItemListView(getContext());
                }
            }else {
                v = mInflater.inflate(itemLayoutId, null);
            }

        }

        bindItem(position,v);
		return v;
	}

    public void setTextViewText(View parrent,int textViewId,String text){
        TextView tv = (TextView) parrent.findViewById(textViewId);
        if(tv != null){
            tv.setText(text);
        }
    }

    private View getDefaultItemView(){
        TextView v = new TextView(getContext());
        return v;
    }
}

