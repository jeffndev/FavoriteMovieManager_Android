package com.jeffndev.favoritemoviesmanager.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.jeffndev.favoritemoviesmanager.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class MovieListTask extends AsyncTask<Integer, Void, Void> {
    final String LOG_TAG = MovieListTask.class.getSimpleName();
    ArrayList<Movie> mMovies = null;
    MovieListTaskCallbacks mCaller = null;

    public interface MovieListTaskCallbacks {
        void onFetchedMovies(ArrayList<Movie> l);
    }

    public MovieListTask(MovieListTaskCallbacks caller){
        mCaller = caller;
    }

    protected Void doInBackground(Integer... params) {
        Integer genreId = params[0];
        final String BASE_URL = "https://api.themoviedb.org/3/";
        final String MOVIES_OF_GENRE_LIST_METHOD = "genre/" + genreId + "/movies";
        final String API_KEY_PARAM = "api_key";
        final String API_KEY_VALUE = "21ac243915da272c3c6b92a9958a3650";
//        final String REQUEST_TOKEN_PARAM = "request_token";
//        final String SESSION_ID_PARAM = "session_id";
//        final String USER_NAME_PARAM = "username";
//        final String PASSWORD_PARAM = "password";

        BufferedReader reader = null;
        HttpsURLConnection urlConnection = null;
        String moviesListJson = null;
        Uri builtUri = Uri.parse(BASE_URL+MOVIES_OF_GENRE_LIST_METHOD).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e(LOG_TAG, "INPUT STREAM from http request returned nothing");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            while((ln = reader.readLine()) != null){
                buffer.append(ln + "\n");
            }
            if(buffer.length() == 0){
                Log.e(LOG_TAG, "empty buffer from http request, no data found");
                return null;
            }
            moviesListJson = buffer.toString();
        }catch(IOException e){
            Log.e(LOG_TAG, "Error ", e);
            return null;
        }
        try {
            JSONObject moviesListObject = new JSONObject(moviesListJson);
            int page = moviesListObject.getInt("page");
            int pages = moviesListObject.getInt("total_pages");
            JSONArray results = moviesListObject.getJSONArray("results");
            mMovies = Movie.fromJson(results);
        }catch(JSONException e){
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mCaller != null){
            mCaller.onFetchedMovies(mMovies);
        }
    }
}
