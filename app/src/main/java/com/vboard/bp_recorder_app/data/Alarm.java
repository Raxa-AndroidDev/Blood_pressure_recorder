package com.vboard.bp_recorder_app.data;

import static com.vboard.bp_recorder_app.ui.Alarm.AlarmViewHolder.convert24To12System;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.vboard.bp_recorder_app.R;
import com.vboard.bp_recorder_app.ui.Alarm.AlarmBroadcastReceiver;
import com.vboard.bp_recorder_app.utils.DayUtil;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "alarm_table")
public class Alarm implements Serializable {
    @PrimaryKey
    @NonNull
    private int alarmId;
    private int hour, minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title;
    private String tone;
    private boolean vibrate;

    public Alarm(int alarmId, int hour, int minute, String title, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, String tone, boolean vibrate) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.title = title;
        this.vibrate = vibrate;
        this.tone = tone;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public void schedule(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj), this);
        intent.putExtra(context.getString(R.string.bundle_alarm_obj), bundle);

        PendingIntent alarmPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {


             alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_MUTABLE);

        }
        else
        {
             alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        }



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Log.e("TAG", "schedule: "+calendar.get(Calendar.HOUR_OF_DAY)+"  min:"+calendar.get( Calendar.MINUTE ));

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = "One Time Alarm " + title + " scheduled for " + DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)) + " at " + convert24To12System(hour, minute);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();


            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
            );

            Toast.makeText(context,"alarm set successfully", Toast.LENGTH_LONG).show();


        } else {
            String toastText = "Repeat Alarm " + title + " scheduled for " + getRecurringDaysText() + " at " + convert24To12System(hour, minute);

            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    RUN_DAILY,
                    alarmPendingIntent
            );
        }

        this.started = true;
        //  alarmRecyclerViewAdapter.notifyDataSetChanged();

    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        if (isRecurring()) {
            this.started = true;
        } else {
            this.started = false;
        }
        String toastText = "Alarm cancelled for " + convert24To12System(hour, minute);

        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }

    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += " Mon";
        }
        if (tuesday) {
            days += " Tue";
        }
        if (wednesday) {
            days += " Wed";
        }
        if (thursday) {
            days += " Thu";
        }
        if (friday) {
            days += " Fri";
        }
        if (saturday) {
            days += " Sat";
        }
        if (sunday) {
            days += " Sun";
        }
        if (saturday && sunday) {
            days = "Weekend";
        }
        if (monday && tuesday && wednesday && thursday && friday) {
            days = "Weekdays";
        }
        if (monday && tuesday && wednesday && thursday && friday && saturday && sunday) {
            days = "Everyday";
        }

        return days;
    }

    public String getTitle() {
        return title;
    }
}
