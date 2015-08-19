package dlv.nanodegree.popularmovies_1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import dlv.nanodegree.popularmovies_1.adapters.ImageAdapter;
import dlv.nanodegree.popularmovies_1.classes.Movie;
import dlv.nanodegree.popularmovies_1.tasks.LoadMoviesTask;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private ImageAdapter mImageAdapter;
    private GridView mGridView;
    private int mSelectedItemId = -1;
    private Menu mMenu;
    private String selectedItemIdKey = "selectedMenuItemId";
    //keep a reference to the movies in order to save in instanceState
    private String moviesKey = "movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gridview
        mGridView = (GridView)findViewById(R.id.gridview_thumbnails);
        mImageAdapter = new ImageAdapter(getApplicationContext(), getLayoutInflater());
        mGridView.setAdapter(mImageAdapter);

        /**
         * if savedInstance not null
         **  --> set saved selectedItemId and Movies array list
         * if saved instance == null
         **  --> since init mSelectedItemId = -1 and LoadMoviesTask(sort_popular = true) will be executed
         *  in onCreateOptionsMenu()

         */
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(selectedItemIdKey)) {
                mSelectedItemId = savedInstanceState.getInt(selectedItemIdKey);
                Log.i(TAG, "savedInstaceState fetched mSelectedItemId :: " + mSelectedItemId);
            }
            if(savedInstanceState.containsKey(moviesKey)){
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(moviesKey);
                mImageAdapter.updateMovies(movies);
                Log.i(TAG, "savedInstaceState fetched movies :: " + movies.size());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;

        if(mSelectedItemId == -1){
            //app just launched and there is nothing selected --> select popular sort order
            onOptionsItemSelected(menu.findItem(R.id.action_popular));
        }else{
            menu.findItem(mSelectedItemId).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * check if previously selected sort order is the same as currently selected
         */
        if(isNetworkAvailable()) {
            if(mSelectedItemId != item.getItemId()) {
                mSelectedItemId = item.getItemId();
                if (mSelectedItemId == R.id.action_popular) {
                    Log.i(TAG, "action_popular selected");
                    //load data with task.execute(popupar = true)
                    new LoadMoviesTask(getApplicationContext(), mImageAdapter, item).execute(true);
                    mGridView.smoothScrollToPosition(0);
                    return true;
                } else if (mSelectedItemId == R.id.action_highest_rated) {
                    Log.i(TAG, "action_highest_rated selected");
                    //load data with task.execute(popupar = false)
                    new LoadMoviesTask(getApplicationContext(), mImageAdapter, item).execute(false);
                    mGridView.smoothScrollToPosition(0);
                    return true;
                }
            }
        }
        else {
            showToast("Network not available. Check connection or try again later");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //save selected menu item id
        Log.i(TAG, "selectedItem :: " + mSelectedItemId);
        for(int i=0; i<mMenu.size(); i++){
            if(mMenu.getItem(i).isChecked()){
                mSelectedItemId = mMenu.getItem(i).getItemId();
                break;
            }
        }
        Log.i(TAG,"outstate putting selectedItem :: "+mSelectedItemId);
        outState.putInt(selectedItemIdKey, mSelectedItemId);
        //save movie list
        ArrayList<Movie> movies = mImageAdapter.getMovies();
        Log.i(TAG, "outstate putting movies :: "+movies.size());
        outState.putParcelableArrayList(moviesKey, movies);
    }

    //Based on a stackoverflow snippet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
