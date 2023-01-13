package com.vboard.bp_recorder_app.alarm_service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel;

import java.util.List;


public class RescheduleAlarmsService extends LifecycleService {
    private BPRecordViewModel alarmsListViewModel;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        alarmsListViewModel = ViewModelProviders.of((FragmentActivity) getApplicationContext()).get(BPRecordViewModel.class);


        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                for (Alarm a : alarms) {
                    if (a.isStarted()) {
                        a.schedule(getApplicationContext());
                    }
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
