package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver  extends BroadcastReceiver{

    private static final long day = 86400000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent triggered = new Intent(context, AlarmTriggeredActivity.class);
        Bundle nested = (Bundle) intent.getExtras().get("bundle");
        NotificationPusher pusher = new NotificationPusher(context);
        NotificationCompat.Builder nb;

        if (nested != null) {
            Alarm passedAlarm = (Alarm) nested.get(MainActivity.EXTRA_ALARM);
            if (passedAlarm != null) {
                nb = pusher.getChannelNotification("Alarm!", passedAlarm.getText());
                triggered.putExtra(MainActivity.EXTRA_ALARM, passedAlarm);
                DBTasks dbTasks = new DBTasks(context);
                passedAlarm.setTime(passedAlarm.getTime() + day); //update to trigger following day
                passedAlarm.set(dbTasks, context);
            } else {
                nb = pusher.getChannelNotification("Alarm!", "");
            }
            pusher.getManager().notify(1, nb.build());
        }

        triggered.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(triggered);
    }
}
