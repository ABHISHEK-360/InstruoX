package com.appdev.abhishek360.instruo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.abhishek360.instruo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class WorkshopListAdapter extends RecyclerView.Adapter<WorkshopListAdapter.WorkshopViewHolder> {
    //private String[] member_pic_url;
    private ArrayList<String> workshops;
    private ArrayList<StorageReference> storageReferences;
    private Context ctx;


    public WorkshopListAdapter(Context ctx) {
        this.ctx = ctx;
        workshops = new ArrayList<>();
        workshops.add("android");
        workshops.add("cad");
    }

    public void setStorageReferences(ArrayList<StorageReference> storageReferences) {
        this.storageReferences = storageReferences;
    }

    @NonNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        View view = inf.inflate(R.layout.workshop_view_holder, parent, false);
        return new WorkshopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopViewHolder holder, final int position) {

        Glide.with(ctx).using(new FirebaseImageLoader()).load(storageReferences.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.getPosterIV());
    }

    @Override
    public int getItemCount() {
        return workshops.size();
    }

    public static class WorkshopViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterIV;

        public ImageView getPosterIV() {
            return posterIV;
        }

        public WorkshopViewHolder(View itemView) {
            super(itemView);
            posterIV = itemView.findViewById(R.id.workshop_view_holder_poster);
        }
    }
}
