package vn.hdisoft.hdilib.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class HDiArrayAdapter<E> extends ArrayAdapter<E> {

	public abstract int getItemLayoutRes();
	public abstract void onBindData(int pos,View v);
	
//	private boolean isViewMore;
//	private int viewMoreRes;
	public boolean HasViewMore;
	
	public String ITEM_TAG		= "item";
	public String VIEW_MORE_TAG	="Viewmore";
	
	public HDiArrayAdapter(Context context, ArrayList<E> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		
	}	
	
	public HDiArrayAdapter(Context context, List<E> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		
	}
					
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			View v = inflater.inflate(getItemLayoutRes(), null);
			convertView = v;
		}
		
//		if(getItem(position).JobID.equalsIgnoreCase("")){
//			if(view == null || !view.getTag().equals("ViewMore")){
//				view = inflater.inflate(R.layout.item_viewmore, null);
//				view.setTag("ViewMore");
//			}
//		}else{
//			if(view == null || !view.getTag().equals("Item")){
//				view = inflater.inflate(mItemRes, null);
//				view.setTag("Item");
//			}
//		}
		
		onBindData(position,convertView);
		return convertView;
	}

	public void setViewMore(boolean viewmore){
		HasViewMore = viewmore;
	}
}
