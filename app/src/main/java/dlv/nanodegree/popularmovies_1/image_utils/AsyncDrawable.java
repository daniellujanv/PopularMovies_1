package dlv.nanodegree.popularmovies_1.image_utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

import dlv.nanodegree.popularmovies_1.tasks.BitmapWorkerTask;

/**
 *
 * Code obtained from http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 *
 * Created by daniellujanvillarreal on 8/10/15.
 */

public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
                         BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference =
                new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }
}