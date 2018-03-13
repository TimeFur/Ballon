package com.example.wayne.pdor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

/**
 * Created by Wayne on 2018/2/28.
 */

public class Retrive_info extends Service {

    final String TAG = "Retrive_info";
    String ServiceReceiver_TAG = "retrieve_info.service.msg";
    Callback main_activity;
    Handler handle;
    final localBinder mBinder = new localBinder();
    Intent service_broadcast;
    DB_Function db = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Create Retrive_info Service");

        //
        service_broadcast = new Intent(ServiceReceiver_TAG);

        //
        handle = new Handler();
        handle.post(this.serviceRunnable);

        final ClipboardManager cm = (ClipboardManager)getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                ClipData data = cm.getPrimaryClip();
                ClipData.Item item = data.getItemAt(0);
                String v = item.getText().toString();
                main_activity.updateServiceRetriveMsg(v);

                //notification
                service_broadcast.putExtra("_url", v);
                sendBroadcast(service_broadcast);

                //Sqlite
                ArrayList<String> data_list = db.getAllData();
                if (data_list.contains(v) == FALSE)
                {
                    if(v.indexOf("www") != -1 || v.indexOf("http") != -1)
                    {
                        db.InsertData(v);
                    }
                }

                Log.w(TAG,"Send Clip data" + v);
            }
        });

        //
        db = new DB_Function(Retrive_info.this);
//        String v = db.GetData(1);
//        Log.w(TAG,"GET-----------------------"+v);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "Already create Retrive_info Service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Destory Retrive_info Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class localBinder extends Binder {
        Retrive_info getBinder(){
            return Retrive_info.this;
        }
    }

    //
    Runnable serviceRunnable = new Runnable() {

        @Override
        public void run() {
            Log.i(TAG, "Post");
            handle.postDelayed(this, 1000);
        }
    };

    //Register the callback function
    public void register_cb_function(Activity main_activity){
        this.main_activity = (Callback) main_activity;
    }

    public interface Callback{
        public void updateServiceRetriveMsg(String v);
    }
}
