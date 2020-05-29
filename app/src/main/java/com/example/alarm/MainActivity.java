package com.example.alarm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ALARM = "com.example.alarm.ALARM";
    public static final String EXTRA_POSITION = "com.example.alarm.POSITION";
    public static final String EXTRA_ACTION = "com.example.alarm.ACTION";

    Button buttonNewAlarm;
    RecyclerView rvAlarms;
    AlarmAdapter adapter;

    Context context;
    DBTasks dbTasks;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data == null) return;

            int pos = data.getIntExtra(EXTRA_POSITION, -1);
            int action = data.getIntExtra(EXTRA_ACTION, 0);
            Alarm alarm = (Alarm) data.getSerializableExtra(EXTRA_ALARM);

            if (alarm == null) return;

            switch (action){
                case AlarmEditorActivity.ALARM_EDIDTED:
                    if (pos == -1) return;
                    if (alarm.isOn()){
                        //Update PendingIntent
                        alarm.cancel(dbTasks, context);
                        alarm.set(dbTasks, context);
                    }
                    adapter.updateAlarm(alarm, pos);
                    break;
                case AlarmEditorActivity.ALARM_DELETED:
                    if (pos == -1) return;
                    if (alarm.isOn()){
                        alarm.cancel(dbTasks, context);
                    }
                    adapter.removeAlarm(pos);
                    break;
                case AlarmEditorActivity.ALARM_ADDED:
                    adapter.insertAlarm(alarm);
                    alarm.set(dbTasks, context);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        dbTasks = new DBTasks(context);

        rvAlarms = findViewById(R.id.AlarmRecyclerView);
        buttonNewAlarm = findViewById(R.id.buttonNewAlarm);

        buttonNewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditor(null, -1);
            }
        });

        adapter = new AlarmAdapter(new ArrayList<Alarm>(), dbTasks);
        rvAlarms.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        rvAlarms.setAdapter(adapter);
        rvAlarms.setLayoutManager(new LinearLayoutManager(context));

        new DBTasks(context).getAll(adapter);

    }

    public void viewItemOnClick(View v){
        int pos = rvAlarms.getChildAdapterPosition(v);
        Alarm clicked = adapter.getAlarm(pos);
        Toast.makeText(this, clicked.toString(), Toast.LENGTH_LONG).show();
        openEditor(clicked, pos);
    }

    private void openEditor(Alarm a, int pos){
        Intent editorIntent = new Intent(getApplicationContext(), AlarmEditorActivity.class);
        editorIntent.putExtra(EXTRA_ALARM, a);
        editorIntent.putExtra(EXTRA_POSITION, pos);
        startActivityForResult(editorIntent, 1);
    }
}
