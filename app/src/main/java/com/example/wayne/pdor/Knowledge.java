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

public class Knowledge extends AppCompatActivity {

    String _url = "https://www.google.com.tw";

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
            return new ScreenSlidePageFragment();
        }
        @Override
        public int getCount() {
            return 5;
        }
    }

    static public class ScreenSlidePageFragment extends  android.support.v4.app.Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.screen_slide_page, container, false);

            return rootView;
        }
    }
}
