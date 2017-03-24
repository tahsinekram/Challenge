package com.example.tahsi.challenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tahsi on 12/25/2016.
 */

public class AppDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = AppDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "challenge.db";

    private static String SQL_CREATE_CHALLENGE_TABLE =  "CREATE TABLE " + AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE + " ("
            + AppContract.ChallengeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AppContract.ChallengeEntry.COLUMN_CHALLENGE + " TEXT)";

    private static String SQL_CREATE_FREE_TABLE =  "CREATE TABLE " + AppContract.ChallengeEntry.TABLE_NAME_FREECOMB + " ("
            + AppContract.ChallengeEntry._FREECOMBID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AppContract.ChallengeEntry.COLUMN_CHALLENGE + " TEXT, "
            + AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR + " TEXT, "
            + AppContract.ChallengeEntry.COLUMN_COMBINED + " TEXT, "
            + AppContract.ChallengeEntry.COLUMN_FREE + " TEXT)";

    public static String SQL_CREATE_TABLE_USERS = "CREATE TABLE " + AppContract.ChallengeEntry.TABLE_NAME_LOGIN + "("
            + AppContract.ChallengeEntry._LOGINID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + AppContract.ChallengeEntry.COLUMN_EMAIL + " TEXT,"
            + AppContract.ChallengeEntry.COLUMN_PASS + " TEXT);";


    private static final int DATABASE_VERSION = 1;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement

        db.execSQL(SQL_CREATE_CHALLENGE_TABLE);
        db.execSQL(SQL_CREATE_FREE_TABLE);
        db.execSQL(SQL_CREATE_TABLE_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
