package dlv.nanodegree.popularmovies_1.classes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Movie Object
 * Created by DanielLujanApps on Monday10/08/15.
 */
public class Movie implements Parcelable{

    public static String MOVIE_KEY = "movie";

    private URL mThumbUrl;
    private URL mPosterUrl;
    private String mTitle;
    private double mRating;
    private String mSynopsis;
    private String mStringReleaseDate;
    public static String dateFormat = "dd MMM yyyy";
    private String dateFormatServer = "yyyy-MM-dd";
    private byte[] thumbnailByteArray;
    private byte[] posterByteArray;
    private boolean loadedYet = false;

    public Movie(JSONObject json) throws JSONException, MalformedURLException {
        mThumbUrl = new URL("http://image.tmdb.org/t/p/w185/"+json.get("backdrop_path"));
        mPosterUrl = new URL("http://image.tmdb.org/t/p/w185/"+json.get("poster_path"));
        mTitle = json.getString("original_title");
        mRating = json.getDouble("vote_average");
        mSynopsis = json.getString("overview");
        DateFormat dateFormat = DateFormat.getInstance();
        mStringReleaseDate = json.getString("release_date");
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

    public String getReleaseDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatServer);
        Date releaseDate = simpleDateFormat.parse(mStringReleaseDate, new ParsePosition(0));
        simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(releaseDate);
    }

    public URL getmPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(URL mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    /******** THUMBNAIL THINGIES ************/
    public boolean hasThumbnailByteArray(){
        return thumbnailByteArray != null;
    }

    public byte[] getThumbnailByteArray(){
        return thumbnailByteArray;
    }

    public void setThumbnailByteArray(byte[] array) {
        thumbnailByteArray = array;
    }
    /*********** POSTER THINGIES***********************/
    public boolean hasPosterByteArray(){
        return posterByteArray != null;
    }

    public byte[] getPosterByteArray(){
        return posterByteArray;
    }

    public void setPosterByteArray(byte[] array) {
        posterByteArray = array;
    }

    public void setLoadedTrue(){
        loadedYet = true;
    }
    /**
     * Parcelable thingies
     **/
    public int describeContents() {
        return 0;
    }

    /**
     *
     * parcelable object
     * string[]{
     *     mThumbnUrl,
     *     mPosterUrl,
     *     mTitle,
     *     mSynopsis,
     *     mDate
     * }
     * ,
     * double mRating
     */
    public void writeToParcel(Parcel out, int flags) {
        String[] stringArray = new String[]{
                mThumbUrl.toString(),
                mPosterUrl.toString(),
                mTitle,
                mSynopsis,
                mStringReleaseDate
        };
        out.writeInt(loadedYet?1:0);
        if(loadedYet) {
            out.writeInt(posterByteArray.length);
            out.writeByteArray(posterByteArray);
            out.writeInt(thumbnailByteArray.length);
            out.writeByteArray(thumbnailByteArray);
        }
        out.writeStringArray(stringArray);
        out.writeDouble(mRating);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        String[] stringArray = new String[5];
        loadedYet = in.readInt() == 1;
        if(loadedYet) {
            int posterLength = in.readInt();
            posterByteArray = new byte[posterLength];
            in.readByteArray(posterByteArray);
            int thumbLength = in.readInt();
            thumbnailByteArray = new byte[thumbLength];
            in.readByteArray(thumbnailByteArray);
        }
        in.readStringArray(stringArray);
        mRating = in.readDouble();

        try {
            mThumbUrl = new URL(stringArray[0]);
            mPosterUrl = new URL(stringArray[1]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mTitle = stringArray[2];
        mSynopsis = stringArray[3];
        mStringReleaseDate = stringArray[4];
    }
}
