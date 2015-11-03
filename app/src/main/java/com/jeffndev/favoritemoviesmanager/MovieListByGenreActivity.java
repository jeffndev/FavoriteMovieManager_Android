package com.jeffndev.favoritemoviesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Intent intent = new Intent(MovieListByGenreActivity.this, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.CURRENT_MOVIE_PARCELABLE_KEY, movie);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//? or maybe also FLAG_ACTIVITY_NEW_TASK to not have it go to backstack
                startActivity(intent);
            }
        });
        //Snackbar.make(findViewById(R.id.content), "REACHED MOVIE LIST!!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchedMovies(ArrayList<Movie> l) {
        mMovieListAdapter = new MovieListAdapter(this, l);
        mListView.setAdapter(mMovieListAdapter);
        Log.v(LOG_TAG, "Movies fetched: " + l.size());
    }
}
