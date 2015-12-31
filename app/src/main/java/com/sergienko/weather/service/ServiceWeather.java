package com.sergienko.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceWeather extends Service {

    public static final String ALARM_ACTION = "ua.ck.jspvitrum";
    public static final String ALARM_MESSAGE = "Внимание есть обновление";

    Timer myTimer;
    private final MyBinder mBinder = new MyBinder();

    public ServiceWeather() {
        MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTask, 400, 60000);}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(ServiceWeather.this, "onStartCommand", Toast.LENGTH_LONG).show();

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
            Intent intent = new Intent();
            intent.setAction(ALARM_ACTION);
            intent.putExtra("ua.ck.jspvitrum", ALARM_MESSAGE);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendBroadcast(intent);
            MyBinder.alarm();

        }
    }
}

