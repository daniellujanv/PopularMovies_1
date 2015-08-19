package dlv.nanodegree.popularmovies_1.tasks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import dlv.nanodegree.popularmovies_1.classes.Movie;

/**
 * Code obtained from http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 *
 * Created by daniellujanvillarreal on 8/10/15.
 */
public class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap[]> {
    private String TAG = getClass().getSimpleName();
    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<Movie> movieWeakReference;
    public URL imageUrl;
    private int mWidth;
    private int mHeight;
    private boolean mInDetails;

    public BitmapWorkerTask(
            ImageView imageView, int width, int height, Movie movie, boolean inDetails) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<>(imageView);
        movieWeakReference = new WeakReference<>(movie);
        mWidth = width;
        mHeight = height;
        mInDetails = inDetails;
    }

    // Decode image in background.

    /**
     * Here we try to load both :: poster and thumbnail.
     * Parcelable Movie object gets less complicated that way.
     * We also save network operations.
     *
     * @param params
     * @return
     */
    @Override
    protected Bitmap[] doInBackground(Void... params) {
        InputStream inputStream = null;
        Movie movie = movieWeakReference.get();
        Bitmap[] result = new Bitmap[0];

        try {
            Log.i(TAG, "opening stream :: " + imageUrl);
            //load movie poster
            imageUrl = movie.getmPosterUrl();
            inputStream = imageUrl.openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (mWidth != 0 && mHeight != 0) {
                options.outHeight = mHeight;
                options.outWidth = mWidth;
            }
            Bitmap bitmapPoster = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            //load movie thumbnail
            imageUrl = movie.getThumbnailUrl();
            inputStream = imageUrl.openStream();
            options = new BitmapFactory.Options();
            if (mWidth != 0 && mHeight != 0) {
                options.outHeight = mHeight;
                options.outWidth = mWidth;
            }
            Bitmap bitmapThumbnail = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            //return both
            result = new Bitmap[2];
            result[0] = bitmapPoster;
            result[1] = bitmapThumbnail;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override

    //0 --> Poster
    //1 --> Thumbnail
    protected void onPostExecute(Bitmap[] bitmaps) {
        if (imageViewReference != null && bitmaps.length != 0) {
            try {
                //thumbnail
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps[1].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
//                Log.i(TAG, "writting thumbnail byteArray");
                movieWeakReference.get().setThumbnailByteArray(byteArray);
                stream.close();
                //poster
                stream = new ByteArrayOutputStream();
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
//                Log.i(TAG, "writting poster byteArray");

                movieWeakReference.get().setPosterByteArray(byteArray);
                //movie byte arrays have been loaded
                movieWeakReference.get().setLoadedTrue();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if(mInDetails) {
                    Log.i(TAG, "setting thumbnail");
                    imageView.setImageBitmap(bitmaps[1]);
                }else{
                    Log.i(TAG, "setting poster");
                    imageView.setImageBitmap(bitmaps[0]);
                }

            }else{
            }


        }
    }

}