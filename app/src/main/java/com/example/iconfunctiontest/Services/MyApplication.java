package com.example.iconfunctiontest.Services;

import android.app.Application;
import android.content.Context;


//This class is only needed to get application context statically
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return MyApplication.context;
    }

}
