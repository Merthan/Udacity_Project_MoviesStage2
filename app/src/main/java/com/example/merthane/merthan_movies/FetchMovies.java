package com.example.merthane.merthan_movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by MerthanE on 16.02.2018.
 */

public class FetchMovies extends AsyncTask<Void,Void,ArrayList<Movie>> {


    String finalUrl ="";
    final static String API_KEY = MainActivity.API_KEY;
    final static String BASE_URL ="https://api.themoviedb.org/3/movie/";
    final static String RESULTS = "results";

    boolean sortByRating;
    MyAdapter myAdapter;
    private Context context;

    FetchMovies(MyAdapter myAdapter, Context context, boolean sort_by_rating){
        this.context=context;
        this.myAdapter=myAdapter;
        this.sortByRating =sort_by_rating;
        finalUrl = BASE_URL+(sort_by_rating?"top_rated":"popular")+"?api_key="+API_KEY+"&language=en-US&page=1";
    }
    @Override
    protected ArrayList<Movie> doInBackground(Void... myAdapters) {

        ArrayList<Movie> movies= new ArrayList<>();
        Uri uri = Uri.parse(finalUrl);


        try {
            JSONObject jsonObject= new JSONObject( getResponseFromUrl(new URL(uri.toString()))   );

            JSONArray results =jsonObject.getJSONArray(RESULTS);
            Log.d("urlll",results.length()+results.toString());

            for(int i=0;i<results.length();i++){
                JSONObject j=results.getJSONObject(i);
                Log.d("urlll","123"+j.toString());

                movies.add(new Movie(j,context));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        String[] data=new String[movies.size()];
        for(int i=0;i<movies.size();i++){
            data[i]=movies.get(i).getImageString();
        }
        myAdapter.setData(data);
        myAdapter.setMovieArray(movies.toArray(new Movie[0]));
        myAdapter.notifyDataSetChanged();


    }

    public String getResponseFromUrl(URL url) throws IOException{
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
