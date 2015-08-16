package dlv.nanodegree.popularmovies_1.tasks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Code obtained from http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 *
 * Created by daniellujanvillarreal on 8/10/15.
 */
public class BitmapWorkerTask extends AsyncTask<URL, Void, Bitmap> {
    private String TAG = getClass().getSimpleName();
    private final WeakReference<ImageView> imageViewReference;
    public URL imageUrl;
    private int mWidth;
    private int mHeight;

    public BitmapWorkerTask(ImageView imageView, int width, int height) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        mWidth = width;
        mHeight = height;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(URL... params) {
        imageUrl = params[0];
        InputStream inputStream = null;
        try {
            Log.i(TAG,"opening stream :: "+ imageUrl);
            inputStream = imageUrl.openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            if(mWidth != 0 && mHeight != 0) {
                options.outHeight = mHeight;
                options.outWidth = mWidth;
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

            inputStream.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                Log.i(TAG,"setting bitmap");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}