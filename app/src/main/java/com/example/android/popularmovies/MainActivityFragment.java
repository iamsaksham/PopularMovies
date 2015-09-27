package com.example.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

   /* public void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();


    }

    @Override
    public void onStart() {
        super.onStart();
        //updateMovies();
    } */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getActivity().getApplicationContext();
        //ImageView imageView = (ImageView) rootView.findViewById(R.id.image1);
        GridView grid = (GridView) rootView.findViewById(R.id.gridview);
        grid.setAdapter(new ImageAdapter(context));
        //Picasso.with(context).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);
        //Toast.makeText(context, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
        return rootView;
    }



}