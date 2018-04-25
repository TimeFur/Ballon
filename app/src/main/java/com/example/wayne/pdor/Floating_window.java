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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Wayne on 2018/4/19.
 */

public class Floating_window extends Service{

    String TAG = "Floating_window";

    public void onCreate(){
        super.onCreate();

        create_floating();
    }

    void create_floating(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //Declare the WinManager, Layout & View ------Step1
        final WindowManager win_manage = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams win_param = new WindowManager.LayoutParams();;
        final View win_view;

        //Setting the WindowManager  ------Step2.1
        win_view = layoutInflater.inflate(R.layout.floating_layout, null);
        ImageView img = win_view.findViewById(R.id.floating_view);
        img.setImageResource(R.drawable.ic_ballon);

        //Setting the Layout parameter -----Step2.2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // >= API26
            win_param.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // >= API19
            win_param.type = WindowManager.LayoutParams.TYPE_TOAST;
        else
            win_param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        win_param.format = PixelFormat.TRANSLUCENT;
        win_param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        win_param.gravity = Gravity.LEFT | Gravity.TOP;
        win_param.width = 100;
        win_param.height = 100;

        //Bind Setting from winView & winParam -----Step3
        win_manage.addView(win_view, win_param);

        win_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int location_x = 0;
                int location_y = 0;
                float touch_x = 0;
                float touch_y = 0;
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "View touch down!!!!");
                        location_x = win_param.x;
                        location_y = win_param.y;
                        touch_x = event.getRawX();
                        touch_y = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v(TAG, "View touch moving!!!!");
                        win_param.x = location_x + (int)( event.getRawX() - touch_x);
                        win_param.y = location_y + (int)( event.getRawY() - touch_y);
                        win_manage.updateViewLayout(win_view, win_param);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v(TAG, "View touch up!!!!");

                        break;
                }

                return true;
            }
        });
    }

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

}