package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    GridView grid;

    public void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        String movieDBKey = "4b229aeb8fe03e58e2c8bc23bf408a2c";
        movieTask.execute(movieDBKey);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getActivity().getApplicationContext();
        //ImageView imageView = (ImageView) rootView.findViewById(R.id.image1);
        grid = (GridView) rootView.findViewById(R.id.gridview);
        //Picasso.with(context).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);
        //Toast.makeText(context, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        //this method will fetch JSON from the url given
        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //will contain raw JSON response from the url as a string
            String movieJsonStr = null;

            String popularity = "popularity.desc";


            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=4b229aeb8fe03e58e2c8bc23bf408a2c");

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, popularity)
                        .appendQueryParameter(API_KEY_PARAM, params[0])
                        .build();

                URL url = new URL(builtUri.toString());
               // Log.v(LOG_TAG, "Built URI " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

               // Log.v(LOG_TAG, "Forecast JSON String: " + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getPopularMoviesFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        //this method parses JSON
        private String[] getPopularMoviesFromJson(String movieJsonStr)
            throws JSONException {

            final String OWM_RESULTS = "results";
            final String OWM_POSTER_PATH = "poster_path";

            JSONObject movieJsonObject = new JSONObject(movieJsonStr);
            JSONArray movieResultsArray = movieJsonObject.getJSONArray(OWM_RESULTS);

            ArrayList<String> list = new ArrayList<String>();

            for (int i = 0; i < movieResultsArray.length(); i++) {

                JSONObject movieDetails = movieResultsArray.getJSONObject(i);
                String posterPath = movieDetails.getString(OWM_POSTER_PATH);

                list.add(posterPath);
            }

            Object[] posterList = list.toArray();
            String[] posterPaths = Arrays.copyOf(posterList, posterList.length, String[].class);

            for (String s : posterPaths) {
                Log.v(LOG_TAG, "Forecast entry: " + s);
            }

            return posterPaths;
        }

        @Override
        protected void onPostExecute(String[] posterPaths) {
            if (posterPaths != null) {
                grid.setAdapter(new ImageAdapter(getActivity(), posterPaths));
            }
        }

    }

}