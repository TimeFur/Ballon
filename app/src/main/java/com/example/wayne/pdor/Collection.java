package com.example.wayne.pdor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RecoverySystem;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.URL;

public class Collection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button play_btn;
    private boolean play_status = false;
    private boolean initial_status = true;
    private String url = "https://www.ssaurel.com/tmp/mymusic.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        //setting the media
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        final Stream_audio stream = new Stream_audio();

        play_btn = findViewById(R.id.collectio_play_btn);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stream.stream_player.isPlaying() == false)
                {
                    //Start to play
                    if (initial_status == true) {
                        stream.execute(url);
                        initial_status = false;
                    }
                    else
                    {
                        if (stream.stream_player.isPlaying() == false)
                            stream.stream_player.start();
                    }
                }
                else
                {
                    //Stop
                    stream.stream_player.pause();
                }
            }
        });
    }

    /*========================================
                   Stream data class
     ========================================*/
    public static class Stream_audio extends AsyncTask<String, Integer, Long>
    {
        public MediaPlayer stream_player;

        //Constructor
        public Stream_audio() {
            stream_player = new MediaPlayer();
            stream_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        @Override
        protected Long doInBackground(String... urls) {
            try
            {
                stream_player.setDataSource(urls[0]);
                stream_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stream_player.stop();
                        stream_player.reset(); //it should set the resource again & call prepare
                    }
                });

                stream_player.prepare(); //player for playback (synchronously)
            }catch (Exception e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            stream_player.start();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

