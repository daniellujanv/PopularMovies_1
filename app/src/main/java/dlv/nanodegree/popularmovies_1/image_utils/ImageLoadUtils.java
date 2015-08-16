package dlv.nanodegree.popularmovies_1.image_utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import java.net.URL;

import dlv.nanodegree.popularmovies_1.tasks.BitmapWorkerTask;


/**
 *
 * Code obtained from http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 *
 * Created by daniellujanvillarreal on 8/10/15.
 */
public class ImageLoadUtils {

    public static boolean cancelPotentialWork(URL url, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final URL bitmapUrl = bitmapWorkerTask.imageUrl;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapUrl == null || !bitmapUrl.equals(url)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

}
