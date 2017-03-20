package vfa.vfdemo.fragments.music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import vfa.vfdemo.data.DBSchema;
import vfa.vfdemo.data.TableSchema;
import vfa.vfdemo.data.VFMasterDatabase;


public class SongDB extends VFMasterDatabase {

    public static String DB_NAME        = "song_db.db";
    public static int DBVersion         = 1;

    //=======
    public static class SongDBSchema extends DBSchema{
        public static final String TBL_SONG = "tl_songs";

        @Override
        public String getDBName() {
            return DB_NAME;
        }

        @Override
        public int getDBVerion() {
            return DBVersion;
        }

        @Override
        public void onCreateTables() {
            TableSong tblSong = new TableSong();
            addTable(tblSong);
        }

        //--------
        class TableSong extends TableSchema{

            public static final String SONG_TITLE = "song_tilte";

            @Override
            public String getTableName() {
                return TBL_SONG;
            }

            @Override
            public void onCreateTable() {
                addTextColumn(SONG_TITLE);
            }
        }
        //--------
    }
    //======
    @Override
    public DBSchema getDBSchema() {
        return new SongDBSchema();
    }

    public SongDB(Context context, DBSchema dbSchema) {
        super(context, dbSchema);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
