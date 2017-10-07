package com.example.diary.other;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;

/**
 * Created by 周要明 on 2017/7/30.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
        Bmob.initialize(this, "833d9f9423746b8c6c06f3c91bf76398");
    }
    public static Context getContext(){
        return context;
    }
}
