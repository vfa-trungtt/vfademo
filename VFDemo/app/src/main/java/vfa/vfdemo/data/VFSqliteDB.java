package vfa.vfdemo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.fragments.sql.RowEntity;
import vfa.vflib.utils.LogUtils;


public class VFSqliteDB {

    public static SQLiteDatabase openDb(Context context,String dbPath){
        try{
            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    public static List<String> getListTables(Context context,String dbPath){
        List<String> list = new ArrayList<>();
        list.add("sqlite_master");
        try{
            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
            String sql = "SELECT name FROM sqlite_master WHERE type='table'";

            Cursor cursor = db.rawQuery(sql,null);

            if( cursor != null && cursor.moveToFirst() ) {
                do {
                    LogUtils.info("table:"+cursor.getString(0));
                    list.add(cursor.getString(0));

                }while(cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public static List<String> getColsTable(String dbPath,String tableName){

        List<String> cols = new ArrayList<>();
        try{
            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
            String sql = "SELECT * FROM "+tableName+" LIMIT 1";

            Cursor cursor = db.rawQuery(sql,null);
            for(int i = 0;i < cursor.getColumnCount();i++){
                String col = cursor.getColumnName(i);
                cols.add(col);
            }
            cursor.close();
            db.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return cols;
    }

    public static List<RowEntity> query(String dbPath, String tableName){
        List<RowEntity> data = new ArrayList<>();

        try{

            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
            String sql = "SELECT * FROM "+tableName+" LIMIT 50";

            Cursor cursor = db.rawQuery(sql,null);
            int colCount = cursor.getColumnCount();

            if( cursor != null && cursor.moveToFirst() ) {
                do {
                    RowEntity row = new RowEntity();
                    for(int i = 0;i < colCount;i++){
                        row.dataRow.add(cursor.getString(i));
                    }
                    data.add(row);


                }while(cursor.moveToNext());
            }


            cursor.close();
            db.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return data;
    }

    public static void exportDatabase(Context context,String dbName){
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+ context.getPackageName()+"//databases//"+dbName+"";
                String backupDBName = "nifty-bk.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBName);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }
}
