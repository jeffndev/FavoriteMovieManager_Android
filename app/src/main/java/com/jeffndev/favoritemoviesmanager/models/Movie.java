package com.jeffndev.favoritemoviesmanager.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class Movie {
    public String title;
    public int id;
    public String posterPath;

    public Movie(int id, String title, String posterPath){
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

    public Movie(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.title = object.getString("title");
            this.posterPath  = object.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Movie> fromJson(JSONArray jsonObjects) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                movies.add(new Movie(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

}
