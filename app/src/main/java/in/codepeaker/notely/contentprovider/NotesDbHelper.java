package in.codepeaker.notely.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by github.com/codepeaker on 26/1/18.
 */

public class NotesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notesDB.db";

    public static final int VERSION = 1;

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String CREATE_TABLE = "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME + " (" +
                NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                NotesContract.NotesEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                NotesContract.NotesEntry.COLUMN_FAV + " INT , " +
                NotesContract.NotesEntry.COLUMN_STARRED + " INT , " +
                NotesContract.NotesEntry.COLUMN_STORY + " INT , " +
                NotesContract.NotesEntry.COLUMN_POEM + " INT , " +
                NotesContract.NotesEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                NotesContract.NotesEntry.COLUMN_LAST_UPDATED +" INT)";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotesContract.NotesEntry.TABLE_NAME);
        onCreate(db);
    }
}
