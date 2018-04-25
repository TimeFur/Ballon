package com.example.wayne.pdor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Wayne on 2018/4/19.
 */

public class Floating_window extends Service{

    Handler handler;

    public void onCreate(){
        super.onCreate();

//        handler = new Handler();
//        handler.post(floating_thread);

        //Bind Setting
//        win_manage.addView(win_view, wmParams);
        create_floating();
//        floating2();
    }

    void create_floating(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        WindowManager win_manage = null;
        WindowManager.LayoutParams win_param = null;

        win_manage = (WindowManager) getSystemService(WINDOW_SERVICE);
        View win_view;
        win_view = layoutInflater.inflate(R.layout.floating_layout, null);
        ImageView img = win_view.findViewById(R.id.floating_view);
        img.setImageResource(R.drawable.ic_ballon);

        //Setting window parameter
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // >= API26
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // >= API19
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        else
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
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

    Runnable floating_thread = new Runnable() {
        @Override
        public void run() {
            create_floating();
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public class localBinder extends Binder {
        Floating_window getBinder(){
            return Floating_window.this;
        }
    }

    void floating2()
    {
        WindowManager windowManager2 = (WindowManager)getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.floating_layout, null);
        ImageView img = view.findViewById(R.id.floating_view);
        img.setImageResource(R.drawable.ic_ballon);
        WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity=Gravity.CENTER|Gravity.CENTER;
        params.x=0;
        params.y=0;
        windowManager2.addView(view, params);
    }

}