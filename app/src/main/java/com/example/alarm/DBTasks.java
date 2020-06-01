package com.example.alarm;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Collections;
import java.util.List;

public class DBTasks {

    private AlarmDao alarmDao;

    public DBTasks(Context context) {
        AlarmDatabase database = AlarmDatabase.getInstance(context);
        alarmDao = database.alarmDao();
    }

    public void getAll(AlarmAdapter adapter){
        new GetAllAlarms(alarmDao, adapter).execute();
    }

    public void insert(Alarm a) {
        new InsertAlarmAsyncTask(alarmDao).execute(a);
    }

    public void update(Alarm a) {
        new UpdateAlarmAsyncTask(alarmDao).execute(a);
    }

    public void delete(Alarm a) {
        new DeleteAlarmAsyncTask(alarmDao).execute(a);
    }

    private static class GetAllAlarms extends AsyncTask<Void,Void, List<Alarm>>{

        AlarmAdapter adapter;
        AlarmDao dao;

        GetAllAlarms(AlarmDao dao, AlarmAdapter adapter){
            this.dao = dao;
            this.adapter = adapter;
        }

        @Override
        protected List<Alarm> doInBackground(Void... voids) {
            List<Alarm> tmp = dao.getAll();
            Collections.sort(tmp);
            return tmp;
        }

        @Override
        protected void onPostExecute(List<Alarm> alarms){
            adapter.updateData(alarms);
        }
    }

    private static class InsertAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao alarmDao;
        private InsertAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }
        @Override
        protected Void doInBackground(Alarm... Alarms) {
            Alarms[0].setId((int) alarmDao.insertAlarm(Alarms[0]));
            return null;
        }
    }

    private static class UpdateAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao alarmDao;
        private UpdateAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }
        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.updateAlarm(alarms[0]);
            return null;
        }
    }

    private static class DeleteAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao alarmDao;
        private DeleteAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }
        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.deleteAlarm(alarms[0]);
            return null;
        }
    }
}
