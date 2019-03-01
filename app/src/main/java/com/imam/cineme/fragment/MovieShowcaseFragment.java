package com.imam.cineme.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.imam.cineme.R;
import com.imam.cineme.activity.MovieDetailActivity;
import com.imam.cineme.adapter.MovieGridAdapter;
import com.imam.cineme.model.Movie;
import com.imam.cineme.util.API;
import com.imam.cineme.util.MovieListLoader;
import com.imam.cineme.util.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieShowcaseFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    @BindView(R.id.lbl_quote) TextView lblQuote;
    @BindView(R.id.lst_movie) RecyclerView lstMovie;
    @BindView(R.id.shimmer_container) ShimmerFrameLayout shimmerContainer;
    private String showcaseState;
    private Unbinder unbinder;
    private ArrayList<Movie> dataList;

    public MovieShowcaseFragment() {
        // Required empty public constructor
    }

    public Fragment setShowcaseState(String state) {
        showcaseState = state;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_movie_showcase, container, false);
        unbinder = ButterKnife.bind(this, view);

        String quotes[];
        if (API.NOW_PLAYING.equals(showcaseState)) {
            quotes = getActivity().getResources().getStringArray(R.array.watch_now_quotes);
        } else {
            quotes = getActivity().getResources().getStringArray(R.array.upcoming_quotes);
        }

        int elmIndex = new Random().nextInt(quotes.length);
        lblQuote.setText(quotes[elmIndex]);


        Bundle args = new Bundle();
        args.putString(MovieListLoader.LIST_CATEGORY, showcaseState);
        getLoaderManager().initLoader(0, args, this);
        return view;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {
        lstMovie.setVisibility(View.GONE);
        shimmerContainer.setVisibility(View.VISIBLE);
        shimmerContainer.startShimmer();
        return new MovieListLoader(getActivity(), bundle);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        dataList = movies;

        MovieGridAdapter adapter = new MovieGridAdapter(getActivity(), movies);
        adapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Movie movie = dataList.get(position);
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
                startActivity(movieDetailIntent);
            }
        });
        lstMovie.setAdapter(adapter);
        lstMovie.setLayoutManager(new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false));

        shimmerContainer.setVisibility(View.GONE);
        shimmerContainer.stopShimmer();
        lstMovie.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
