package com.example.wayne.pdor;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.URL;

public class Collection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button play_btn;
    private Button stop_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        //setting the media
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play_btn = findViewById(R.id.collectio_play_btn);
        stop_btn = findViewById(R.id.collectio_stop_btn);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    /*========================================
                   Stream data class
     ========================================*/
    class Stream_audio extends AsyncTask<URL, Integer, Long>
    {
        @Override
        protected Long doInBackground(URL... urls) {



            return null;
        }
    }
}

