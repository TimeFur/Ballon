package com.example.wayne.pdor;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Wayne on 2018/2/28.
 */

public class Retrive_info extends Service {

    final String TAG = "Retrive_info";
    Callback main_activity;
    Handler handle;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Create Retrive_info Service");

        handle = new Handler();
        handle.post(this.serviceRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Already create Retrive_info Service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destory Retrive_info Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable serviceRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Post");
            handle.postDelayed(this, 1000);
        }
    };
    //Register the callback function
    public void register_cb_function(Activity main_activity){
        this.main_activity = (Callback) main_activity;
    }

    public interface Callback{
        public void updateServiceRetriveMsg(String _url);
    }
}
