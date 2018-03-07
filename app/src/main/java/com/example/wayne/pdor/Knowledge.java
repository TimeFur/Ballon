package com.example.wayne.pdor;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
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

public class Knowledge extends AppCompatActivity{

    static String url_db[] = { "https://www.google.com.tw",
                        "https://pnn.tw/",
                        "https://gnn.gamer.com.tw/2/159522.html",
                        "https://www.bnext.com.tw/",
                        "https://buzzorange.com/techorange/"};

    static  ArrayList fragement_list = new ArrayList();
    Retrive_info retrive_info;
    PagerAdapter pager_adpt;
    ReceiveServiceMsg service_receiver;
    String ServiceReceiver_TAG = "retrieve_info.service.msg";
    String TAG = "KNOWLEDGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        //
        ViewPager view_pager = (ViewPager) findViewById(R.id.viewPager);
        pager_adpt = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(pager_adpt);

        //
        Button add_btn = (Button)findViewById(R.id.add_button);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

    public class ReceiveServiceMsg extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String _url = intent.getStringExtra("_url");
            fragement_list.add(_url);
            pager_adpt.notifyDataSetChanged(); //refresh the fragment

            Log.w(TAG, "Broadcast get info: " + _url);
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
