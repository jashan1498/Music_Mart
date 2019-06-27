package com.company.jashan.music_mart;

import android.Manifest;
import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wanderingcan.persistentsearch.PersistentSearchView;
import com.wanderingcan.persistentsearch.SearchMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements home_fragment.PassMedia {

    FragmentTransaction transaction;
    SearchMenu searchmenu;
    BottomSheetBehavior bottomSheetBehavior;

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.expanded_view)
    LinearLayout expandedview;
    @BindView(R.id.collapsed_view)
    LinearLayout collapsedview;
    @BindView(R.id.persistent_search)
    PersistentSearchView searchView;
    @BindView(R.id.bottom_sheet)
    LinearLayout linearLayout;
    @BindView(R.id.null_view)
    View null_view;
    @BindView(R.id.seekbar)
    AppCompatSeekBar seekBar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.song_length)
    TextView duration;
    @BindView(R.id.pause_play)
    Button pause;
    @BindView(R.id.pause_play1)
    Button pause1;
    @BindView(R.id.prev)
    Button prev;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.loop)
    Button loop;
    @BindView(R.id.random)
    Button randomise;
    @BindView(R.id.song_name)
    TextView song_name;
    @BindView(R.id.song_name1)
    TextView song_name1;
    @BindView(R.id.song_info)
    TextView song_info;
    @BindView(R.id.bottom_sheet_recyc)
    RecyclerView recycler_bottom;
    MediaPlayer mp;
    Thread thread = null;
    int currentpos;
    int prevpos;
    ArrayList song_list;
    ArrayList<String> song_data;
    int song_pos = 1;
    int isloop_on = 0;
    int israndom = 0;
    ArrayList shuffled_list;

    @OnClick(R.id.random)
    public void OnRandomClick() {

        if (israndom == 0) {
            randomise.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_dark));
            israndom = 1;
        } else {
            israndom = 0;
            randomise.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        }

    }

    @OnClick(R.id.loop)
    public void onLoopClick() {
        if (isloop_on == 0) {
            isloop_on = 1;
            loop.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_dark));
        } else {
            isloop_on = 0;
            loop.setBackgroundTintList(getColorStateList(R.color.colorPrimary));

        }

    }

    @OnClick(R.id.next)
    public void onClickNext() {
        int new_pos;
        mp.pause();
        if (israndom == 1) {

        } else {

        }
        try {
            if (isloop_on == 1) {
                new_pos = song_pos;
            } else {

                if (song_pos >= song_list.size()) {
                    new_pos = song_pos;
                } else {
                    new_pos = song_pos + 1;
                    song_pos = new_pos;
                }

            }
            mp.reset();
            mp.setDataSource(song_data.get(new_pos));
            mp.prepare();
            mp.start();
            notifyseek(0, 0);
            updatesonginfo(song_list.get(new_pos).toString());
//            Toast.makeText(this, "" + song_list.get(new_pos), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.prev)
    public void OnPrevClick() {
        mp.pause();
        int new_pos;
        try {
            if (isloop_on == 1) {
                new_pos = song_pos;
            } else {

                if (song_pos <= 0) {
                    new_pos = song_pos;
                } else {
                    new_pos = song_pos - 1;
                    song_pos = new_pos;
                }

            }
            mp.reset();
            mp.setDataSource(song_data.get(new_pos));
            mp.prepare();
            mp.start();
            notifyseek(0, 0);
            updatesonginfo(song_list.get(new_pos).toString());
//            Toast.makeText(this, "" + song_list.get(new_pos), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(value = R.id.pause_play)
    public void onClick() {
        if (mp != null) {
            if (mp.isPlaying()) {
                pause.setBackground(getDrawable(R.drawable.play_circle_outline_black_108x108));
                pause1.setBackground(getDrawable(R.drawable.play_circle_outline_black_108x108));
                mp.pause();
            } else {
                pause.setBackground(getDrawable(R.drawable.pause_circle_outline_black_108x108));
                pause1.setBackground(getDrawable(R.drawable.pause_circle_outline_black_108x108));

                mp.start();
            }
        }

    }

    @OnClick(value = R.id.pause_play1)
    public void onClick2() {
        if (mp != null) {
            if (mp.isPlaying()) {
                pause.setBackground(getDrawable(R.drawable.play_circle_outline_black_108x108));
                pause1.setBackground(getDrawable(R.drawable.play_circle_outline_black_108x108));
                mp.pause();
            } else {
                pause.setBackground(getDrawable(R.drawable.pause_circle_outline_black_108x108));
                pause1.setBackground(getDrawable(R.drawable.pause_circle_outline_black_108x108));

                mp.start();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                changefragment("home");

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        collapsedview.setVisibility(View.VISIBLE);
        View view = navigationView.getHeaderView(0);

        Button item = view.findViewById(R.id.close_drawer_btn);


        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {

                // 3 is expanded
                // 4 is collapsed
                if (state == 3) {
                    null_view.setVisibility(View.VISIBLE);
                    collapsedview.setVisibility(View.GONE);
                } else if (state == 4) {
                    collapsedview.setVisibility(View.VISIBLE);
                    null_view.setVisibility(View.GONE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {


            }
        });

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);


        searchmenu = searchView.getSearchMenu();
        searchView.setOnIconClickListener(new PersistentSearchView.OnIconClickListener() {
            @Override
            public void OnNavigationIconClick() {
                drawerLayout.openDrawer(navigationView);
            }

            @Override
            public void OnEndIconClick() {
                Toast.makeText(MainActivity.this, "end icon clicked", Toast.LENGTH_SHORT).show();
            }
        });
        searchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchClosed() {
                searchView.closeSearch();
            }

            @Override
            public void onSearchCleared() {
                Toast.makeText(MainActivity.this, "cleared", Toast.LENGTH_SHORT).show();
                searchmenu.clearItems();

            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
                Toast.makeText(MainActivity.this, "search term changed", Toast.LENGTH_SHORT).show();
                searchmenu.addSearchMenuItem(1, term.toString());

            }

            @Override
            public void onSearch(CharSequence text) {
                Toast.makeText(MainActivity.this, "searched", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.item1) {
                changefragment("home");
            } else if (menuItem.getItemId() == R.id.item2) {
                changefragment("favourites");
            } else if (menuItem.getItemId() == R.id.item3) {
                changefragment("settings");
            }
            drawerLayout.closeDrawers();
            return true;
        });
        bottom_navigation.setOnNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.item4) {
                changefragment("home");
            } else if (menuItem.getItemId() == R.id.item5) {
                changefragment("favourites");
            } else if (menuItem.getItemId() == R.id.item7) {
                changefragment("settings");
            } else if (menuItem.getItemId() == R.id.item6) {
//                    changefragment("Settings");
                Toast.makeText(MainActivity.this, "Under Construction", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                prevpos = seekBar.getProgress();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (mp != null) {
                    currentpos = seekBar.getProgress();
                    notifyseek(currentpos, prevpos);
                } else {
                    Toast.makeText(MainActivity.this, "What Are You Trying To Seek ??? ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void changefragment(String name) {
        transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
        if (fragment == null) {
            switch (name) {
                case "home":
                    fragment = new home_fragment();
                    break;
                case "favourites":
                    fragment = new favourites_fragment();
                    break;
                case "settings":
                    fragment = new Settings_fragment();
                    break;
            }
            if (fragment != null) {
                transaction.add(R.id.fragment, fragment, name);
            }
        }
        hideAllFragment(transaction);
        showFragment(fragment, transaction, name);
        transaction.commit();

    }

    public void showFragment(Fragment fragment, FragmentTransaction transaction, String name) {

        transaction.show(fragment);
    }

    public void hideAllFragment(FragmentTransaction transaction) {
        Fragment home = getSupportFragmentManager().findFragmentByTag("home");
        Fragment favourites = getSupportFragmentManager().findFragmentByTag("favourites");
        Fragment settings = getSupportFragmentManager().findFragmentByTag("settings");
        if (home != null) {
            transaction.hide(home);
        }
        if (favourites != null) {
            transaction.hide(favourites);
        }
        if (settings != null) {
            transaction.hide(settings);
        }
    }

    @Override
    public void audio_Data(MediaPlayer mp, String Name, ArrayList song_list, ArrayList song_data, int song_pos, ArrayList albumart) {
        this.song_list = song_list;
        this.song_pos = song_pos;
        this.shuffled_list = song_list;
        this.song_data = song_data;
        this.mp = mp;
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onClickNext();
            }
        });
        updatesonginfo(Name);
        startnotification();
        ArrayList album_art = new ArrayList();
        Recycler_adapter_bottom_sheet adapter = new Recycler_adapter_bottom_sheet(song_data, this.song_list, mp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        recycler_bottom.setLayoutManager(mLayoutManager);
        recycler_bottom.setItemAnimator(new DefaultItemAnimator());
        recycler_bottom.setAdapter(adapter);

    }

    void startnotification() {

        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.notification);
        RemoteViews big_remoteView = new RemoteViews(getPackageName(), R.layout.notification_expanded);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1234, i, 0);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle("Welcome to Music Mart");
        bigPictureStyle.setSummaryText("vniwnvowwwwwww");


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_song_image);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "123")
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setContentTitle(song_name.getText())
                .setSmallIcon(R.drawable.app_icon, 3)
                .setStyle(bigPictureStyle);
        NotificationChannel channel = new NotificationChannel("123", "High Importance",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
        getSystemService(NotificationManager.class);
        mNotificationManager.notify(1, notification.build());


    }

    void updatesonginfo(String Name) {
        String total = "";

        song_info.setVisibility(View.INVISIBLE);
        if (mp == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } else {

            seekbarControl(mp);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            int min = (mp.getDuration() / 60000);
            int sec = (mp.getDuration() / 1000 - min * 60);

            if (sec < 10) {
                total = min + ":0" + sec;
            } else {
                total = min + ":" + sec;
            }
            String[] songname = Name.split("\\.");
            if (songname.length > 1) {
                song_name.setText(songname[0]);
                song_name1.setText(songname[0]);

                song_info.setText(songname[1]);
                song_info.setVisibility(View.VISIBLE);
            } else {
                song_name.setText(Name);
                song_name1.setText(Name);

            }
            duration.setText(total);


        }
    }


    public void seekbarControl(MediaPlayer mp) {

        if (mp != null) {
            seekBar.setMax(mp.getDuration());
        }

    }

    public void notifyseek(int pos, int prev) {
        int prevpos = prev;
        currentpos = pos;

        Log.d("helloworld", "" + currentpos);
        if (currentpos != prevpos) {
            mp.seekTo(pos);
            Log.d("helloworld", "pos changed" + currentpos);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (mp.isPlaying()) {

                    seekBar.setProgress(mp.getCurrentPosition());
                    currentpos = mp.getCurrentPosition();
                }
            }
        };

        thread = new Thread(runnable);
        thread.start();

    }

}
