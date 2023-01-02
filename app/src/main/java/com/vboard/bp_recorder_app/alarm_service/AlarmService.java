package com.vboard.bp_recorder_app.alarm_service;


import static android.media.MediaPlayer.*;
import static com.vboard.bp_recorder_app.App.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.vboard.bp_recorder_app.R;
import com.vboard.bp_recorder_app.RingActivity;
import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.ui.Alarm.AlarmViewHolder;

import java.io.IOException;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    NotificationManager notificationManager;
    Alarm alarm;
    Uri ringtone;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        CreateNotificationChannel();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ringtone = RingtoneManager.getActualDefaultRingtoneUri(this.getBaseContext(), RingtoneManager.TYPE_ALARM);
    }

    private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "This channerl";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
        Log.d("Bundle", "Bundle " + bundle);
        if (bundle != null)
            alarm = (Alarm) bundle.getSerializable(getString(R.string.arg_alarm_obj));
        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra(getString(R.string.bundle_alarm_obj), bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String alarmTitle = getString(R.string.alarm_title);
        if (alarm != null) {
            alarmTitle = alarm.getTitle();
            try {
                mediaPlayer.setDataSource(this.getBaseContext(), Uri.parse(alarm.getTone()));
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {

            try {
                mediaPlayer.setDataSource(this.getBaseContext(), ringtone);
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_white)
                .setTicker("Alarm")
                .setContent(remoteViews)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true);

        remoteViews.setTextViewText(R.id.tv_subtitle, "Reminder: "+"'"+alarmTitle+"''");
        remoteViews.setTextViewText(R.id.tv_time, AlarmViewHolder.convert24To12System(alarm.getHour(), alarm.getMinute()));



        mediaPlayer.setOnPreparedListener(mediaPlayer -> mediaPlayer.start());

        if (alarm.isVibrate()) {
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }
        startForeground(1, notification.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
