package com.example.tahsi.challenge.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tahsi on 12/25/2016.
 */

public final class AppContract {

    private AppContract(){}

    public static final String CONTENT_AUTHORITY ="com.example.tahsi.challenge";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CHALLENGE = "challenge";

    public static final String PATH_ATTR_FREECOMB = "freecomb";

    public static final String PATH_LOGIN = "login";



    public static final class ChallengeEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI_CHALLENGE = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CHALLENGE);

        public static final Uri CONTENT_URI_FREECOMB = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ATTR_FREECOMB);

        public static final Uri CONTENT_URI_LOGIN = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LOGIN);


        public static final String CONTENT_LIST_TYPE_CHALLENGE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHALLENGE;

        public static final String CONTENT_LIST_TYPE_ATTR_FREECOMB =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTR_FREECOMB;

        public static final String CONTENT_LIST_TYPE_LOGIN =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOGIN;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */

        /** Name of database table for pets */
        public final static String TABLE_NAME_CHALLENGE = "brainstorm";

        public final static String TABLE_NAME_FREECOMB = "attributefree";

        public static final String TABLE_NAME_LOGIN = "users";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        public final static String _FREECOMBID = BaseColumns._ID;

        public final static String COLUMN_ATTRIBUTE_PAIR = "attribute";

        public final static String COLUMN_CHALLENGE = "challenge";

        public final static String COLUMN_FREE = "free";

        public final static String COLUMN_COMBINED = "combine";

        public static final String _LOGINID = BaseColumns._ID;
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASS = "password";

    }
}
