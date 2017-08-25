package vn.hdisoft.hdilib.viewadapters;

import android.view.View;

public interface IBindListItem<E>{
	public void onBindItem(View v, int pos, E entity);
}