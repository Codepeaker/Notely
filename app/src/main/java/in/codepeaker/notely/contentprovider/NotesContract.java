package in.codepeaker.notely.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by github.com/codepeaker on 26/1/18.
 */

public class NotesContract {

    public static final String AUTHORITY = "in.codepeaker.notely";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_NOTES = "notes";
    public static final String FAV = "fav";
    public static final String STAR = "star";
    public static final String FAV_AND_STAR = "fav_and_star";

    public static final class NotesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_DESC = "DESC";
        public static final String COLUMN_LAST_UPDATED = "LAST_UPDATED";
        public static final String COLUMN_TITLE = "TITLE";
        public static final String COLUMN_STARRED = "STARRED";
        public static final String COLUMN_FAV = "FAV";
        public static final String COLUMN_POEM = "POEM";
        public static final String COLUMN_STORY = "STORY";

    }
}
