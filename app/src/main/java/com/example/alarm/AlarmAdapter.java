package com.example.alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView alarmTimeTextView, alarmInfoTextView;
        public Switch onSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            alarmTimeTextView = itemView.findViewById(R.id.textViewAlarmTime);
            alarmInfoTextView = itemView.findViewById(R.id.textViewAlarmInfo);
            onSwitch = itemView.findViewById(R.id.switchAlarm);
        }
    }

    private List<Alarm> lAlarms;

    public AlarmAdapter(List<Alarm> alarms){
        lAlarms = alarms;
    }

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View alarmView = inflater.inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(alarmView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder holder, int position) {
        final Alarm alarm = lAlarms.get(position);

        TextView timeTV = holder.alarmTimeTextView;
        timeTV.setText(alarm.getTimeString());

        TextView infoTV = holder.alarmInfoTextView;
        Context context = holder.itemView.getContext();
        String next = alarm.today() ? context.getString(R.string.today) : context.getString(R.string.tomorrow);
        infoTV.setText(next + "  " + alarm.getText());

        Switch onSwitch = holder.onSwitch;
        onSwitch.setChecked(alarm.isOn());

        onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lAlarms.size();
    }

}
