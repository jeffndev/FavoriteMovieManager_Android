package com.jeffndev.favoritemoviesmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeffndev.favoritemoviesmanager.models.Movie;
import com.jeffndev.favoritemoviesmanager.tasks.FetchImageFromUrlTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jeffreynewell1 on 11/2/15.
 */
public class MovieDetailActivity extends AppCompatActivity {
    public final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    public static final String CURRENT_MOVIE_PARCELABLE_KEY = "CURRENT_MOVIE_DETAIL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundleSent = intent.getExtras();
        Movie movie = bundleSent.getParcelable(CURRENT_MOVIE_PARCELABLE_KEY);

        setContentView(R.layout.activity_movie_detail);
        TextView tvTitle = (TextView)findViewById(R.id.movie_detail_title_lable);
        tvTitle.setText(movie.title);
        ImageView imMovieImage = (ImageView)findViewById(R.id.movie_detail_image_view);
        //have to send to an aysnc task to get the poster image...
        FetchImageFromUrlTask fetchImageFromUrlTask = new FetchImageFromUrlTask(imMovieImage);
        final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/";
        final String CELL_IMAGE_SIZE_FORMAT = "w342";
        Uri uri = Uri.parse( BASE_IMAGE_PATH + CELL_IMAGE_SIZE_FORMAT + "/" + movie.posterPath);
        try {
            URL imageUrl = new URL(uri.toString());
            fetchImageFromUrlTask.execute(new URL[] {imageUrl});
        } catch( IOException e){
            Log.v(LOG_TAG, "Could not parse out URL to movie image: " + uri.toString());
        }
    }
}
