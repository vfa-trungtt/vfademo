package vfa.vfdemo.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import vfa.vfdemo.BaseActivity;
import vfa.vfdemo.fragments.reminder.ReminderEnity;


public class ReminderEntity extends BaseActivity implements IDataEntity<ReminderEnity>{
    public Date remindDate;
    public String displayMessage;

    @Override
    public String getTableName() {
        return getClass().getSimpleName();
    }

    @Override
    public ContentValues getDataRowContent() {
        return null;
    }

    @Override
    public ReminderEnity getEntity(Cursor cursor) {
        return null;
    }

    @Override
    public ReminderEnity getEntity(ContentValues data) {

        return null;
    }
}
