package com.imam.cineme.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.imam.cineme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_HEADER = 0;
    private final int TYPE_MOVIE = 1;
    private Context context;

    public MovieGridAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.layout_search_box, parent, false);
            return new SearchBoxVH(view);
        } else {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.layout_movie_grid, parent, false);
            return new MovieVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_MOVIE;
        }
    }

    static class MovieVH extends RecyclerView.ViewHolder {
        @BindView(R.id.img_movie_poster) ImageView imgPoster;
        @BindView(R.id.lbl_movie_views) TextView lblViews;
        @BindView(R.id.lbl_movie_release) TextView lblReleaseDate;

        public MovieVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SearchBoxVH extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_search) EditText txtSearch;
        @BindView(R.id.btn_search) ImageButton btnSearch;

        public SearchBoxVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
