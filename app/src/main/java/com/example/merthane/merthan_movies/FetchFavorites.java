package com.example.merthane.merthan_movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FetchFavorites extends AsyncTask<Void,Void,ArrayList<Movie>> {



    final static String API_KEY = MainActivity.API_KEY;
    final static String BASE_URL ="https://api.themoviedb.org/3/movie/";



    MyAdapter myAdapter;
    private Context context;
    private String[] movieIds;
    FetchFavorites(MyAdapter myAdapter, Context context, String[] movieIds){
        this.context=context;
        this.myAdapter=myAdapter;
        this.movieIds=movieIds;
    }
    @Override
    protected ArrayList<Movie> doInBackground(Void... myAdapters) {

        ArrayList<Movie> movies= new ArrayList<>();

        try {

            for(String movieID:movieIds){
                if(movieID==null)continue;
                Uri uri = Uri.parse(BASE_URL+movieID+"?api_key="+API_KEY+"&language=en-US");

                JSONObject jsonObject= new JSONObject( getResponseFromUrl(new URL(uri.toString()))   );
                movies.add(new Movie(jsonObject,context));






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

    public String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        String s="";
        try{

            Scanner scanner=new Scanner(urlConnection.getInputStream());
            while (scanner.hasNextLine()){
                s+=scanner.nextLine();//i know this isnt optimal, but it works
            }
        }finally {
            Log.d("urlll",s);
            urlConnection.disconnect();
        }
        return s;
    }
}
