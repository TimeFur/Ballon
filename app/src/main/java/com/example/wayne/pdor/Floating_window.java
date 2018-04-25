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

    public void onCreate(){
        super.onCreate();
        create_floating();
    }

    void create_floating(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //Declare the WinManager, Layout & View ------Step1
        WindowManager win_manage = null;
        WindowManager.LayoutParams win_param = new WindowManager.LayoutParams();;
        View win_view;

        //Setting the WindowManager  ------Step2.1
        win_manage = (WindowManager) getSystemService(WINDOW_SERVICE);

        //Setting the WindowManager  ------Step2.2
        win_view = layoutInflater.inflate(R.layout.floating_layout, null);
        ImageView img = win_view.findViewById(R.id.floating_view);
        img.setImageResource(R.drawable.ic_ballon);

        //Setting the Layout parameter -----Step2.3
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