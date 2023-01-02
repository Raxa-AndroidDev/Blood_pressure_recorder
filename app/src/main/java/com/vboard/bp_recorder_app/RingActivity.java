package com.vboard.bp_recorder_app;

import static com.vboard.bp_recorder_app.ui.Alarm.AlarmViewHolder.convert24To12System;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.vboard.bp_recorder_app.alarm_service.AlarmService;
import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.databinding.ActivityRingBinding;
import com.vboard.bp_recorder_app.ui.blood_pressure.BPRecordViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RingActivity extends AppCompatActivity {
    Alarm alarm;
    private BPRecordViewModel alarmsListViewModel;
    private ActivityRingBinding ringActivityViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ringActivityViewBinding = ActivityRingBinding.inflate(getLayoutInflater());

        setContentView(ringActivityViewBinding.getRoot());
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }


        alarmsListViewModel = ViewModelProviders.of(this).get(BPRecordViewModel.class);

        Bundle bundle = getIntent().getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle != null)
            alarm = (Alarm) bundle.getSerializable(getString(R.string.arg_alarm_obj));


        DateFormat df = new SimpleDateFormat("h:mm");
       // String date = df.format(Calendar.getInstance().getTime());
        Log.d("currenttime",df.format(Calendar.getInstance().getTime()));


        if (convert24To12System(alarm.getHour(), alarm.getMinute()).contains("AM")) {
            ringActivityViewBinding.textView.setText(df.format(Calendar.getInstance().getTime()));
            ringActivityViewBinding.tvampm.setText("Am");
        } else if (convert24To12System(alarm.getHour(), alarm.getMinute()).contains("PM")) {
            ringActivityViewBinding.textView.setText(df.format(Calendar.getInstance().getTime()));
            ringActivityViewBinding.tvampm.setText("Pm");
        }

        String currentDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date());
        ringActivityViewBinding.textdate.setText(currentDate);

        ringActivityViewBinding.activityRingDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAlarm();
            }
        });

        ringActivityViewBinding.activityRingSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snoozeAlarm();
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false);
            setTurnScreenOn(false);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }
    }

    private void dismissAlarm() {
        Log.d("isreccuring",""+alarm.isRecurring());
        if (alarm != null) {


            alarm.cancelAlarm(getBaseContext());
            alarmsListViewModel.update(alarm);
        }
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }

    private   void snoozeAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 5);

        if (alarm != null) {
            alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            alarm.setMinute(calendar.get(Calendar.MINUTE));
            alarm.setTitle("Snooze " + alarm.getTitle());
        } else {
            alarm = new Alarm(
                    new Random().nextInt(Integer.MAX_VALUE),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    "Snooze",
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_ALARM).toString(),
                    false
            );
        }
        alarm.schedule(getApplicationContext());

        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }
}