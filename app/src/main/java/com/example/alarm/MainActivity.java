package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    Button buttonNewAlarm;
    RecyclerView rvAlarms;
    AlarmAdapter adapter;

    Context context;
    List<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvAlarms = findViewById(R.id.AlarmRecyclerView);
        buttonNewAlarm = findViewById(R.id.buttonNewAlarm);

        buttonNewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditor(null);
            }
        });

        context = this;
        adapter = new AlarmAdapter(new ArrayList<Alarm>());
        rvAlarms.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        rvAlarms.setAdapter(adapter);
        rvAlarms.setLayoutManager(new LinearLayoutManager(context));

        new DBTasks(context).getAll(adapter);

    }

    public void viewItemOnClick(View v){
        int pos = rvAlarms.getChildAdapterPosition(v);
        Alarm clicked = alarms.get(pos);
        Toast.makeText(this, clicked.toString(), Toast.LENGTH_LONG).show();
        openEditor(clicked);
    }

    private void openEditor(Alarm a){
        Intent editorIntent = new Intent(getApplicationContext(), AlarmEditorActivity.class);
        editorIntent.putExtra(EXTRA_ALARM, a);
        startActivity(editorIntent);
    }

}
