package com.mob.mobapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.mob.mobapp.R;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.Center;


import java.util.ArrayList;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {
    private ArrayList<Center> centerArrayList;

    public ArrayList<Center> getCenterArrayList() {
        return centerArrayList;
    }

    public void setCenterArrayList(ArrayList<Center> centerArrayList) {
        this.centerArrayList = centerArrayList;
        notifyDataSetChanged();
    }

    private final Context context;

    public CenterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return centerArrayList.size();
    }

    @NonNull
    @Override
    public CenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.center_item, parent, false);
        return new CenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterViewHolder holder, int position) {
        Center center = centerArrayList.get(position);

        // set center image

        ImageView centerImage = (ImageView) holder.itemView.findViewById(R.id.imageViewCenter);

        GlideUrl glideUrl = new GlideUrl(ApiFactory.BASE_URL + "getImage/" + center.getImg(),
                new LazyHeaders.Builder()
                        .build());

        Glide.with(context)
                .asBitmap()
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(centerImage);

        // set center text desc

        TextView centerText = (TextView) holder.itemView.findViewById(R.id.textViewCenter);
        centerText.setText(String.format("%s\n%s\n\n%s", center.getAddress(), center.getHours(), center.getDescription()));
    }

    public static class CenterViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCenter);
            textView = itemView.findViewById(R.id.textViewCenter);
        }
    }
}
