package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    PendingIntent pendingIntent;
    TimePicker picker;
    TextView alarmStatusText;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmStatusText = findViewById(R.id.AlarmStatusText);

        picker = findViewById(R.id.AlertTimePicker);
        picker.setIs24HourView(true);

    }
    public void AlarmOn(View v){
        setStatusText(R.string.alarm_on_text);
        Intent intent = new Intent(this, Alarm_Receiver.class);
        intent.putExtra("extra", true);
        pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getPickerTime(), pendingIntent);
    }

    public void AlarmOff(View v){
        if (pendingIntent != null) {
            setStatusText(R.string.alarm_off_text);
            toast(getString(R.string.alarm_off_text));
            alarmManager.cancel(pendingIntent);
            pendingIntent = null;
        }
    }

    private void setStatusText(int output){
        alarmStatusText.setText(output);
    }

    private long getPickerTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
        calendar.set(Calendar.MINUTE, picker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1);
        }
        String date = calendar.get(Calendar.MONTH) +"/" + calendar.get(Calendar.DATE) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        toast(getString(R.string.alarm_on_text) + ": " + date);
        return calendar.getTimeInMillis();
    }
    private void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
