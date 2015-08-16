package dlv.nanodegree.popularmovies_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import dlv.nanodegree.popularmovies_1.adapters.ImageAdapter;
import dlv.nanodegree.popularmovies_1.tasks.LoadMoviesTask;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gridview
        GridView gridView = (GridView)findViewById(R.id.gridview_thumbnails);
        mImageAdapter = new ImageAdapter(getApplicationContext(), getLayoutInflater());
        gridView.setAdapter(mImageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        onOptionsItemSelected(menu.findItem(R.id.action_popular));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            Log.i(TAG, "action_popular selected");
            item.setChecked(true);
            //load data with task
            new LoadMoviesTask(getApplicationContext(), mImageAdapter).execute(true);
            return true;
        }else if(id == R.id.action_highest_rated){
            Log.i(TAG, "action_highest_rated selected");
            item.setChecked(true);
            new LoadMoviesTask(getApplicationContext(), mImageAdapter).execute(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
