package com.example.wayne.pdor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Knowledge extends AppCompatActivity{

    boolean once = FALSE;
    static  ArrayList fragement_list = null;
    Retrive_info retrive_info;
    PagerAdapter pager_adpt;
    ReceiveServiceMsg service_receiver;
    DB_Function db = null;
    Handler mhandler_list = null;
    Thread mthread_list = null;

    float Start_x = 0xFFFF;
    float Start_y = 0xFFFF;
    float End_x = 0xFFFF;
    float End_y = 0xFFFF;
    float capture_start_x;
    float capture_start_y;
    float capture_end_x;
    float capture_end_y;

    String ServiceReceiver_TAG = "retrieve_info.service.msg";
    String TAG = "KNOWLEDGE";

    public class Coor{
        public int x;
        public int y;
        public Coor(int _x, int _y){
            x = _x;
            y = _y;
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        if (fragement_list == null)
            fragement_list = new ArrayList();
        //
        ViewPager view_pager = (ViewPager) findViewById(R.id.viewPager);
        pager_adpt = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(pager_adpt);

        //
        Button clear_db_btn = (Button)findViewById(R.id.clear_db_button);
        clear_db_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTABLE();
            }
        });
        Button screen_shot = (Button)findViewById(R.id.screen_button);
        screen_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coor left_up_coor = new Coor(0, 0);
                Coor right_down_coor = new Coor(0, 0);
                ScreenShot("1", left_up_coor, right_down_coor);
            }
        });

                //Broadcast receiver
        service_receiver = new ReceiveServiceMsg();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceReceiver_TAG);
        registerReceiver(service_receiver, intentFilter);

        //Log to database
        db = new DB_Function(Knowledge.this);
        mhandler_list = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.w(TAG, "Hanlder----in");

                boolean update = FALSE;

                //Retrieve data from database
                ArrayList<String> data_list = db.getAllData();
                int size = data_list.size();
                for (int i = 0; i < size; i++)
                {
                    if (fragement_list.contains(data_list.get(i)) == FALSE
                            && (data_list.get(i).indexOf("www") != -1 || data_list.get(i).indexOf("http") != -1))
                    {
                        fragement_list.add(data_list.get(i));
                        update = TRUE;
                    }
                    Log.w(TAG, "DB---->" + data_list.get(i));
                }
                if (update == TRUE)
                    pager_adpt.notifyDataSetChanged(); //refresh the fragment
            }

        };
        mthread_list = new Thread(new Runnable(){
            @Override
            public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    mhandler_list.sendMessage(msg);
            }
        });

        mthread_list.start();
    }


    public class ReceiveServiceMsg extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean update = FALSE;
            String _url = intent.getStringExtra("_url");

            ArrayList<String> data_list = db.getAllData();
            int size = data_list.size();
            for (int i = 0; i < size; i++)
            {
                if (fragement_list.contains(data_list.get(i)) == FALSE
                        && (data_list.get(i).indexOf("www") != -1 || data_list.get(i).indexOf("http") != -1))
                {
                    fragement_list.add(data_list.get(i));
                    update = TRUE;
                }
                Log.w(TAG, "DB---->" + data_list.get(i));
            }
            if (update == TRUE)
                pager_adpt.notifyDataSetChanged(); //refresh the fragment
//            Log.w(TAG, "Broadcast get info: " + _url);
        }
    }


    /*------------------------------------------------
                Fragment Adapter
     ------------------------------------------------*/
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            fragment.get_postion(position);
            return fragment;
        }
        @Override
        public int getCount() {
            return fragement_list.size();
        }
    }

    static public class ScreenSlidePageFragment extends android.support.v4.app.Fragment{

        int position;

        public void get_postion(int pos){
            this.position = pos;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.screen_slide_page, container, false);

            TextView tv = rootView.findViewById(R.id.screen_fragment_tv1);
            WebView web_view = (WebView) rootView.findViewById(R.id.screen_fragment_webview1);
            web_view.getSettings().setJavaScriptEnabled(true);
            web_view.getSettings().setDisplayZoomControls(true);
            web_view.setWebViewClient(new WebViewClient()); //Force links & open website in current view

            tv.setText("Hi" + this.position);
            String _url = fragement_list.get(this.position).toString();
            web_view.loadUrl(_url);

            return rootView;
        }
    }

    /*------------------------------------------------
                    Screen Shot
     ------------------------------------------------*/
    public void ScreenShot(String name, Coor left_up_coor, Coor right_down_coor){
        try{
//            int quality = 100;

            //img path definition
//            String shot_path = Environment.getExternalStorageDirectory().getPath() + "/" + name + ".jpg"; //Retrieve the sdcard status

            //create bitmap shot
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);                        //set to build the map to Cache
            Bitmap map = Bitmap.createBitmap(v1.getDrawingCache());   //By getting the Cache, it could return the bitmap type
            v1.setDrawingCacheEnabled(false);

            //Store the screen shot
//            File img_file = new File(shot_path);
//            FileOutputStream outputStream = new FileOutputStream(img_file);

            //save bitmap
//            map.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//
//            outputStream.flush(); //Flushes this output stream and forces any buffered output bytes to be written out.
//            outputStream.close();

            openSnapshot(map);
        }catch (Throwable e){
            Log.e(TAG,e.toString());
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void openSnapshot(Bitmap src_map){
        AlertDialog.Builder alert_view = new AlertDialog.Builder(this);

        LayoutInflater factory = LayoutInflater.from(this);
        View v = factory.inflate(R.layout.diag_screenshot, null); //Retrieve the view from specfic layout from current context

        alert_view.setView(v);

        final ImageView img = (ImageView)v.findViewById(R.id.screenshot_imgview);
        final Bitmap bitmap = src_map;

        //customize
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = event.getX();
                float y = event.getY();
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//                    Log.w(TAG,"X = " + x);
//                    Log.w(TAG,"Y = " + y);
                    if (Start_x == 0xFFFF)
                    {
                        Start_x = x;
                        Start_y = y;
                    }
                    End_x = x;
                    End_y = y;

                    Bitmap mutableBitmap = Capture_bitmap(bitmap, x, y);
                    img.setImageBitmap(mutableBitmap);
                }
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    capture_start_x = Start_x;
                    capture_start_y = Start_y;
                    capture_end_x = End_x;
                    capture_end_y = End_y;

                    Start_x = 0xFFFF;
                    Start_y = 0xFFFF;
                }
                return true;
            }

        });

        img.setImageBitmap(bitmap);
        alert_view.show();
    }
    public Bitmap Capture_bitmap(Bitmap src_bitmap, float x, float y){
        int r = 100;
        float c_x = x;
        float c_y = y;

        Bitmap mutableBitmap = src_bitmap.copy(Bitmap.Config.ARGB_8888, true); //avoid collision with the origin bitmap

        Canvas mcanvas = new Canvas(mutableBitmap);
        Paint mpaint = new Paint();

        mpaint.setColor(Color.RED);
        mpaint.setStrokeWidth(2);
        mpaint.setStyle(Paint.Style.STROKE);

        mcanvas.drawRect(Start_x, Start_y, c_x, c_y, mpaint);

        mcanvas.save( Canvas.ALL_SAVE_FLAG );
        mcanvas.restore();

        return mutableBitmap;
    }
}

