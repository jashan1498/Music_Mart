package com.company.jashan.music_mart;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class home_fragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<String> song_name;
    ArrayList<String> song_data;
    ViewPager banner;
    ArrayList banner_images;
    Handler handler;
    Runnable runnable;
    MediaPlayer mp;
    ArrayList<Bitmap> album_art;
    PassMedia passMedia;

    public interface PassMedia{
         void audio_Data(MediaPlayer mp,String name,ArrayList song_name,ArrayList song_data,int song_pos,ArrayList albumart);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        banner = view.findViewById(R.id.banner);
        passMedia =(PassMedia) getActivity();


        song_name = new ArrayList();
        banner_images = new ArrayList();
        song_data = new ArrayList();
        album_art = new ArrayList<>();

        banner_images.add(R.drawable.banner_1);
        banner_images.add(R.drawable.banner_2);
        banner_images.add(R.drawable.banner_3);


        Banner_adpter banner_adpter = new Banner_adpter(getContext(), banner_images);
        banner.setAdapter(banner_adpter);
        banner_auto_move();
        getsongsdata();

        Recycler_view_adapter adapter = new Recycler_view_adapter(song_name, song_data,album_art);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.OnItemClickListener(new Recycler_view_adapter.setonClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getContext(), "song data : " + song_data.get(position), Toast.LENGTH_SHORT).show();
                try {
                    if(mp == null) {
                        mp = new MediaPlayer();
                    }
                    mp.reset();
                    mp.setDataSource(song_data.get(position));
                    mp.prepare();
                    mp.start();
                    passMedia.audio_Data(mp,song_name.get(position),song_name,song_data,position,album_art);

                    Toast.makeText(getContext(), "playing", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    public void getsongsdata() {

        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media._ID};
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String media_data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    song_name.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                    song_data.add(media_data);

                    MediaMetadataRetriever mediaMetadataRetriever;
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(media_data);
                    byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
                    if (data != null) {
                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);

                         album_art.add(image);

                    }else{
                        album_art.add(BitmapFactory.decodeResource(getResources(),R.drawable.default_song_image));
                    }

                }
                while (cursor.moveToNext()) ;
            } else {
                song_name.add("No Songs Found ... ");
            }
            Toast.makeText(getContext(), "number of songs found " + song_name.size(), Toast.LENGTH_SHORT).show();
        }
    }
    void banner_auto_move() {

         handler = new Handler();
          runnable = new Runnable() {
            @Override
            public void run() {
                if (banner.getCurrentItem() == 0) {
                    banner.setCurrentItem(1);
                } else if (banner.getCurrentItem() == 1) {
                    banner.setCurrentItem(2);
                } else if (banner.getCurrentItem() == 2) {
                    banner.setCurrentItem(0);
                }
                handler.postDelayed(runnable,3000);
            }
        };

        handler.postDelayed(runnable,3000);
    }
}