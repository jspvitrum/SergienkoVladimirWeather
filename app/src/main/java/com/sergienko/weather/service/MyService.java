package com.sergienko.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    Timer myTimer;
    private final MyBinder mBinder = new MyBinder();

    public MyService() { MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTask, 400, 60000);}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onStartCommand", "onStartCommand ");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.d("onStopCommand", "onStopCommand ");
        myTimer.cancel();
        super.onDestroy();
    }

    public static class MyBinder extends Binder {

        public static void alarm () {
            Log.d("Alarm", "Alarm");
        }
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            MyBinder.alarm();

        }
    }
}
