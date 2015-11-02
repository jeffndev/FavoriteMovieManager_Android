package com.jeffndev.favoritemoviesmanager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeffndev.favoritemoviesmanager.models.Movie;
import com.jeffndev.favoritemoviesmanager.tasks.FetchImageFromUrlTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {
    final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    public MovieListAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie data = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.movie_list_item_movie_title);
            viewHolder.tvPosterImage = (ImageView)convertView.findViewById(R.id.movie_list_item_image_view);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //take the viewholder's items and set the image and text...
        viewHolder.tvTitle.setText(data.title);
        //LEAVE posterimage empty.. viewHolder.tvPosterImage.setImageBitmap(null);
        FetchImageFromUrlTask fetchImageFromUrlTask = new FetchImageFromUrlTask(viewHolder.tvPosterImage);
        final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/";
        final String CELL_IMAGE_SIZE_FORMAT = "w154";
        Uri uri = Uri.parse( BASE_IMAGE_PATH + CELL_IMAGE_SIZE_FORMAT + "/" + data.posterPath);
        try {
            URL imageUrl = new URL(uri.toString());
            fetchImageFromUrlTask.execute(new URL[] {imageUrl});
        } catch( IOException e){
            Log.v(LOG_TAG, "Could not parse out URL to movie image: " + uri.toString());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        ImageView tvPosterImage;
    }
}
