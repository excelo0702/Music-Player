package com.example.tanishyadav.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button pause,next,previous;
    TextView SongName;
    SeekBar seekBar;

    static MediaPlayer mymediaPlayer;
    int position;

    String sName;

    ArrayList<File> Songs;
    Thread updateSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pause = (Button)findViewById(R.id.pause);
        next = (Button)findViewById(R.id.next);
        SongName = (TextView)findViewById(R.id.SongName);
        previous = (Button)findViewById(R.id.previous);
        seekBar = (SeekBar)findViewById(R.id.Seekbar);
        Log.v("MainActivity.this","11100000");


        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int Tduration = mymediaPlayer.getDuration();
                int Cduration = 0;
                while(Cduration < Tduration)
                {
                    Log.v("MainActivity.this","111");
                    try{
                        sleep(200);
                        Cduration = mymediaPlayer.getCurrentPosition();
                        seekBar.setProgress(Cduration);

                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        };

        if(mymediaPlayer!=null)
        {
            mymediaPlayer.stop();
            mymediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        Songs = (ArrayList)bundle.getParcelableArrayList("songs");
        sName = Songs.get(position).getName().toString();
        position = bundle.getInt("pos",0);

        String songName = i.getStringExtra("songName");
        SongName.setText(sName);
        SongName.setSelected(true);

        Uri u =Uri.parse(Songs.get(position).toString());

        mymediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        mymediaPlayer.start();
        seekBar.setMax(mymediaPlayer.getDuration());
        Log.v("MainActivity.this","11100");

        updateSeekBar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mymediaPlayer.seekTo(seekBar.getProgress());
                Log.v("MainActivity.this","111000");
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(mymediaPlayer.getDuration());
                if(mymediaPlayer.isPlaying())
                {
                    pause.setBackgroundResource(R.drawable.icon_play);
                    mymediaPlayer.pause();
                }
                else
                {
                    pause.setBackgroundResource(R.drawable.icon_pause);
                    mymediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaPlayer.pause();
                mymediaPlayer.release();
                position = (position+1)%Songs.size();

                Uri u = Uri.parse(Songs.get(position).toString());
                mymediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sName = Songs.get(position).getName().toString();
                SongName.setText(sName);
                mymediaPlayer.start();

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaPlayer.pause();
                mymediaPlayer.release();
                position = ((position-1)<0)?(Songs.size()-1):(position-1);

                Uri u = Uri.parse(Songs.get(position).toString());
                mymediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sName = Songs.get(position).getName().toString();
                SongName.setText(sName);
                mymediaPlayer.start();

            }
        });

    }
}
