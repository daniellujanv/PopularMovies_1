package dlv.nanodegree.popularmovies_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import dlv.nanodegree.popularmovies_1.classes.Movie;
import dlv.nanodegree.popularmovies_1.tasks.BitmapWorkerTask;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovie = getIntent().getParcelableExtra(Movie.MOVIE_KEY);

        ((TextView) findViewById(R.id.tv_title)).setText(mMovie.getTitle());
        ((TextView) findViewById(R.id.tv_date)).setText(mMovie.getReleaseDate());
        ((TextView) findViewById(R.id.tv_rating)).setText(""+mMovie.getRating()+" out of 10");
        ((TextView) findViewById(R.id.tv_synopsis)).setText(mMovie.getSynopsis());

        ImageView imageView = (ImageView) findViewById(R.id.iv_details_image);

        byte[] imageBytes = mMovie.getThumbnailByteArray();
        //no need to fetch movie thumbnail
        Bitmap bitmap= BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
