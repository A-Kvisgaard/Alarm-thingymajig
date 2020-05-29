package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AlarmTriggeredActivity extends AppCompatActivity {

    TextView textTV, dateTV;
    Vibrator vibrator;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_triggered);

        textTV = findViewById(R.id.alarmText);
        dateTV = findViewById(R.id.dateTextView);

        Alarm alarm = (Alarm) getIntent().getSerializableExtra(MainActivity.EXTRA_ALARM);
        if (alarm != null){
            textTV.setText(alarm.getText());
            dateTV.setText(alarm.toString());
        }

        vibrator = (Vibrator) getSystemService(Activity.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, 0);

        Uri toneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        if (toneUri == null){
            toneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
        }
        mp = MediaPlayer.create(getApplicationContext(), toneUri);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
        mp.stop();
        mp.release();
    }

    public void dismissAlarm(View v){
        vibrator.cancel();
        mp.stop();
        mp.release();
        finishAffinity();
    }
}
