package com.example.wayne.pdor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements Retrive_info.Callback{

    final String TAG = "Main Activity";
    Retrive_info Retrive_service;
    Floating_window Floating_service;
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
//                intent.setClass(MainActivity.this, Ballon.class);
//                startActivity(intent);

//                intent.setClass(MainActivity.this, Floating_window.class);
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 1);

                Intent startservice = new Intent(MainActivity.this, Floating_window.class);
                startService(startservice);
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
    public ServiceConnection fService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Floating_window.localBinder binder = (Floating_window.localBinder) service;
            Floating_service = binder.getBinder();
//            Floating_service.create_floating();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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

    void create_floating(){
        LayoutInflater inflater = LayoutInflater.from(this);
        WindowManager win_manage = null;
        WindowManager.LayoutParams win_param = null;

        win_manage = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        View win_view = inflater.inflate(R.layout.floating_layout, null, false);
        ImageView img = (ImageView) win_view.findViewById(R.id.floating_view);
        img.setImageResource(R.drawable.ic_ballon);

        //Setting window parameter
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.width = 100;
        wmParams.height = 100;

        //Bind Setting
        win_manage.addView(win_view, wmParams);
    }
}
