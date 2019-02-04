package com.imam.cineme.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.imam.cineme.R;
import com.imam.cineme.model.Movie;
import com.imam.cineme.util.API;
import com.imam.cineme.util.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private final int TYPE_HEADER = 0;
    private final int TYPE_MOVIE = 1;
    private Context context;
    private ArrayList<Movie> dataList;
    private RecyclerViewOnItemClickListener itemClickListener = null;

    public MovieGridAdapter(Context context, ArrayList<Movie> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.layout_movie_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie currMovie = dataList.get(position);
        holder.lblViews.setText(String.valueOf(currMovie.getRate()));
        holder.lblReleaseDate.setText(currMovie.getReleaseDate());

        String url = API.IMAGE_BASE_URL + API.POSTER_SIZE + currMovie.getPosterPath();
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.img_poster_placeholder).fitCenter())
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return (dataList == null)? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_MOVIE;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_movie_poster) ImageView imgPoster;
        @BindView(R.id.lbl_movie_views) TextView lblViews;
        @BindView(R.id.lbl_movie_release) TextView lblReleaseDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClicked(view, position);
            }
        }
    }
}
