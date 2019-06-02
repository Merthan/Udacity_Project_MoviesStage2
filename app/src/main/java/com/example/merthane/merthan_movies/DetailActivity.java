package com.example.merthane.merthan_movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    Intent INTENT;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

       INTENT= getIntent();

        TextView textView = findViewById(R.id.message);

        ((TextView) findViewById(R.id.title)).setText(INTENT.getStringExtra("title"));
        ((TextView) findViewById(R.id.release_date)).setText(INTENT.getStringExtra("date"));
        ((TextView) findViewById(R.id.rating)).setText(INTENT.getStringExtra("vote_average"));


        if(Double.parseDouble(((TextView) findViewById(R.id.rating)).getText().toString())>7){
            ((TextView) findViewById(R.id.rating)).setTextColor(Color.GREEN);
        }else if(Double.parseDouble(((TextView) findViewById(R.id.rating)).getText().toString())>5){
            ((TextView) findViewById(R.id.rating)).setTextColor(Color.YELLOW);
        }else{
            ((TextView) findViewById(R.id.rating)).setTextColor(Color.RED);
        }

        button=((Button) findViewById(R.id.button));
        button.setText(checkIfInDbAlready()?R.string.remove_from_favorites:R.string.save_as_favorite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfInDbAlready()){
                    removeFromContentProvider();
                }else{
                    saveInContentProvider();
                }
            }
        });






        ((TextView) findViewById(R.id.plot_synopsis)).setText(INTENT.getStringExtra("plot_synopsis"));

        Picasso.with(this).load(INTENT.getStringExtra("imageString")).into((ImageView) findViewById(R.id.poster));

        final LinearLayout linOne = findViewById(R.id.linear_one);
        final LinearLayout linTwo = findViewById(R.id.linear_two);

        new FetchVideos(INTENT.getStringExtra("id"), new FetchVideos.FetchVideosInterface() {
            @Override
            public void onFinished(HashMap<String, String> keyAndTitle) {

                for(Map.Entry<String,String> entry : keyAndTitle.entrySet()) {

                    TextView title = new TextView(DetailActivity.this);


                    final String key = entry.getKey();
                    String value = entry.getValue();
                    title.setText(value);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                           try{
                               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+key)));

                           }catch (Exception E){
                               E.printStackTrace();
                           }
                        }
                    });
                    TextView space=new TextView(DetailActivity.this);
                    space.setText("  ");

                    linOne.addView(space);
                    linOne.addView(title);
                }
            }
        }).execute();


        new FetchReviews(INTENT.getStringExtra("id"), new FetchReviews.FetchReviewsInterface() {
            @Override
            public void onFinished(ArrayList<Review> reviewList) {
                if(reviewList==null)return;
                for(Review r:reviewList){
                    TextView T = new TextView(DetailActivity.this);
                    String text = r.author+":\n"+r.text;
                    T.setText(text);


                    linTwo.addView(T);

                    ScrollView scrollView = findViewById(R.id.scrolldetail);
                    scrollView.scrollTo(0,restorePostition);
                }

            }
        }).execute();

    }

    private void saveInContentProvider(){
        try{
            if(INTENT==null)return;

            ContentValues cv = new ContentValues();//Saving title&id in CV
            cv.put(ContractClass.MyEntry.COLUMN_TITLE,INTENT.getStringExtra("title"));
            cv.put(ContractClass.MyEntry.COLUMN_MOVIE_ID,INTENT.getStringExtra("id"));

            //Saving it
            getContentResolver().insert(ContractClass.MyEntry.CONTENT_URI_FAVORITES,cv);
            Toast.makeText(this,INTENT.getStringExtra("title")+"\nSaved as favorite",Toast.LENGTH_SHORT).show();
            button.setText(R.string.remove_from_favorites);
        }catch (Exception E){
            Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show();
        }

    }

    private void removeFromContentProvider(){

        if(INTENT==null)return;

        getContentResolver().delete(ContractClass.MyEntry.CONTENT_URI_FAVORITES,ContractClass.MyEntry.COLUMN_MOVIE_ID+"="+INTENT.getStringExtra("id"),null);
        Toast.makeText(this,"Removed Favorite.",Toast.LENGTH_SHORT).show();
        button.setText(R.string.save_as_favorite);

    }

    private boolean checkIfInDbAlready(){


        Cursor cursor = getContentResolver().query(ContractClass.MyEntry.CONTENT_URI_FAVORITES, null,ContractClass.MyEntry.COLUMN_MOVIE_ID+" = "+INTENT.getStringExtra("id"),null,null);

        if(cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        restorePostition=0;
        ScrollView scrollView = findViewById(R.id.scrolldetail);
        outState.putInt("SCROLL_POSITION", scrollView.getScrollY());
    }
    int restorePostition;
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restorePostition = savedInstanceState.getInt("SCROLL_POSITION");
        /*if(position != null)
            mScrollView.post(new Runnable() {
                    mScrollView.scrollTo(position[0], position[1]);
            });*/
    }



}
