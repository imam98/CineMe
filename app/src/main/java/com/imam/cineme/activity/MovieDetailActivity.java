package com.imam.cineme.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.imam.cineme.R;
import com.imam.cineme.adapter.CastGridAdapter;
import com.imam.cineme.adapter.MovieGridAdapter;
import com.imam.cineme.model.Movie;
import com.imam.cineme.util.API;
import com.imam.cineme.util.HeaderView;
import com.imam.cineme.util.MovieDetailLoader;
import com.imam.cineme.util.RecyclerViewOnItemClickListener;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, LoaderManager.LoaderCallbacks<Movie> {
    @BindView(R.id.appBar) AppBarLayout appbar;
    @BindView(R.id.toolbar_header_view) HeaderView toolbarHeaderView;
    @BindView(R.id.float_header_view) HeaderView floatHeaderView;
    @BindView(R.id.toolbarImage) ImageView imgBackdrop;
    @BindView(R.id.lbl_director) TextView lblDirector;
    @BindView(R.id.lbl_genre) TextView lblGenre;
    @BindView(R.id.lbl_popularity) TextView lblPopularity;
    @BindView(R.id.lbl_rate) TextView lblRate;
    @BindView(R.id.lbl_runtime) TextView lblRuntime;
    @BindView(R.id.lbl_overview) TextView lblOverview;
    @BindView(R.id.lst_casts) RecyclerView lstCasts;
    @BindView(R.id.lst_recommendations) RecyclerView lstRecommendations;
    @BindView(R.id.container_movie_detail) ConstraintLayout movieDetailContainer;
    @BindView(R.id.shimmer_container) ShimmerFrameLayout shimmerContainer;
    public static final String EXTRA_MOVIE = "extra_movie";
    private boolean isHideToolbarView = false;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        toolbarHeaderView.bindTo(movie.getTitle(), movie.getReleaseYear());
        floatHeaderView.bindTo(movie.getTitle(), movie.getReleaseYear());

        appbar.addOnOffsetChangedListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public Loader<Movie> onCreateLoader(int i, Bundle bundle) {
        movieDetailContainer.setVisibility(View.GONE);
        shimmerContainer.setVisibility(View.VISIBLE);
        return new MovieDetailLoader(this, movie);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, final Movie movie) {
        this.movie = movie;

        String url = API.IMAGE_BASE_URL + API.BACKDROP_SIZE + movie.getBackdropPath();
        Glide.with(this)
                .asBitmap()
                .load(url)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(imgBackdrop);

        lblDirector.setText(movie.getDirector());

        StringBuilder genreStr = new StringBuilder();
        for (Iterator<String> it = movie.getGenres().iterator(); it.hasNext(); ) {
            genreStr.append(it.next());
            if (it.hasNext()) {
                genreStr.append(", ");
            }
        }
        lblGenre.setText(genreStr.toString());

        lblPopularity.setText(String.valueOf(movie.getPopularity()));
        lblRate.setText(String.valueOf(movie.getRate()));
        lblRuntime.setText(String.valueOf(movie.getRuntime()));
        lblOverview.setText(movie.getOverview());

        lstCasts.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        CastGridAdapter castAdapter = new CastGridAdapter(this, movie.getCasts());
        lstCasts.setAdapter(castAdapter);

        lstRecommendations.setLayoutManager(new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false));
        MovieGridAdapter movieAdapter = new MovieGridAdapter(this, movie.getRecommendations());
        movieAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Movie clickedMovie = movie.getRecommendations().get(position);
                Intent movieDetailActivity = new Intent(MovieDetailActivity.this,
                        MovieDetailActivity.class);
                movieDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, clickedMovie);
                startActivity(movieDetailActivity);
            }
        });
        lstRecommendations.setAdapter(movieAdapter);

        shimmerContainer.setVisibility(View.GONE);
        shimmerContainer.stopShimmer();
        movieDetailContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}
