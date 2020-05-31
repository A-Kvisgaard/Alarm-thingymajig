package com.example.alarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmTriggeredActivity extends AppCompatActivity {

    TextView textTV, dateTV;
    Vibrator vibrator;
    MediaPlayer mp;

    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    //private Button button;
    private ImageView imageView;

    private double lat;
    private double lon;

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

        //button = findViewById(R.id.button);
        textView = findViewById(R.id.weatherInformationTextView);
        imageView = findViewById(R.id.weatherImageView);




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                APICall(lat, lon);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
        } else {
            locationUpdates();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
        }
    }

    private void locationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return; //Returns if any of them do not have Permission Granted
            }
        }
        locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
    }

    public void APICall(double lat, double lon) {
        String strlat = Double.toString(lat);
        strlat = strlat.substring(0, 8);
        String strlon = Double.toString(lon);
        strlon = strlon.substring(0, 8);
        String APIKey = "f6c3a3f6cdb9fe5301ecd683e89bad1d"; //Personalized APIKey needed to get Data.
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + strlat + "&lon=" + strlon + "&appid=" + APIKey;
        final String[] imageId = {null};

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject weather = jsonArray.getJSONObject(0);
                            String main = weather.getString("main");
                            String description = weather.getString("description");
                            imageId[0] = weather.getString("icon");

                            textView.setText("\nThe weather is currently:\n" + main + " (" + description + ")\n");

                            String url2 = "https://openweathermap.org/img/wn/" + imageId[0] + "@4x.png";
                            Picasso.get().load(url2).into(imageView);

                            JSONObject jsonObject2 = response.getJSONObject("main");
                            double temp = jsonObject2.getDouble("temp");
                            double celsius = temp - 272.15;
                            NumberFormat nf1 = NumberFormat.getNumberInstance();
                            nf1.setMaximumFractionDigits(1);
                            String degree = nf1.format(celsius);

                            textView.append("\n The current temperature is:\n" + degree + " Degrees Celsius\n");

                            JSONObject jsonObject3 = response.getJSONObject("wind");
                            double speed = jsonObject3.getDouble("speed");
                            NumberFormat nf2 = NumberFormat.getNumberInstance();
                            nf2.setMaximumFractionDigits(2);
                            String speed2 = nf2.format(speed);

                            textView.append("\n Expect Wind Speeds of:\n" + speed2 + " M/s\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(request);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
        if (mp.isPlaying()){ mp.stop();}
        mp.release();
    }

    public void dismissAlarm(View v){
        vibrator.cancel();
        if (mp.isPlaying()){ mp.stop();}
        finishAffinity();
    }
}
