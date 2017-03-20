package vfa.vfdemo.data;

import java.util.HashMap;

/**
 * Created by Vitalify on 3/20/17.
 */

public abstract class DBSchema {
    public String dbname;
    public int dbversion;
    public HashMap<String,TableSchema> tables = new HashMap<>();
    public abstract String getDBName();
    public abstract int getDBVerion();
    public abstract void onCreateTables();

    public DBSchema(){
        onCreateTables();
    }

    public void addTable(TableSchema tbl){
        tables.put(tbl.tableName,tbl);
    }
}
