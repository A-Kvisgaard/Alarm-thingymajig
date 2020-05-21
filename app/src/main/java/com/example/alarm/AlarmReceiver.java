package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver  extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Recived","TRUE");
        NotificationPusher pusher = new NotificationPusher(context);
        NotificationCompat.Builder nb = pusher.getChannelNotification("Alarm!","Wake up you lazy bugger");
        pusher.getManager().notify(1, nb.build());
    }
}
