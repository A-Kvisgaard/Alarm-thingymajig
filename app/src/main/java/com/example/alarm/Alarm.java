package com.example.alarm;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;

public class Alarm implements Comparable{
    private int id;
    private long time;
    private boolean on;
    private String text;

    public Alarm(int id, long time, String text, boolean on){
        this.id = id;
        this.time = time;
        this.text = text;
        this.on = on;
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

    public boolean today (){
        int hour = Calendar.HOUR_OF_DAY;
        int min = Calendar.MINUTE;

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
        //TODO Cancel/start alarm
    }

    public String getText() {
        return text+" ";
    }

    public static ArrayList<Alarm> createAlarmList(int numAlarms){
        ArrayList<Alarm> Alarms = new ArrayList<>();
        long now = Calendar.getInstance().getTimeInMillis();

        for (int i = 0; i <= numAlarms -1; i++){
            long alarmTime = now + i *(60*60*1000);//Add an hour per alarm
            Alarms.add(new Alarm(i, alarmTime,":::::LOREM IPSUM:::::", false));
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
        int hour = Calendar.HOUR_OF_DAY;
        int min = Calendar.MINUTE;

        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(time);
        Calendar other = Calendar.getInstance();
        other.setTimeInMillis(other_alarm.getTime());
        if (alarmTime.get(hour) < other.get(hour)){
            return -1;
        } else if (alarmTime.get(hour) == other.get(hour)){
            if (alarmTime.get(min) < other.get(min)){
                return -1;
            }
            return 1;
        }
        return 1;
    }
}
