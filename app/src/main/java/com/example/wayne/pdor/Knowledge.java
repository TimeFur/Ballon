package com.example.wayne.pdor;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Knowledge extends AppCompatActivity {

    String _url = "https://www.google.com.tw";
    final int Fragment_num = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        ViewPager view_pager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pager_adpt = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(pager_adpt);

        //setting
//        WebView web_view = (WebView)findViewById(R.id.webView1);
//        WebSettings web_view_setting = web_view.getSettings();
//        web_view_setting.setJavaScriptEnabled(true);
//        web_view.loadUrl(_url);
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
            return Fragment_num;
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
            TextView tv = rootView.findViewById(R.id.screen_slide_tv1);
            tv.setText("Hi" + this.position);

            return rootView;
        }
    }
}
