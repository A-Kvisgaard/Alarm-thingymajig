package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    TimePicker picker;
    TextView alarmStatusText;
    Button alarmOn, alarmOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = findViewById(R.id.AlertTimePicker);
        picker.setIs24HourView(true);

        alarmOn = findViewById(R.id.AlarmOn);
        alarmOff = findViewById(R.id.AlarmOff);


    }
    public void AlarmOn(View v){
        alarmStatusText = findViewById(R.id.AlarmStatusText);
        alarmStatusText.setText(R.string.alarm_on_text);
    }

    public void AlarmOff(View v){
        alarmStatusText = findViewById(R.id.AlarmStatusText);
        alarmStatusText.setText(R.string.alarm_off_text);
    }
}
