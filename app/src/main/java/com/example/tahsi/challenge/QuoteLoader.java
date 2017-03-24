package com.example.tahsi.challenge;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by tahsi on 3/5/2017.
 */

public class QuoteLoader extends AsyncTaskLoader<List<String>> {

    /** Tag for log messages */
    private static final String LOG_TAG = QuoteLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link QuoteLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public QuoteLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<String> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<String> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
