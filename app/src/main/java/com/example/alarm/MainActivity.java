package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    //Button buttonNewAlarm;
    RecyclerView rvAlarms;

    ArrayList<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvAlarms = findViewById(R.id.AlarmRecyclerView);
        //buttonNewAlarm = findViewById(R.id.buttonNewAlarm);

        alarms = Alarm.createAlarmList(40);
        Collections.sort(alarms);

        AlarmAdapter adapter = new AlarmAdapter(alarms);
        rvAlarms.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvAlarms.setAdapter(adapter);
        rvAlarms.setLayoutManager(new LinearLayoutManager(this));

    }

    public void viewItemOnClick(View v){
        int pos = rvAlarms.getChildAdapterPosition(v);
        Alarm clicked = alarms.get(pos);
        Toast.makeText(this, clicked.toString(), Toast.LENGTH_LONG).show();
        //TODO Open edit activity
    }

}
