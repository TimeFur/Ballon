package com.example.wayne.pdor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * Created by Wayne on 2018/4/19.
 */

public class Floating_window extends Service{

    public void onCreate(){
        super.onCreate();

        LayoutInflater inflater = LayoutInflater.from(this);
        WindowManager win_manage = null;
        WindowManager.LayoutParams  win_param = null;

        win_manage = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        win_param = new WindowManager.LayoutParams();
        View win_view = inflater.inflate(R.layout.floating_layout, null);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.ic_launcher_background);

        //Setting window parameter
//        win_param.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG;
//        win_param.height = 200;
//        win_param.width = 200;
//        win_param.gravity = Gravity.LEFT;
//        win_param.x = 0;
//        win_param.y = 0;
        win_param = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        win_param.gravity = Gravity.TOP | Gravity.LEFT;
        win_param.x = 0;
        win_param.y = 100;
        //Bind Setting
        win_manage.addView(img, win_param);
    }

//    void floating_start(){
//        LayoutInflater inflater = LayoutInflater.from(this.main_context);
//        WindowManager win_manage = null;
//        WindowManager.LayoutParams  win_param = null;
//
//        win_manage = (WindowManager) this.main_context.getSystemService(Context.WINDOW_SERVICE);
//        win_param = new WindowManager.LayoutParams();
//        View win_view = inflater.inflate(R.layout.floating_layout, null);
//
//        //Setting window parameter
//        win_param.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG;
//
//        //Bind Setting
//        win_manage.addView(win_view, win_param);
//    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}