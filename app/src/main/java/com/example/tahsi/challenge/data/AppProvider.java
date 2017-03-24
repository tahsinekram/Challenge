package com.example.tahsi.challenge.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by tahsi on 12/25/2016.
 */

public class AppProvider extends ContentProvider{

    public static final String LOG_TAG = AppProvider.class.getSimpleName();

    private static final int CHALLENGE = 100;

    private static final int CHALLENGE_ID = 101;

    private static final int FREECOMB = 102;

    private static final int FREECOMB_ID = 103;

    private static final int LOGIN = 104;

    private static final int LOGIN_ID = 105;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_CHALLENGE, CHALLENGE);

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_CHALLENGE + "/#", CHALLENGE_ID);

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_ATTR_FREECOMB, FREECOMB);

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_ATTR_FREECOMB + "/#", FREECOMB_ID);

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_LOGIN,LOGIN);

        sUriMatcher.addURI(AppContract.CONTENT_AUTHORITY, AppContract.PATH_LOGIN + "/#", LOGIN_ID);



    }
    private AppDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AppDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CHALLENGE:

                cursor = database.query(AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CHALLENGE_ID:

                selection = AppContract.ChallengeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = database.query(AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FREECOMB:

                cursor = database.query(AppContract.ChallengeEntry.TABLE_NAME_FREECOMB, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FREECOMB_ID:

                selection = AppContract.ChallengeEntry._FREECOMBID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = database.query(AppContract.ChallengeEntry.TABLE_NAME_FREECOMB, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case LOGIN:

                cursor = database.query(AppContract.ChallengeEntry.TABLE_NAME_LOGIN, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case LOGIN_ID:

                selection = AppContract.ChallengeEntry._LOGINID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = database.query(AppContract.ChallengeEntry.TABLE_NAME_LOGIN, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHALLENGE:
                return AppContract.ChallengeEntry.CONTENT_LIST_TYPE_CHALLENGE;
            case CHALLENGE_ID:
                return AppContract.ChallengeEntry.CONTENT_LIST_TYPE_CHALLENGE;
            case FREECOMB:
                return AppContract.ChallengeEntry.CONTENT_LIST_TYPE_ATTR_FREECOMB;
            case FREECOMB_ID:
                return AppContract.ChallengeEntry.CONTENT_LIST_TYPE_ATTR_FREECOMB;
            case LOGIN:
                return AppContract.ChallengeEntry.CONTENT_LIST_TYPE_LOGIN ;
            case LOGIN_ID:
                return AppContract.ChallengeEntry.CONTENT_LIST_TYPE_LOGIN ;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHALLENGE:
                return insertChallenge(uri, contentValues);
            case FREECOMB:
                return insertFreeComb(uri, contentValues);
            case LOGIN:
                return insertLogin(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertChallenge(Uri uri, ContentValues values) {
        // Check that the name is not null
        String challenge = values.getAsString(AppContract.ChallengeEntry.COLUMN_CHALLENGE);
        if (challenge == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertFreeComb(Uri uri, ContentValues values) {
        // Check that the name is not null
        String free = values.getAsString(AppContract.ChallengeEntry.COLUMN_FREE);
        if (free == null) {
            throw new IllegalArgumentException("Pet requires a free name");
        }

        String comb = values.getAsString(AppContract.ChallengeEntry.COLUMN_COMBINED);
        if (comb == null) {
            throw new IllegalArgumentException("Pet requires a comb name");
        }

        String attribute = values.getAsString(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR);
        if (attribute == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // No need to check the breed, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(AppContract.ChallengeEntry.TABLE_NAME_FREECOMB, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertLogin(Uri uri, ContentValues values) {
        // Check that the name is not null
        String email = values.getAsString(AppContract.ChallengeEntry.COLUMN_EMAIL);
        if (email == null) {
            throw new IllegalArgumentException("requires email");
        }

        String pass = values.getAsString(AppContract.ChallengeEntry.COLUMN_PASS);
        if (pass == null) {
            throw new IllegalArgumentException("requires pass");
        }
        // No need to check the breed, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(AppContract.ChallengeEntry.TABLE_NAME_LOGIN, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHALLENGE:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE, selection, selectionArgs);
                break;
            case CHALLENGE_ID:
                // Delete a single row given by the ID in the URI
                selection = AppContract.ChallengeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE, selection, selectionArgs);
                break;
            case FREECOMB:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AppContract.ChallengeEntry.TABLE_NAME_FREECOMB, selection, selectionArgs);
                break;
            case FREECOMB_ID:
                // Delete a single row given by the ID in the URI
                selection = AppContract.ChallengeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AppContract.ChallengeEntry.TABLE_NAME_FREECOMB, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHALLENGE:
                return updateChallenge(uri, contentValues, selection, selectionArgs);
            case CHALLENGE_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AppContract.ChallengeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateChallenge(uri, contentValues, selection, selectionArgs);
            case FREECOMB:
                return updateFreeComb(uri, contentValues, selection, selectionArgs);
            case FREECOMB_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AppContract.ChallengeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateFreeComb(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateChallenge(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(AppContract.ChallengeEntry.COLUMN_CHALLENGE)) {
            String challenge = values.getAsString(AppContract.ChallengeEntry.COLUMN_CHALLENGE);
            if (challenge == null) {
                throw new IllegalArgumentException("App requiers a challenge");
            }
        }

        if (values.containsKey(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR)) {
            String question = values.getAsString(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR);
            if (question == null) {
                throw new IllegalArgumentException("App requiers a question");
            }
        }
        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AppContract.ChallengeEntry.TABLE_NAME_CHALLENGE, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    private int updateFreeComb(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(AppContract.ChallengeEntry.COLUMN_FREE)) {
            String challenge = values.getAsString(AppContract.ChallengeEntry.COLUMN_FREE);
            if (challenge == null) {
                throw new IllegalArgumentException("App requiers a free");
            }
        }

        if (values.containsKey(AppContract.ChallengeEntry.COLUMN_CHALLENGE)) {
            String challenge = values.getAsString(AppContract.ChallengeEntry.COLUMN_CHALLENGE);
            if (challenge == null) {
                throw new IllegalArgumentException("App requiers a challenge");
            }
        }

        if (values.containsKey(AppContract.ChallengeEntry.COLUMN_COMBINED)) {
            String challenge = values.getAsString(AppContract.ChallengeEntry.COLUMN_COMBINED);
            if (challenge == null) {
                throw new IllegalArgumentException("App requiers a comb");
            }
        }

        if (values.containsKey(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR)) {
            String question = values.getAsString(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR);
            if (question == null) {
                throw new IllegalArgumentException("App requiers a pair");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AppContract.ChallengeEntry.TABLE_NAME_FREECOMB, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }




}
