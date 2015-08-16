package dlv.nanodegree.popularmovies_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import dlv.nanodegree.popularmovies_1.DetailActivity;
import dlv.nanodegree.popularmovies_1.R;
import dlv.nanodegree.popularmovies_1.classes.Movie;
import dlv.nanodegree.popularmovies_1.image_utils.AsyncDrawable;
import dlv.nanodegree.popularmovies_1.image_utils.ImageLoadUtils;
import dlv.nanodegree.popularmovies_1.tasks.BitmapWorkerTask;

/**
 * Created by DanielLujanApps on Monday10/08/15.
 */
public class ImageAdapter extends BaseAdapter  {
    private String TAG = getClass().getSimpleName();
    private ArrayList<Movie> alMovies;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context, LayoutInflater layoutInflater){
        mContext = context;
        alMovies = new ArrayList<>();
        mLayoutInflater = layoutInflater;
    }

    public void updateMovies(ArrayList<Movie> movies){
        Log.i(TAG, "updating #movies:: " + movies.size());
        alMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount #movies:: " + alMovies.size());
        return alMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        ImageView imageView;
        int width = 120;
        int height = 120;
        if(convertView == null){
            //not recycled
            view = mLayoutInflater.inflate(R.layout.gridview_item, parent, false);
            imageView = (ImageView) view.findViewById(R.id.imageview_gv_item);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            view = convertView;
            imageView = (ImageView) view.findViewById(R.id.imageview_gv_item);
        }

        width = imageView.getMeasuredWidth();
        height = imageView.getMeasuredHeight();
        URL posterUrl = alMovies.get(position).getmPosterUrl();
        if (ImageLoadUtils.cancelPotentialWork(posterUrl, imageView)) {

            Log.d(TAG, "width :: "+width +"... height :: "+height);

            final BitmapWorkerTask workerTask = new BitmapWorkerTask( imageView, width, height);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), null, workerTask);
            imageView.setImageDrawable(asyncDrawable);
            workerTask.execute(posterUrl);
        }

        final int i = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("movie", alMovies.get(i));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

                Log.i("ImageAdapter", "OnClickListener - fullScreenImage");
            }
        });

        return view;
    }
}
