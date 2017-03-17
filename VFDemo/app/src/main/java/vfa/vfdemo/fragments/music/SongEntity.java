package vfa.vfdemo.fragments.music;

import android.content.ContentValues;
import android.database.Cursor;

import vfa.vfdemo.data.IDataEntity;

/**
 * Created by Vitalify on 3/16/17.
 */

public class SongEntity implements IDataEntity<SongEntity>{
    public String title;

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public ContentValues getDataRowContent() {
        ContentValues rowData = new ContentValues();
        rowData.put(SongDB.SongDBSchema.TableSong.SONG_TITLE,title);
        return rowData;
    }

    @Override
    public SongEntity getEntity(Cursor cursor) {
        SongEntity entity = new SongEntity();
//        entity.title      = get
        return entity;
    }
}
