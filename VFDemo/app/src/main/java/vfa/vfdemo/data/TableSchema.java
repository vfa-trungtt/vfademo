package vfa.vfdemo.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vitalify on 9/6/16.
 */
public abstract class TableSchema {

    public static final String DATA_PRIMARY_INTEGER  = "integer PRIMARY KEY AUTOINCREMENT NOT NULL";
    public static final String DATA_TEXT        = "TEXT DEFAULT ''";
    public static final String DATA_INTEGER     = "INTEGER DEFAULT 0";
    public static final String DATA_DATE_STAMP  = "DATETIME DEFAULT CURRENT_TIMESTAMP";

    public String tableName;
    public ArrayList<String> columns = new ArrayList<>();
    public Map<String,String> fields = new HashMap<>();
    //Default column
    private boolean _hasDefaultCollumn = true;

    public static final String F_ID          = "id";
    public static final String F_IS_DELETED  = "is_deleted";
    public static final String F_UPDATE_DATE = "date_updated";
    public static final String F_CREATE_DATE = "date_created";

    public abstract String getTableName();
    public abstract void onCreateTable();

    private SQLiteDatabase _db;


    public TableSchema(boolean hasdefaultColumn){
        _hasDefaultCollumn = hasdefaultColumn;
        tableName = getTableName();
        initDefaultColumns();
    }

    public TableSchema(){
        tableName = getTableName();
        initDefaultColumns();
    }

    public TableSchema(SQLiteDatabase db){
        _db = db;
        initDefaultColumns();
        _db.execSQL(getCreateTableSql());
    }

    public TableSchema(SQLiteDatabase db, boolean hasdefaultColum){
        _hasDefaultCollumn = hasdefaultColum;
        _db = db;
        initDefaultColumns();
        _db.execSQL(getCreateTableSql());
    }

    public boolean isEmpty(){
        long count = count(null);
        if(count == 0) return true;
        return false;

    }

    public long count(String where){
        String sql = "SELECT COUNT(*) FROM "+tableName;
        if(!TextUtils.isEmpty(where)){
            sql += where;
        }

        if(_db == null) return 0;

        Cursor cursor = _db.rawQuery(sql,null);
        cursor.moveToFirst();
        long reval = cursor.getLong(0);
        cursor.close();
        return reval;

    }

    public void disableDefaulColums(boolean value){
        _hasDefaultCollumn = value;
    }

    private void initDefaultColumns(){
        tableName = getTableName();
        // add Default column
        // is_delete,dateCreate,dateupdate,id,cloud_id
        if(_hasDefaultCollumn){
            fields.put(F_ID,"integer PRIMARY KEY AUTOINCREMENT NOT NULL");
//            fields.put(F_CLOUD_ID,"TEXT");
//            fields.put(F_INDEX,"INTEGER DEFAULT 0");
            fields.put(F_IS_DELETED,"INTEGER DEFAULT 0");

            fields.put(F_CREATE_DATE,"INTEGER");
            fields.put(F_UPDATE_DATE,"INTEGER");
        }


        onCreateTable();
    }

    public void upgradeTable(SQLiteDatabase db,int newVesion){

    }

    public void addFieldIfNotExist(SQLiteDatabase db, String fieldName,String fieldType) {
        boolean isExist = true;
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT  "+ fieldName +" FROM "+tableName,null);
            if(res == null){
                isExist = false;
            }
            int value = res.getColumnIndex(fieldName);

            if(value == -1){
                isExist = false;
            }

//            LogUtils.info("Field " + value );
        } catch (Exception e) {
            // TODO: handle exception
//            LogUtils.error(e.getMessage());
            isExist = false;
        }

        if(res != null) res.close();

        if(isExist){
//            LogUtils.info("Field " + fieldName +" has exist! ");
        }else{

//            LogUtils.info("Field " + fieldName +" not exist!Create it!!!");
            String sql = "ALTER TABLE "+tableName + " ADD "+fieldName +" " + fieldType;
            db.execSQL(sql);
        }

    }

    public void addIntegerColumn(String colName){
        fields.put(colName,"INTEGER");
    }

    public void addTextColumn(String colName){
        fields.put(colName,"TEXT");
    }

    public void addDateTimeColumn(String colName){
        fields.put(colName,"DATETIME DEFAULT CURRENT_TIMESTAMP");
    }

    public void addBoolColumn(String colName){
        fields.put(colName,"BOOL DEFAULT false");
    }

    public void addColumn(String colName,String type){
        fields.put(colName,type);
    }

    public void deleteAll(){

    }

    public void removeColumn(String colName){

    }

    public  String getCreateTableSql(){
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName +" (";
//        String sql = "CREATE TABLE " + tableName +" (";
        String separator = "";

        for (Map.Entry<String, String> param : fields.entrySet()) {
            sql += separator +  param.getKey() + " " + param.getValue();
            separator = ",";
        }
        sql += ");";
//        LogUtils.debug("create db:"+sql);
        return sql;
    }

    public void query(String where){

        if(TextUtils.isEmpty(where)){

        }else{

        }

    }

    public void insert(ContentValues data){
        long id = _db.insert(tableName, null, data);
    }

    public void update(ContentValues data,int id){
        int row_affected = _db.update(tableName, data, F_ID + " = " + id, null);
    }

    public void updateDelete(int id){

        ContentValues contentValue = new ContentValues();
        contentValue.put(F_IS_DELETED, 1);
//        contentValue.put(F_UPDATE_DATE, MBassHelper.getDateString(new Date()));

        int row_affected = _db.update(tableName, contentValue, F_ID + " = " + id, null);
    }

    public void delete(){

    }

    public int getIntColumnByName(Cursor cursor, String fieldName){
        int reval = 0;
        int index = cursor.getColumnIndex(fieldName);

        if(index == -1){
//            LogUtils.error("Field ["+fieldName + "] not found!");
            return 0;
        }
        try {
            reval = Integer.parseInt(cursor.getString(index));

        } catch (Exception e) {
            // TODO: handle exception
//            LogUtils.error("Field ["+fieldName + "] data not interger :"+cursor.getString(index));
            reval = 0;
        }
        return reval;
    }

    public long getLongColumnByName(Cursor cursor, String fieldName){
        long reval = 0;
        int index = cursor.getColumnIndex(fieldName);

        if(index == -1){
//            LogUtils.error("Field ["+fieldName + "] not found!");
            return 0;
        }
        try {
            reval = cursor.getLong(index);

        } catch (Exception e) {
            // TODO: handle exception
//            LogUtils.error("Field ["+fieldName + "] data not interger :"+cursor.getString(index));
            reval = 0;
        }
        return reval;
    }

    public String getStringColumnByName(Cursor cursor,String fieldName){
        String reval = "";
        int index = cursor.getColumnIndex(fieldName);
        if(index == -1){
//            LogUtils.error("Field ["+fieldName + "] not found!");
            return "";
        }
        try {
            reval = cursor.getString(index);

        } catch (Exception e) {
            // TODO: handle exception
//            LogUtils.error("Field ["+fieldName + "] data not string :"+cursor.getString(index));
        }
        return reval;
    }

    public Date getDateTimeColumnByName(Cursor cursor,String fieldName){
        Date date = new Date();
        String tmp = getStringColumnByName(cursor,fieldName);

        return date;
    }

    public Date getDateTimeFromInt(Cursor cursor,String fieldName){
        Date date = new Date();
        long tmp = getLongColumnByName(cursor,fieldName);
        date.setTime(tmp);

        return date;
    }

    public boolean getBoolFromInt(Cursor cursor,String fieldName){
        int tmp = getIntColumnByName(cursor,fieldName);
        if(tmp == 0)
            return false;
        else
            return  true;
    }
}
