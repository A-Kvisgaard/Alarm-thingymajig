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
import java.util.Collections;
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
    private DBTasks dbTasks;

    public AlarmAdapter(List<Alarm> alarms, DBTasks dbTasks){
        lAlarms = alarms;
        this.dbTasks = dbTasks;
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
    public void onBindViewHolder(@NonNull final AlarmAdapter.ViewHolder holder, int position) {
        Alarm alarm = lAlarms.get(position);

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
               lAlarms.get(holder.getAdapterPosition()).toggle(dbTasks, holder.onSwitch.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lAlarms.size();
    }

    public Alarm getAlarm(int position){
        return lAlarms.get(position);
    }

    public void removeAlarm(int position){
        this.lAlarms.remove(position);
        notifyItemRemoved(position);
        Log.d("Remove", "removeAlarm: "+position);
    }

    public void insertAlarm(Alarm a){
        this.lAlarms.add(a);
        Collections.sort(lAlarms);
        notifyItemInserted(lAlarms.indexOf(a));
    }

    public void updateAlarm(Alarm a, int pos){
        lAlarms.set(pos, a);
        notifyItemChanged(pos);
    }

    void updateData(List<Alarm> alarms){
        this.lAlarms.clear();
        this.lAlarms.addAll(alarms);
        notifyDataSetChanged();
    }
}