package com.example.wayne.pdor;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

        play_btn = findViewById(R.id.collectio_play_btn);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying() == false)
                {
                    //Start to play
                    if (initial_status == true) {
                        new Stream_audio().execute(url);
                        initial_status = false;
                    }
                    else
                    {
                        if (mediaPlayer.isPlaying() == false)
                            mediaPlayer.start();
                    }
                }
                else
                {
                    //Stop
                    mediaPlayer.pause();
                }
            }
        });
    }

    /*========================================
                   Stream data class
     ========================================*/
    class Stream_audio extends AsyncTask<String, Integer, Long>
    {
        @Override
        protected Long doInBackground(String... urls) {
            try
            {
                mediaPlayer.setDataSource(urls[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset(); //it should set the resource again & call prepare
                        initial_status = true;
                    }
                });

                mediaPlayer.prepare(); //player for playback (synchronously)

            }catch (Exception e){

            }


            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            mediaPlayer.start();

        }
    }
}

