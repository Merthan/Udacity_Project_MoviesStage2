package com.example.merthane.merthan_movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by merth on 14-Apr-18.
 */


public class MyDB_Helper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";


    private static final int DATABASE_VERSION = 2;

    public MyDB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_FAVORITES=

                "CREATE TABLE " + ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES + " (" +

                        ContractClass.MyEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        ContractClass.MyEntry.COLUMN_MOVIE_ID      + " TEXT NOT NULL, "                 +

                        ContractClass.MyEntry.COLUMN_TITLE       + " TEXT NOT NULL ); "        ;




        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractClass.MyEntry.TABLE_NAME_FOR_FAVORITES);

        onCreate(sqLiteDatabase);
    }
}
