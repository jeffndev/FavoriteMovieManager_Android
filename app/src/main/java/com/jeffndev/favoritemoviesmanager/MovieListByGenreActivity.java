package com.jeffndev.favoritemoviesmanager;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.jeffndev.favoritemoviesmanager.models.Movie;
import com.jeffndev.favoritemoviesmanager.tasks.MovieListTask;

import java.util.ArrayList;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class MovieListByGenreActivity extends AppCompatActivity
            implements MovieListTask.MovieListTaskCallbacks {
    private final String LOG_TAG = MovieListByGenreActivity.class.getSimpleName();
    private ListView mListView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bygenre_movie_list);

        mListView = (ListView)findViewById(R.id.movies_list_over_genres_list_view);
        MovieListTask fetchMoviesTask = new MovieListTask(this);
        final Integer GENRE_ACTION = 28;
        fetchMoviesTask.execute(new Integer[]{GENRE_ACTION});
        Snackbar.make(findViewById(R.id.content), "REACHED MOVIE LIST!!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchedMovies(ArrayList<Movie> l) {
        mMovieListAdapter = new MovieListAdapter(this, l);
        mListView.setAdapter(mMovieListAdapter);
        Log.v(LOG_TAG, "Movies fetched: " + l.size());
    }
}
