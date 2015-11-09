package com.jeffndev.favoritemoviesmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class Movie implements Parcelable {
    public String title;
    public int id;
    public String posterPath;

    //TODO: Movie Genre enum with map to MoveDB keys and a short descr

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();

    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };


    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
    }

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
