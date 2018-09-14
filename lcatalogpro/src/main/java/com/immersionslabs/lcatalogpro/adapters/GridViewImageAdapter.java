package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalogpro.FullScreenImageViewActivity;
import com.immersionslabs.lcatalogpro.GalleryActivity;
import com.immersionslabs.lcatalogpro.R;

import java.util.ArrayList;

public class GridViewImageAdapter extends RecyclerView.Adapter<GridViewImageAdapter.ViewHolder> {

    public static final String TAG = GridViewImageAdapter.class.getSimpleName();

    private Activity _activity;
    private ArrayList<String> _filePaths = new ArrayList<>();

    public GridViewImageAdapter(GalleryActivity activity,
                                ArrayList<String> imagePaths) {
        this._activity = activity;
        this._filePaths = imagePaths;
    }

    @NonNull
    @Override
    public GridViewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(_activity)
                .load(_filePaths.get(position))
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        Log.e(TAG, " images" + holder.imageView);

        holder.gallery_container.setOnClickListener(v -> {
            Intent intent = new Intent(_activity, FullScreenImageViewActivity.class);
            intent.putExtra("position", position);
            _activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return _filePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        RelativeLayout gallery_container;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_item);
            gallery_container = itemView.findViewById(R.id.gallery_container);
        }
    }
}