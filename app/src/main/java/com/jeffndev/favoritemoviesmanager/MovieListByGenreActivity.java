package com.jeffndev.favoritemoviesmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class MovieListByGenreActivity extends AppCompatActivity {
            //implements MovieListTask.MovieListTaskCallbacks { //MARK: transferred this to Fragment

    private final String LOG_TAG = MovieListByGenreActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    //TODO: move to Fragment
    /*
    private ListView mListView;
    private MovieListAdapter mMovieListAdapter;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bygenre_movie_list);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        //TODO: move to Fragment
        /*
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
        */
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        MovieListByGenreFragment fragAction = new MovieListByGenreFragment();
        Bundle args = new Bundle();
        args.putInt(MovieListByGenreFragment.MOVIE_GENRE_KEY, MovieListByGenreFragment.MOVIE_GENRE_ACTION);
        fragAction.setArguments(args);
        adapter.addFragment(fragAction, "Action"); //TODO: fix this, the Label of the tab shouldn't be hidden here, and should be in RESOURCES, too
        MovieListByGenreFragment fragScifi = new MovieListByGenreFragment();
        args = new Bundle();
        args.putInt(MovieListByGenreFragment.MOVIE_GENRE_KEY, MovieListByGenreFragment.MOVIE_GENRE_SCIFI);
        fragScifi.setArguments(args);
        adapter.addFragment(fragScifi, "Sci-Fi"); //TODO: fix this, the Label of the tab shouldn't be hidden here
        MovieListByGenreFragment fragComedy = new MovieListByGenreFragment();
        args = new Bundle();
        args.putInt(MovieListByGenreFragment.MOVIE_GENRE_KEY, MovieListByGenreFragment.MOVIE_GENRE_COMEDY);
        fragComedy.setArguments(args);
        adapter.addFragment(fragComedy, "Comedy"); //TODO: fix this, the Label of the tab shouldn't be hidden here

        FavoriteMoviesListFragment fragFavs = new FavoriteMoviesListFragment();
        adapter.addFragment(fragFavs, "Favorites");//TODO: fix this, the Label of the tab shouldn't be hidden here
        mViewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    /* moved to Fragment
    @Override
    public void onFetchedMovies(ArrayList<Movie> l) {
        mMovieListAdapter = new MovieListAdapter(this, l);
        mListView.setAdapter(mMovieListAdapter);
        Log.v(LOG_TAG, "Movies fetched: " + l.size());
    }
    */
}
