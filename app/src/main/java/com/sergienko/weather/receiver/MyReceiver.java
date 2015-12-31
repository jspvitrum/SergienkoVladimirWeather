package com.sergienko.weather.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Toast;

import com.sergienko.weather.R;
import com.sergienko.weather.main.activity.MainActivity;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }
    private static final int NOTIFY_ID = 101;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Обнаружено сообщение: " +
                        intent.getStringExtra("ua.ck.jspvitrum"),
                Toast.LENGTH_LONG).show();


        context = context.getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent childPIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.alarm)
                .setTicker("Имеется обновление погоды!")
                .setContentTitle("Внимание")
                .setContentText("Имеется обновленный прогноз погоды!")
        .setAutoCancel(true);

builder.setContentIntent(childPIntent);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
                      notificationManager.notify(NOTIFY_ID, notification);

    }
}

