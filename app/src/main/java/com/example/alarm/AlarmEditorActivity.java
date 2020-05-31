
package com.example.alarm;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmEditorActivity extends AppCompatActivity {

    public static final int ALARM_EDIDTED = 111;
    public static final int ALARM_DELETED = 112;
    public static final int ALARM_ADDED = 113;

    private int position;

    Button saveButton, deleteButton;
    ImageView clearReminder;
    TimePicker picker;
    EditText reminderText;
    AlarmManager alarmManager;
    Alarm alarm;
    DBTasks dbTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_editor);

        dbTasks = new DBTasks(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        reminderText = findViewById(R.id.editTextReminder);
        reminderText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && reminderText.getText().toString().equals(getString(R.string.reminder))){
                    clearReminder(null);
                }
            }
        });

        picker = findViewById(R.id.AlertTimePicker);
        picker.setIs24HourView(true);

        position = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, -1);

        alarm = (Alarm) getIntent().getSerializableExtra(MainActivity.EXTRA_ALARM);

        if (alarm != null){
            reminderText.setText(alarm.getText());
            picker.setHour(alarm.getHour());
            picker.setMinute(alarm.getMinute());
            deleteButton.setVisibility(View.VISIBLE);
        }

    }

    public void SaveButtonPressed(View v) {
        if (alarm == null){
            alarm = new Alarm(Alarm.ID_NOT_SET, getPickerTime(), getReminder(), false);
            dbTasks.insert(alarm);
            finish(ALARM_ADDED, alarm);
        }
        alarm.setText(getReminder());
        alarm.setTime(getPickerTime());
        dbTasks.update(alarm);
        finish(ALARM_EDIDTED, alarm);
    }

    public void deleteButtonPressed(View v){
        dbTasks.delete(alarm);
        finish(ALARM_DELETED, alarm);
    }

    public void clearReminder(View v){
        reminderText.setText("");
    }

    private String getReminder() {
        return (reminderText.getText().toString().equals(getString(R.string.reminder))) ? "" : reminderText.getText().toString();
    }

    private long getPickerTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
        calendar.set(Calendar.MINUTE, picker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTimeInMillis();
    }

    private void finish(int action, Alarm alarm){
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_POSITION, position);
        intent.putExtra(MainActivity.EXTRA_ACTION, action);
        intent.putExtra(MainActivity.EXTRA_ALARM, alarm);
        setResult(RESULT_OK, intent);
        finish();
    }
}
