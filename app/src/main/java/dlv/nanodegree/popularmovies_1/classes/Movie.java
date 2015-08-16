package dlv.nanodegree.popularmovies_1.classes;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DanielLujanApps on Monday10/08/15.
 */
public class Movie implements Serializable{

    private URL mThumbUrl;
    private URL mPosterUrl;
    private String mTitle;
    private double mRating;
    private String mSynopsis;
    private Date mReleaseDate;

    public Movie(JSONObject json) throws JSONException, MalformedURLException {
        mThumbUrl = new URL("http://image.tmdb.org/t/p/w185/"+json.get("backdrop_path"));
        mPosterUrl = new URL("http://image.tmdb.org/t/p/w185/"+json.get("poster_path"));
        mTitle = json.getString("original_title");
        mRating = json.getDouble("vote_average");
        mSynopsis = json.getString("overview");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mReleaseDate = simpleDateFormat.parse(
                json.getString("release_date"), new ParsePosition(0));
    }

    public URL getThumbnailUrl(){
        return mThumbUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public double getRating() {
        return mRating;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public URL getmPosterUrl() {
        return mPosterUrl;
    }

    public void setmPosterUrl(URL mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }
}
