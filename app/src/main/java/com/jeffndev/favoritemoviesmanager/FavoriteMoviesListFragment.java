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
import com.jeffndev.favoritemoviesmanager.tasks.ListUserFavoriteMoviesTask;
import com.jeffndev.favoritemoviesmanager.tasks.MovieListTask;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteMoviesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteMoviesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteMoviesListFragment extends Fragment
        implements MovieListTask.MovieListTaskCallbacks {
        private final String LOG_TAG = FavoriteMoviesListFragment.class.getSimpleName();


        private ListView mListView;
        private MovieListAdapter mMovieListAdapter;

        public FavoriteMoviesListFragment() {
            // Required empty public constructor

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_movie_list, container, false);



            mListView = (ListView)view.findViewById(R.id.movies_list_list_view);
            ListUserFavoriteMoviesTask fetchFavsTask = new ListUserFavoriteMoviesTask(this);
            fetchFavsTask.execute();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.v(LOG_TAG, "CHOSEN ITEM INDEX: " + position);
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
            if( l != null) {
                mMovieListAdapter = new MovieListAdapter(getContext(), l);
                mListView.setAdapter(mMovieListAdapter);
                Log.v(LOG_TAG, "Movies fetched: " + l.size());
            }else {
                Log.v(LOG_TAG, "Empty movie list returned to onFetchMovies, investigate bad API call");
            }
        }
}
