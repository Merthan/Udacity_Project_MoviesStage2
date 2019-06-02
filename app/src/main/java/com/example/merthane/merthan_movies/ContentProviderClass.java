package com.example.merthane.merthan_movies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by merth on 14-Apr-18.
 */

public class ContentProviderClass extends ContentProvider {



    private static final int CODE_FAVORITES = 500;
    private static final int CODE_FAVORITES_AND_ID = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MyDB_Helper mDatabaseHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContractClass.CONTENT_AUTHORITY;



        
        matcher.addURI(authority, ContractClass.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, ContractClass.PATH_FAVORITES + "/#", CODE_FAVORITES_AND_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new MyDB_Helper(getContext());
        return true;//was handled
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        throw new RuntimeException("not implemented");
        }



    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        String movieId;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITES:
                cursor = mDatabaseHelper.getReadableDatabase().query(
                        ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_FAVORITES_AND_ID:
                movieId = uri.getLastPathSegment();
                cursor = mDatabaseHelper.getReadableDatabase().query(
                        ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES,
                        projection,
                        ContractClass.MyEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder);
                break;




            default:
                throw new UnsupportedOperationException("Query error at:  " + uri);
        }

        
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int numRowsDeleted;
        String movieId;


        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {


            case CODE_FAVORITES:
                numRowsDeleted = db.delete(ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES, selection, selectionArgs);
                break;

            case CODE_FAVORITES_AND_ID:
                movieId = uri.getLastPathSegment();
                numRowsDeleted = db.delete(ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES,
                        ContractClass.MyEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{movieId});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri (delete): " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not working .");
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Uri returnUri;
        long ids;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITES:
                ids = db.insert(ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES, null, values);
                if (ids > 0) {
                    returnUri = ContentUris.withAppendedId(ContractClass.MyEntry.CONTENT_URI_FAVORITES, ids);
                } else {
                    throw new SQLException("Failed to insert row at" + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri command: inserting>>> " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not working");
    }


}
