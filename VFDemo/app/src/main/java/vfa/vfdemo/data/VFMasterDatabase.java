package vfa.vfdemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public abstract class VFMasterDatabase extends SQLiteOpenHelper {

    public abstract DBSchema getDBSchema();

    public VFMasterDatabase(Context context, DBSchema dbSchema){
        super(context, dbSchema.getDBName(), null, dbSchema.getDBVerion());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        for(TableSchema tbl:getDBSchema().tables){
//
//        }
    }

    public void saveEntity(IDataEntity data){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(data.getTableName(),null,data.getDataRowContent());
    }

    public void deleteEntity(){

    }
}
