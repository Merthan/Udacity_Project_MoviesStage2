package com.example.merthane.merthan_movies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by merth on 14-Apr-18.
 */

public class ContractClass {

    public static final String CONTENT_AUTHORITY = "com.example.merthane.merthan_movies";

    public static final String PATH_FAVORITES = "favorites";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);





    public static final class MyEntry implements BaseColumns {
        public static final Uri CONTENT_URI_FAVORITES = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();






        public static final String TABLE_NAME_FOR_FAVORITES = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";



    }
}
