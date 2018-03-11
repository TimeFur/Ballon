package com.example.wayne.pdor;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Knowledge extends AppCompatActivity{

    static  ArrayList fragement_list;
    Retrive_info retrive_info;
    PagerAdapter pager_adpt;
    ReceiveServiceMsg service_receiver;
    DB_Function db = null;
    Handler mhandler_list = null;
    Thread mthread_list = null;

    String ServiceReceiver_TAG = "retrieve_info.service.msg";
    String TAG = "KNOWLEDGE";

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

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
//                if (fragement_list.size() < url_db.length)
//                {
//                    fragement_list.add(url_db[fragement_list.size()]);
//                    pager_adpt.notifyDataSetChanged(); //refresh the fragment
//                }
            }
        });

        service_receiver = new ReceiveServiceMsg();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceReceiver_TAG);
        registerReceiver(service_receiver, intentFilter);

        //Log to database
        db = new DB_Function(Knowledge.this);

        //https://www.youtube.com
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

                //Show the fragment current url
//                size = fragement_list.size();
//                for (int i = 0; i < size; i++)
//                {
//                    Log.w(TAG, "Fragment---->" + fragement_list.get(i).toString());
//                }

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
}
