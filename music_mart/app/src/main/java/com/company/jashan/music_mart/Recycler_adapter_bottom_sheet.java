package com.company.jashan.music_mart;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Recycler_adapter_bottom_sheet extends RecyclerView.Adapter<Recycler_adapter_bottom_sheet.Recycler_View_holder> {
    ArrayList<String> song_list;
    ArrayList song_data;
    MediaPlayer mp;
    ArrayList<Bitmap> album_art;

    public Recycler_adapter_bottom_sheet(ArrayList song_data,ArrayList song_list,MediaPlayer mp) {
        this.song_data = song_data;
        this.song_list = song_list;
        this.mp = mp;

    }

    @NonNull
    @Override
    public Recycler_View_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_songs_bottom_sheet, viewGroup, false);
        return new Recycler_View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_View_holder recycler_view_holder, int i) {

        recycler_view_holder.song_name.setText(song_list.get(i));
//        recycler_view_holder.album_art.setImageBitmap(album_art.get(i));

    }

    @Override
    public int getItemCount() {
        return song_list.size();
    }

    public class Recycler_View_holder extends RecyclerView.ViewHolder{
        TextView song_name;
        ImageView album_art;


        public Recycler_View_holder(@NonNull View view) {
            super(view);
            song_name = view.findViewById(R.id.song_name_bottom_sheet);
            album_art = view.findViewById(R.id.album_art);

        }
    }
}
