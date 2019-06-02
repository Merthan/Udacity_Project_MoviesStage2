package com.example.merthane.merthan_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyAdapter.CardClickListener {


    public static final String API_KEY="";//TODO:INSERT API KEY HERE



    MyAdapter adapter;

    final boolean[] isWithinFavorites=new boolean[1];


    final static String sort_rating ="sort_rating";

    FloatingActionButton fabReference ;
    RecyclerView recyclerViewReference;

    @Override
    public void onBackPressed() {
        if(isWithinFavorites[0]){
            fabReference.callOnClick();
            return;
        }
        super.onBackPressed();
    }
    private String RV_STATE_KEY="rv_state";
    private Parcelable mRvState;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRvState=recyclerViewReference.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RV_STATE_KEY,mRvState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            mRvState=savedInstanceState.getParcelable(RV_STATE_KEY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isWithinFavorites[0]=false;

        Toast.makeText(this,"To show your Favorites, long press the corner Button.",Toast.LENGTH_SHORT).show();

        final SharedPreferences sharedPreferences= getPreferences(MODE_PRIVATE);
        final RecyclerView recyclerView=findViewById(R.id.rv);
        recyclerViewReference=recyclerView;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fabReference=fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWithinFavorites[0]=false;

                boolean b=sharedPreferences.getBoolean(sort_rating,false);
                //if true, it was set to sorting by rating so group picture, because now it will be by popular
                fab.setImageResource(b?R.drawable.ic_group_black_24dp:R.drawable.ic_thumb_up_black_24dp);

                sharedPreferences.edit().putBoolean(sort_rating,!b).apply();

                FetchMovies fm = new FetchMovies(adapter,MainActivity.this,!b);
                fm.execute();

                Snackbar.make(view, b?getString(R.string.set_to_popular):getString(R.string.set_to_rating), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();


                recyclerView.smoothScrollToPosition(0);
            }
        });


        boolean sort_by_rating=sharedPreferences.getBoolean(sort_rating,false);
        fab.setImageResource(sort_by_rating?R.drawable.ic_thumb_up_black_24dp:R.drawable.ic_group_black_24dp   );

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                prepareFavorites();
                return true;
            }
        });

        int num_colums=3;
        recyclerView.setLayoutManager(new GridLayoutManager(this,num_colums));
        adapter=new MyAdapter(this,new String[]{""});
        adapter.setCardClickListener(this);
        recyclerView.setAdapter(adapter);

        FetchMovies fm = new FetchMovies(adapter,this,sort_by_rating);
        fm.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isWithinFavorites[0]){
            prepareFavorites();//in case someone just deleted an element
        }
        if(mRvState!=null && recyclerViewReference!=null) {

            recyclerViewReference.getLayoutManager().onRestoreInstanceState(mRvState);
        }
    }

    private void prepareFavorites(){
        isWithinFavorites[0]=true;
        recyclerViewReference.smoothScrollToPosition(0);

        Cursor cursor2 =getContentResolver().query(ContractClass.MyEntry.CONTENT_URI_FAVORITES,null,null,null,null);

        String[] ids = new String[cursor2.getCount()];


        for( cursor2.moveToFirst() ; cursor2.moveToNext() ; ){//move to first the first time,
            ids[cursor2.getPosition()]= cursor2.getString(cursor2.getColumnIndex(ContractClass.MyEntry.COLUMN_MOVIE_ID));
        }
        cursor2.close();

        fabReference.setImageResource(R.drawable.ic_favorite_black_24dp);

        Snackbar.make(fabReference, R.string.showing_favorites, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        FetchFavorites fetchFavorites = new FetchFavorites(adapter,MainActivity.this,ids);
        fetchFavorites.execute();
    }

    @Override
    public void onItemClick(View view, int position,Movie[] movies) {
        Movie m=movies[position];
        Intent i=new Intent(this,DetailActivity.class);

        i.putExtra("imageString",m.getImageString());
        i.putExtra("title",m.getTitle());
        i.putExtra("date",m.getDate());
        i.putExtra("vote_average",m.getVoteAverage());
        i.putExtra("plot_synopsis",m.getPlotSynopsis());
        i.putExtra("id",m.getID());
        startActivity(i);

    }
}
