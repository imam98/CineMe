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
import com.imam.cineme.model.Cast;
import com.imam.cineme.util.API;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastGridAdapter extends RecyclerView.Adapter<CastGridAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Cast> dataList;

    public CastGridAdapter(Context context, ArrayList<Cast> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_person_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast currCast = dataList.get(position);

        holder.lblName.setText(currCast.getName());
        holder.lblCharacter.setText(currCast.getCharacter());

        String url = API.IMAGE_BASE_URL + API.PROFILE_SIZE + currCast.getProfilePath();
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.img_person_placeholder)
                        .fitCenter())
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_profile) ImageView imgProfile;
        @BindView(R.id.lbl_name) TextView lblName;
        @BindView(R.id.lbl_desc) TextView lblCharacter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
