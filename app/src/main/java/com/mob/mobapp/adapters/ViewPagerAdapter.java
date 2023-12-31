package com.mob.mobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.mob.mobapp.R;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.Promo;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    // Context object
    Context context;
    // Array of images
    ArrayList<Promo> promos;
    // Layout Inflater
    LayoutInflater mLayoutInflater;

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, ArrayList<Promo> promos) {
        this.context = context;
        this.promos = promos;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return promos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

//        // set promo name and desc
        TextView textViewPromoName = (TextView) itemView.findViewById(R.id.textViewPromoName);
        TextView textViewPromoDesc = (TextView) itemView.findViewById(R.id.textViewPromoDesc);
        textViewPromoName.setText(promos.get(position).getName());
        textViewPromoDesc.setText(promos.get(position).getDescription());

        //setting the image in the imageView

        GlideUrl glideUrl = new GlideUrl(ApiFactory.BASE_URL + "getImage/" + promos.get(position).getImgName(),
                new LazyHeaders.Builder()
                        .build());

        Glide.with(context)
                .asBitmap()
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);


        // Adding the View
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
