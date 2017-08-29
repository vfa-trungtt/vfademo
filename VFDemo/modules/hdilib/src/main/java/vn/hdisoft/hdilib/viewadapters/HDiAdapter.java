package vn.hdisoft.hdilib.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class HDiAdapter<E> extends ArrayAdapter<E> {

	private int itemRes;
	
	public IBindListItem bindItem;
	
	public HDiAdapter(Context context, ArrayList<E> objects, int itemLayout) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		itemRes = itemLayout;
	}	
	
	public HDiAdapter(Context context, List<E> objects, int itemLayout) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		itemRes	= itemLayout;
	}
					
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			View v = inflater.inflate(itemRes, null);
			convertView = v;
		}

		if(bindItem != null){
			E entity = getItem(position);
			bindItem.onBindItem(convertView, position, entity);
		}
		
		return convertView;
	}	
}
