package com.imam.cineme.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
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
    @BindView(R.id.shimmer_container) ShimmerFrameLayout shimmerContainer;
    private Unbinder unbinder;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String queryStr = txtSearch.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !queryStr.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(MovieListLoader.LIST_CATEGORY, API.QUERY);
                    bundle.putString(MovieListLoader.QUERY_STRING, queryStr);
                    getLoaderManager().restartLoader(0, bundle, ExploreFragment.this);
                }

                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtSearch.getText().toString().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(MovieListLoader.LIST_CATEGORY, API.QUERY);
                    bundle.putString(MovieListLoader.QUERY_STRING, txtSearch.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    getLoaderManager().restartLoader(0, bundle, ExploreFragment.this);
                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString(MovieListLoader.LIST_CATEGORY, API.POPULAR);
        getLoaderManager().initLoader(0, bundle, this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {
        lstMovie.setVisibility(View.GONE);
        shimmerContainer.startShimmer();
        shimmerContainer.setVisibility(View.VISIBLE);
        return new MovieListLoader(getActivity(), bundle);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        MovieGridAdapter adapter = new MovieGridAdapter(getActivity(), movies);
        lstMovie.setLayoutManager(new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false));
        lstMovie.setAdapter(adapter);

        lstMovie.setVisibility(View.VISIBLE);
        shimmerContainer.setVisibility(View.GONE);
        shimmerContainer.stopShimmer();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
    }
}
