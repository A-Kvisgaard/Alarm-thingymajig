package com.example.alarm;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(long time) {
        this.time = time;
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

    public void toggle(){
        on = !on;
        //dao.updateAlarm(this);
        //TODO Cancel/start alarm
    }

    public String getText() {
        return text;
    }

    public static ArrayList<Alarm> createAlarmList(int numAlarms){
        ArrayList<Alarm> Alarms = new ArrayList<>();
        long now = Calendar.getInstance().getTimeInMillis();

        for (int i = 0; i <= numAlarms -1; i++){
            long alarmTime = now + i *(60*60*1000);//Add an hour per alarm
            Alarms.add(new Alarm(i, alarmTime,":::::LOREM IPSUM::::::::::LOREM IPSUM::::::::::LOREM IPSUM::::::::::LOREM IPSUM::::::::::LOREM IPSUM:::::", false));
        }

        return Alarms;
    }

    @NonNull
    @Override
    public String toString() {
        return getId()+ " | " + getTimeString()+ " | " + isOn();
    }

    @Override
    public int compareTo(Object o) {
        Alarm other_alarm = (Alarm) o;
        return this.getTimeString().compareTo(other_alarm.getTimeString());
    }
}
