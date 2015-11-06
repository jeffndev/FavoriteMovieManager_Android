package com.jeffndev.favoritemoviesmanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeffndev.favoritemoviesmanager.models.Movie;
import com.jeffndev.favoritemoviesmanager.tasks.MovieListTask;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListByGenreFragment extends Fragment
        implements MovieListTask.MovieListTaskCallbacks{
    private final String LOG_TAG = MovieListByGenreFragment.class.getSimpleName();
    //TODO: implement this as a MOVIE_GENRE enum, and with .rawValue implementations to get the int
    public static final int MOVIE_GENRE_ACTION = 28;
    public static final int MOVIE_GENRE_SCIFI = 878;
    public static final int MOVIE_GENRE_COMEDY = 35;
    public static final String MOVIE_GENRE_KEY = "movie_genre_key";
    private int mMovieGenre = -1;

    private ListView mListView;
    private MovieListAdapter mMovieListAdapter;

    public MovieListByGenreFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        Bundle args  = getArguments();
        if(args != null && args.containsKey(MOVIE_GENRE_KEY)) {
            mMovieGenre = args.getInt(MOVIE_GENRE_KEY);
        }
        mListView = (ListView)view.findViewById(R.id.movies_list_list_view);
        MovieListTask fetchMoviesTask = new MovieListTask(this);
        fetchMoviesTask.execute(new Integer[]{mMovieGenre});
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(LOG_TAG,"CHOSEN ITEM INDEX: " + position);
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.CURRENT_MOVIE_PARCELABLE_KEY, movie);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//? or maybe also FLAG_ACTIVITY_NEW_TASK to not have it go to backstack
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onFetchedMovies(ArrayList<Movie> l) {
        mMovieListAdapter = new MovieListAdapter(getContext(), l);
        mListView.setAdapter(mMovieListAdapter);
        Log.v(LOG_TAG, "Movies (genre: " + mMovieGenre + ") fetched: " + l.size());
    }
}
