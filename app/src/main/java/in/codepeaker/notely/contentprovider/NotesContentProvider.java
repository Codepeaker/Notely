package in.codepeaker.notely.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.CONTENT_URI;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.TABLE_NAME;

/**
 * Created by github.com/codepeaker on 26/1/18.
 */

public class NotesContentProvider extends ContentProvider {

    public static final int NOTES = 100;
    public static final int NOTE_WITH_ID = 101;
    public static final int FAV_NOTES = 102;
    public static final int STAR_NOTES = 103;
    public static final int FAV_AND_STAR_NOTES = 104;
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private NotesDbHelper notesDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NotesContract.AUTHORITY, NotesContract.PATH_NOTES, NOTES);
        uriMatcher.addURI(NotesContract.AUTHORITY, NotesContract.PATH_NOTES + "/#", NOTE_WITH_ID);
        uriMatcher.addURI(NotesContract.AUTHORITY, NotesContract.PATH_NOTES + "/" + NotesContract.FAV, FAV_NOTES);
        uriMatcher.addURI(NotesContract.AUTHORITY, NotesContract.PATH_NOTES + "/" + NotesContract.STAR, STAR_NOTES);
        uriMatcher.addURI(NotesContract.AUTHORITY, NotesContract.PATH_NOTES + "/" + NotesContract.FAV_AND_STAR, FAV_AND_STAR_NOTES);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        notesDbHelper = new NotesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase sqLiteDatabase = notesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case NOTES:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAV_NOTES:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case STAR_NOTES:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAV_AND_STAR_NOTES:
                retCursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = notesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case NOTES:
                long id = 0;
                try {

                    id = sqLiteDatabase.insert(TABLE_NAME, null, values);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = notesDbHelper.getWritableDatabase();

        int notesDeleted;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTE_WITH_ID:

                String id = uri.getPathSegments().get(1);
                notesDeleted = sqLiteDatabase.delete(TABLE_NAME
                        , "_id=?", new String[]{id});

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        if (notesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }

        return notesDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = notesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int notesUpdated;

        switch (match) {
            case NOTE_WITH_ID:

                String id = uri.getPathSegments().get(1);
                notesUpdated = sqLiteDatabase.update(TABLE_NAME, values, "_id=?", new String[]{id});

                if (notesUpdated <= 0) {
                    throw new SQLException("Failed to update row " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return notesUpdated;
    }
}
