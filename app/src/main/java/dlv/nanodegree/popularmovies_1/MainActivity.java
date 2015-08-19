package dlv.nanodegree.popularmovies_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

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
            onOptionsItemSelected(menu.findItem(R.id.action_popular));
        }else{
            menu.findItem(mSelectedItemId).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_popular) {
            Log.i(TAG, "action_popular selected");
//            item.setChecked(true);
            //load data with task
            new LoadMoviesTask(getApplicationContext(), mImageAdapter, item).execute(true);
            mGridView.smoothScrollToPosition(0);
            return true;
        }else if(item.getItemId() == R.id.action_highest_rated){
            Log.i(TAG, "action_highest_rated selected");
//            item.setChecked(true);
            new LoadMoviesTask(getApplicationContext(), mImageAdapter, item).execute(false);
            mGridView.smoothScrollToPosition(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG, "selectedItem :: " + mSelectedItemId);
        for(int i=0; i<mMenu.size(); i++){
            if(mMenu.getItem(i).isChecked()){
                mSelectedItemId = mMenu.getItem(i).getItemId();
                break;
            }
        }
        Log.i(TAG,"outstate putting selectedItem :: "+mSelectedItemId);
//        if(mSelectedItemId != -1) {
        outState.putInt(selectedItemIdKey, mSelectedItemId);
//        }
        ArrayList<Movie> movies = mImageAdapter.getMovies();
        Log.i(TAG, "outstate putting movies :: "+movies.size());
        outState.putParcelableArrayList(moviesKey, movies);
    }
}
