package com.example.merthane.merthan_movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import static com.example.merthane.merthan_movies.FetchMovies.RESULTS;

public class FetchVideos extends AsyncTask<Void,Void,Void> {

    public interface FetchVideosInterface{
        void onFinished(HashMap<String,String> keyAndTitle);
    }

    final static String API_KEY = MainActivity.API_KEY;
    final static String BASE_URL ="https://api.themoviedb.org/3/movie/";
    final static String VIDEOS_URL="/videos?api_key=";
    final static String LANGUAGE_URL="&language=en-US";



    private FetchVideosInterface fetchVideosInterface=null;
    private String movieId;
    FetchVideos(String movieId,FetchVideosInterface fetchVideosInterface){
        this.fetchVideosInterface=fetchVideosInterface;

        this.movieId=movieId;
    }

    HashMap<String,String> keyAndTitleMap = new HashMap<>();
    @Override
    protected Void doInBackground(Void... myAdapters) {







        try {


                if(movieId==null)return null;
                Uri uri = Uri.parse(BASE_URL+movieId+VIDEOS_URL+API_KEY+LANGUAGE_URL);

                JSONObject jsonObject= new JSONObject( getResponseFromUrl(new URL(uri.toString()))   );


            JSONArray results =jsonObject.getJSONArray(RESULTS);
            Log.d("urlll",results.length()+results.toString());

            for(int i=0;i<results.length();i++){
                JSONObject result=results.getJSONObject(i);

                keyAndTitleMap.put(result.getString("key"),result.getString("name"));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        fetchVideosInterface.onFinished(keyAndTitleMap);
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
