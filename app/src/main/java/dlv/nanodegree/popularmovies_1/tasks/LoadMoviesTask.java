package dlv.nanodegree.popularmovies_1.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import dlv.nanodegree.popularmovies_1.adapters.ImageAdapter;
import dlv.nanodegree.popularmovies_1.classes.Movie;

/**
 * Created by DanielLujanApps on Monday10/08/15.
 */
public class LoadMoviesTask extends AsyncTask<Boolean, String, ArrayList<Movie>> {
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private WeakReference<ImageAdapter> mImageAdapter;
    private WeakReference<MenuItem> mMenuItem;
    //TODO remove key
    private final String API_KEY = ""; //INSERT API KEY FOR themoviedb HERE

    public LoadMoviesTask(Context context, ImageAdapter imageAdapter
            , MenuItem menuItem){
        mContext = context;
        mImageAdapter = new WeakReference<>(imageAdapter);
        mMenuItem = new WeakReference<>(menuItem);
    }

    @Override
    protected ArrayList<Movie> doInBackground(Boolean... params) {
        boolean fetchPopularMovies = params[0];

        if(fetchPopularMovies){
            //fetch popular
            try {
                URL urlPopular = new URL("http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY);
                HttpURLConnection conn = (HttpURLConnection) urlPopular.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Accept", "application/json");

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                InputStream is = conn.getInputStream();

                // Convert the InputStream into a string
                return getArrayListFromString(readIt(is));

            } catch(UnknownHostException uhe){
                publishProgress("Error fetching movies. Check connection or try again later.");
                uhe.printStackTrace();
                return null;
            }
            catch (IOException e) {
                publishProgress("Error fetching movies. Try again later.");
                e.printStackTrace();
                return null;
            }
        }else{
            //fetch highest rated
            try {
                URL urlPopular = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY);
                HttpURLConnection conn = (HttpURLConnection) urlPopular.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Accept", "application/json");

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                InputStream is = conn.getInputStream();

                // Convert the InputStream into a string
                return getArrayListFromString(readIt(is));
            } catch(UnknownHostException uhe){
                publishProgress("Error fetching movies. Check connection or try again later.");
                uhe.printStackTrace();
                return null;
            }
            catch (IOException e) {
                publishProgress("Error fetching movies. Try again later.");
                e.printStackTrace();
                return null;
            }
        }

//        return new ArrayList<Movie>();
    }

    @Override
    protected void onProgressUpdate(String... messages){
        Toast.makeText(mContext, messages[0], Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result){
        if(result != null) {
            Log.i(TAG, "fetched #movies:: " + result.size());
            mImageAdapter.get().updateMovies(result);
            //we set the menuItem checked until movie loading is complete
            //other wise the menuItem will be checked but the UI wouldn't change
            mMenuItem.get().setChecked(true);
        }
    }

    private ArrayList<Movie> getArrayListFromString(String response){
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonArray = new JSONObject(response);
            JSONArray moviesJArray = jsonArray.getJSONArray("results");

            for(int i= 0; i < moviesJArray.length(); i++){
                movies.add(new Movie(moviesJArray.getJSONObject(i)));
            }

        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        return movies;

    }

    // Reads an InputStream and converts it to a String.
    //got it from http://stackoverflow.com/questions/6511880/how-to-parse-a-json-input-stream
    public String readIt(InputStream stream) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return responseStrBuilder.toString();
    }
}
