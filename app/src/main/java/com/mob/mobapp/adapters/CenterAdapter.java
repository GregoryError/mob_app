package com.mob.mobapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mob.mobapp.views.ChatActivity;
import com.mob.mobapp.views.MapViewActivity;


import java.util.ArrayList;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {
    private ArrayList<Center> centerArrayList;
    private String uName;
    private String uPhone;

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    // null = nothing, true = show map, false = show chat
    private Boolean mapOrChat;

    public ArrayList<Center> getCenterArrayList() {
        return centerArrayList;
    }

    public void setCenterArrayList(ArrayList<Center> centerArrayList) {
        this.centerArrayList = centerArrayList;
        notifyDataSetChanged();
    }

    private final Context context;

    public CenterAdapter(Context context, Boolean mapOrChat) {
        this.context = context;
        this.mapOrChat = mapOrChat;
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

        if (mapOrChat != null) {
            if (mapOrChat) {
                LinearLayout linearLayoutOpenMap = (LinearLayout) holder.itemView.findViewById(R.id.linearLayoutOpenMap);
                linearLayoutOpenMap.setVisibility(View.VISIBLE);
                linearLayoutOpenMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open map with lon/lat
                        Intent intent = new Intent(context, MapViewActivity.class);
                        intent.putExtra("lon", center.getLon());
                        intent.putExtra("lat", center.getLat());
                        intent.putExtra("pointDescription", center.getAddress() + "\n" + center.getHours());
                        intent.putExtra("userName", uName);
                        intent.putExtra("userPhone", uPhone);
                        intent.putExtra("cId", center.getcId());
                        context.startActivity(intent);
                    }
                });
            } else {
                LinearLayout linearLayoutOpenChat = (LinearLayout) holder.itemView.findViewById(R.id.linearLayoutOpenChat);
                linearLayoutOpenChat.setVisibility(View.VISIBLE);
                linearLayoutOpenChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open chat with cId
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("userName", uName);
                        intent.putExtra("userPhone", uPhone);
                        intent.putExtra("cId", center.getcId());
                        context.startActivity(intent);
                    }
                });
            }
        }
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
