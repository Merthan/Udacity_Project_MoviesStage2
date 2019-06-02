package com.example.merthane.merthan_movies;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.merthane.merthan_movies.FetchMovies.RESULTS;

public class FetchReviews extends AsyncTask<Void,Void,Void> {

    public interface FetchReviewsInterface{
        void onFinished(ArrayList<Review> reviewList);
    }

    final static String API_KEY = MainActivity.API_KEY;
    final static String BASE_URL ="https://api.themoviedb.org/3/movie/";
    final static String REVIEWS_URL="/reviews?api_key=";
    final static String LANGUAGE_URL="&language=en-US";



    private FetchReviewsInterface fetchReviewsInterface=null;
    private String movieId;
    FetchReviews(String movieId,FetchReviewsInterface fetchReviewsInterface){
        this.fetchReviewsInterface=fetchReviewsInterface;

        this.movieId=movieId;
    }

    ArrayList<Review> reviewList= new ArrayList<>();
    @Override
    protected Void doInBackground(Void... myAdapters) {

        reviewList=new ArrayList<>();

        //Uri uri = Uri.parse(finalUrl);




        try {


            if(movieId==null)return null;
            Uri uri = Uri.parse(BASE_URL+movieId+REVIEWS_URL+API_KEY+LANGUAGE_URL);

            JSONObject jsonObject= new JSONObject( getResponseFromUrl(new URL(uri.toString()))   );
            JSONArray results =jsonObject.getJSONArray(RESULTS);
            //resultName=result.getString("name");
            //resultKey=result.getString("key");


            for(int i=0;i<results.length();i++){
                JSONObject j=results.getJSONObject(i);


                reviewList.add(new Review(j.getString("author"),j.getString("content")));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
            fetchReviewsInterface.onFinished(reviewList);
    }

    public String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        String s="";
        try{

            Scanner scanner=new Scanner(urlConnection.getInputStream());
            while (scanner.hasNextLine()){
                s+=scanner.nextLine();//i know this isnt optimal, but it works
            }
        }finally {

            urlConnection.disconnect();
        }
        return s;
    }
}
class Review{
    String author;
    String text;
    public Review(String author,String text){
        this.author=author;this.text=text;
    }
}