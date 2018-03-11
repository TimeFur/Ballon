package com.example.wayne.pdor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Retrive_info.Callback{

    final String TAG = "Main Activity";
    Retrive_info Retrive_service;
    public DB_Function db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent();
        Log.d(TAG,"Main  Start");

        //Toolbar (the upper view of activity_main)-----------------------------------Upper
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG,"Upper  Start");

        //FloatingActionButton (the bottom view of activity_main)---------------------Bottom
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d(TAG,"Bottom  Start");

        //Fragment view---------------------------------------------------------------Middle
        Button read_btn = (Button) findViewById(R.id.Read_btn);
        Button ballon_btn = (Button) findViewById(R.id.Ballon_btn);
        Button gift_btn = (Button) findViewById(R.id.Gift_btn);

        read_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, Knowledge.class);
                startActivity(intent);
            }
        });
        ballon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, Ballon.class);
                startActivity(intent);
            }
        });
        gift_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, Collection.class);
                startActivity(intent);
            }
        });
        Log.d(TAG,"Middle  Start");

        //Service Setting-------------------------------------------------------------Background thread
        Intent startservice = new Intent(this, Retrive_info.class);
        //startService(startservice);
        bindService(startservice, mService, Context.BIND_AUTO_CREATE);
        Log.d(TAG,"Service  Start");

        //Log to database
        db = new DB_Function(MainActivity.this);

        Log.d(TAG,"DB  Start");

    }
    public DB_Function getDB(){
            return db;
    }
    public void test(){
        Log.w(TAG, "DataBase Test");
    }
    public ServiceConnection mService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Retrive_info.localBinder binder = (Retrive_info.localBinder)service;
            Retrive_service = binder.getBinder();
            Retrive_service.register_cb_function(MainActivity.this); // callback function register
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Retrive_service = null;
        }
    };

    //Callback function implement for Service
    public void updateServiceRetriveMsg(String _url){
//        Log.w(TAG, _url);
//        Log.w(TAG, "Get Service callback");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
