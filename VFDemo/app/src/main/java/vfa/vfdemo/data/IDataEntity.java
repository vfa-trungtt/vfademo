package vfa.vfdemo.data;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Vitalify on 3/16/17.
 */

public interface IDataEntity<E> {
    public String getTableName();
    public ContentValues getDataRowContent();
    public E getEntity(Cursor cursor);
    public E getEntity(ContentValues data);
}
