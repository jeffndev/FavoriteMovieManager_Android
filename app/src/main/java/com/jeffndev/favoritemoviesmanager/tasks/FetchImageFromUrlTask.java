package com.jeffndev.favoritemoviesmanager.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by jeffreynewell1 on 11/1/15.
 */
public class FetchImageFromUrlTask extends AsyncTask<URL, Void, Void> {
    final String LOG_TAG = FetchImageFromUrlTask.class.getSimpleName();
    private Bitmap mImageBitmap = null;
    private ImageView mImageViewToSet = null;

    public FetchImageFromUrlTask(ImageView imageView){
        mImageViewToSet = imageView;
    }
    @Override
    protected Void doInBackground(URL... params) {
        URL photoUrl = params[0];
        try {
            InputStream inputStream = photoUrl.openStream();
            mImageBitmap = BitmapFactory.decodeStream(inputStream);
        }catch(IOException e){
            Log.v(LOG_TAG, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mImageViewToSet != null && mImageBitmap != null){
            mImageViewToSet.setImageBitmap(mImageBitmap);
        }
    }
}
