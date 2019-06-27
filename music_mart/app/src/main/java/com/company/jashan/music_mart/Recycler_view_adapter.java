package com.company.jashan.music_mart;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Recycler_view_adapter extends RecyclerView.Adapter<Recycler_view_adapter.Recycler_view_holder> {

    ArrayList song_name_list;
    ArrayList<String> song_data;
    Context context;
    int position;
    ArrayList<Bitmap> album_art;

    setonClickListener clickListener;

    public interface setonClickListener {
        void onItemClick(int position);
    }

    public void OnItemClickListener(setonClickListener listener) {
        clickListener = listener;
    }

    public Recycler_view_adapter(ArrayList name, ArrayList song_data, ArrayList album_art) {

        song_name_list = name;
        this.song_data = song_data;
        this.album_art = album_art;

    }

    @NonNull
    @Override
    public Recycler_view_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_songs, viewGroup, false);
        context = viewGroup.getContext();
        return new Recycler_view_holder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_view_holder recycler_view_holder, int i) {

        recycler_view_holder.song_name.setText(song_name_list.get(i).toString());
//        if (album_art.size() > song_image_list.size()) {
//            for (int x = 0; x < album_art.size() - song_image_list.size(); x++) {
//                album_art.add(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.default_song_image));
//            }
//        }

        recycler_view_holder.song_image.setImageBitmap(album_art.get(i));
        position = i;

    }

    @Override
    public int getItemCount() {
        return song_name_list.size();
    }

    public class Recycler_view_holder extends RecyclerView.ViewHolder {

        TextView song_name;
        ImageView song_image;


        public Recycler_view_holder(@NonNull View itemView, final setonClickListener listener) {
            super(itemView);
            song_name = itemView.findViewById(R.id.song_name);
            song_image = itemView.findViewById(R.id.song_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

        }
    }
}
