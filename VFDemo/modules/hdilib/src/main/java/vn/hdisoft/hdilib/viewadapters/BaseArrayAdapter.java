package vn.hdisoft.hdilib.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//import vn.hdisoft.hdilib.ui.SimpleItemView;

public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {

	public interface BindItemHandler{
		public void onBindItem(View v, int pos);
	}
	
	public abstract void onBindItem(View v, int pos);
	
	private int _itemLayoutRes;
	private BindItemHandler _bindHandler;
	
	public BaseArrayAdapter(Context context, List<T> objects, int itemLayoutRes) {
		super(context, 0, 0, objects);
		_itemLayoutRes = itemLayoutRes;
	}
	
	public void setOnBindItem(BindItemHandler bindHandler){
		_bindHandler = bindHandler;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			if(_itemLayoutRes == 0){
				//default itemlayout is textview
//				v = onCreateItemView();
			}else{
				v = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(_itemLayoutRes, null);
			}
			
		}

		if(_bindHandler != null){
			_bindHandler.onBindItem(v, position);
		}else{
            onBindItem(v, position);
        }
		return v;
	}

	//user for default item view
	public void setTitleText(View item, String text){
		TextView tv = (TextView)item;
		if(tv != null){
			tv.setText(text);
		}
	}
		

//    public View onCreateItemView(){
//        View v = new SimpleItemView(getContext());
//        return v;
//    }
}
