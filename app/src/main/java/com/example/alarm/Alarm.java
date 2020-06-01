package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Alarm implements Comparable, Serializable {

    @Ignore
    public static final int ID_NOT_SET = 0;//primary key treated as not set when 0

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long time;
    private boolean on;
    private String text;

    @Ignore
    private int hour = Calendar.HOUR_OF_DAY;
    @Ignore
    private int min = Calendar.MINUTE;

    public Alarm(int id, long time, String text, boolean on){
        this.id = id;
        this.time = time;
        this.text = text;
        this.on = on;
    }

    public int getHour(){
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(time);

        return alarmTime.get(hour);
    }
    public int getMinute(){
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(time);

        return alarmTime.get(min);
    }

    public int getId() {
        return id;
    }

    public boolean isOn() {
        return on;
    }

    public long getTime() {
        return time;
    }

    public String getTimeString() {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(time);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setId(int id ){
        this.id = id;
    }

    public boolean today (){
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(time);
        Calendar now = Calendar.getInstance();

        if (alarmTime.get(hour) > now.get(hour)){
            return true;
        } else if (alarmTime.get(hour) == now.get(hour)){
            return alarmTime.get(min) > now.get(min);
        }
        return false;
    }

    public void toggle(DBTasks dbTasks, Context context){
        if (isOn()){
            cancel(dbTasks, context);
        } else {
            set(dbTasks, context);
        }
    }

    public void set(DBTasks dbTasks, Context context){
        on = true;

        if (time < System.currentTimeMillis()){
            Calendar newTime = Calendar.getInstance();
            newTime.set(hour, getHour());
            newTime.set(min, getMinute());
            if (newTime.before(Calendar.getInstance())){
                newTime.add(Calendar.DATE, 1);
            }
            setTime(newTime.getTimeInMillis());
        }

        dbTasks.update(this);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        //Had to put alarm in bundle for it not to null in the receiver
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.EXTRA_ALARM, this);
        intent.putExtra("bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getTime(), pendingIntent);

    }

    public void cancel(DBTasks dbTasks, Context context){
        on = false;

        dbTasks.update(this);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(MainActivity.EXTRA_ALARM, this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.id, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

    public String timeTill(){
        String time = "";
        long till = this.getTime() - System.currentTimeMillis();
        int days   = (int) (till / (1000*60*60*24));
        int hours   = (int) ((till - days*1000*60*60*24) / (1000*60*60));
        int minutes = (int) ((till - days*1000*60*60*24 - hours * 1000*60*60) / (1000*60));

        if (days > 0){
            time = String.format("%d days, %d hours and %d minutes",days,hours,minutes);
        } else {
            time = String.format("%02d hours and %02d minutes",hours,minutes);
        }

        return "Alarm rings in " + time;
    }

    @NonNull
    @Override
    public String toString() {
        return new SimpleDateFormat("dd-M hh:mm").format(new Date(getTime()));
    }

    @Override
    public int compareTo(Object o) {
        Alarm other_alarm = (Alarm) o;
        int compare = Integer.compare(this.getHour(), other_alarm.getHour());
        if (compare == 0){
            return Integer.compare(this.getMinute(), this.getMinute());
        }
        return compare;
    }

}
