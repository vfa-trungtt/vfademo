package vfa.vfdemo.fragments.music;

import android.content.Context;

import java.util.HashMap;

import vfa.vfdemo.data.TableSchema;
import vfa.vfdemo.data.VFDatabase;

/**
 * Created by Vitalify on 3/16/17.
 */

public class SongDB extends VFDatabase {
    public static class DBSchema{
        public static final String TBL_SONG = "tl_songs";

        public static String DBNAME = "song_db.db";
        public static int DB_VERSION = 1;

        HashMap<String,TableSchema> mapTable = new HashMap<>();

        public DBSchema(){
            TableSong tblSong = new TableSong();
            mapTable.put(tblSong.getTableName(),tblSong);
        }

        public void addTable(TableSchema tbl){
            mapTable.put(tbl.getTableName(),tbl);
        }

        class TableSong extends TableSchema{

            public static final String SONG_TITLE = "song_tilte";

            @Override
            public String getTableName() {
                return TBL_SONG;
            }

            @Override
            public void setUpColumns() {
                addTextColumn(SONG_TITLE);
            }
        }

    }

    public SongDB(Context context, String name) {
        super(context, name);
    }
}
