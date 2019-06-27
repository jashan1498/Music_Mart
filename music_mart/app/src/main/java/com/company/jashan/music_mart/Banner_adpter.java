package com.company.jashan.music_mart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;


public class Banner_adpter extends PagerAdapter {

    Context context;
    ArrayList<Integer> image;
    ImageView banner_image;
    ImageView banner_play;

    public Banner_adpter(Context context, ArrayList image) {
        this.context = context;
        this.image = image;

    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (FrameLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = (View) LayoutInflater.from(context).inflate(R.layout.banner_main, container, false);

        banner_image = view.findViewById(R.id.banner_image);
        banner_play = view.findViewById(R.id.banner_play);
        banner_image.setImageResource(image.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }
}
