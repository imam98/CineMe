package com.imam.cineme.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.imam.cineme.R;
import com.imam.cineme.adapter.MovieGridAdapter;
import com.imam.cineme.model.Movie;
import com.imam.cineme.util.API;
import com.imam.cineme.util.MovieListLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    @BindView(R.id.lst_movie) RecyclerView lstMovie;
    @BindView(R.id.txt_search) EditText txtSearch;
    @BindView(R.id.btn_search) ImageButton btnSearch;
    private Unbinder unbinder;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieListLoader(getActivity(), API.POPULAR);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        MovieGridAdapter adapter = new MovieGridAdapter(getActivity(), movies);
        lstMovie.setLayoutManager(new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false));
        lstMovie.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }
}
