package com.immersionslabs.lcatalogpro.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.immersionslabs.lcatalogpro.NotifyActivity;
import com.immersionslabs.lcatalogpro.R;
import com.immersionslabs.lcatalogpro.utils.EnvConstants;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = NotificationAdapter.class.getSimpleName();

    private Activity activity;

    private ArrayList<String> notification_ids;
    private ArrayList<String> notification_messages;
    private ArrayList<String> notification_images;
    private ArrayList<String> notification_titles;

    public NotificationAdapter(NotifyActivity activity,
                               ArrayList<String> notification_ids,
                               ArrayList<String> notification_messages,
                               ArrayList<String> notification_images,
                               ArrayList<String> notification_titles) {

        this.notification_ids = notification_ids;
        this.notification_messages = notification_messages;
        this.notification_images = notification_images;
        this.notification_titles = notification_titles;

        Log.e(TAG, "ids" + notification_ids);
        Log.e(TAG, "messages" + notification_messages);
        Log.e(TAG, "images" + notification_images);
        Log.e(TAG, "titles" + notification_titles);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_notification, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        String get_image = notification_images.get(position);


        RequestOptions glideoptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.drawable.dummy_icon);
        Glide.with(activity)
                .load(EnvConstants.APP_BASE_URL + "/upload/notifications/" + get_image)
                .apply(glideoptions)
                .into(viewHolder.imageView);

        viewHolder.title.setText(notification_titles.get(position));
        viewHolder.message.setText(notification_messages.get(position));
    }

    @Override
    public int getItemCount() {
        return notification_ids.size();
    }

    /**
     * View holder to display each RecylerView item
     */

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView imageView;
        private TextView title, message;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notify_image);
            title = itemView.findViewById(R.id.notification_title);
            message = itemView.findViewById(R.id.notification_data);
        }
    }
}