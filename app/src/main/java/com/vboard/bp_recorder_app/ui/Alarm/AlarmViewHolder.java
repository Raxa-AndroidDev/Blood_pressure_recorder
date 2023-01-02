package com.vboard.bp_recorder_app.ui.Alarm;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vboard.bp_recorder_app.R;
import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.databinding.ItemAlarmBinding;
import com.vboard.bp_recorder_app.utils.DayUtil;
import com.vboard.bp_recorder_app.utils.interfaces.OnToggleAlarmListener;


public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;
    private SwitchCompat alarmStarted;
    private ImageButton deleteAlarm;
    private View itemView;
    private TextView alarmDay;

    public AlarmViewHolder(@NonNull ItemAlarmBinding itemAlarmBinding) {
        super(itemAlarmBinding.getRoot());
        alarmTime = itemAlarmBinding.itemAlarmTime;
        alarmStarted = itemAlarmBinding.itemAlarmStarted;
        alarmRecurring = itemAlarmBinding.itemAlarmRecurring;
        alarmRecurringDays = itemAlarmBinding.itemAlarmRecurringDays;
        alarmTitle = itemAlarmBinding.itemAlarmTitle;
        deleteAlarm = itemAlarmBinding.itemAlarmRecurringDelete;
        alarmDay = itemAlarmBinding.itemAlarmDay;
        this.itemView = itemAlarmBinding.getRoot();
    }

    public void bind(Alarm alarm, OnToggleAlarmListener listener) {

        alarmTime.setText(convert24To12System(alarm.getHour(), alarm.getMinute()));

        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);

            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText("My Alarm");
        }
        if (alarm.isRecurring()) {
            if (alarm.getRecurringDaysText().equals("")) {
                alarmDay.setText(DayUtil.getDay(alarm.getHour(), alarm.getMinute()));
            } else {
                alarmDay.setText(alarm.getRecurringDaysText());
            }

        } else {
            alarmDay.setText(DayUtil.getDay(alarm.getHour(), alarm.getMinute()));
        }
        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown() || buttonView.isPressed())
                    listener.onToggle(alarm);
            }
        });

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(alarm);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(alarm, view);
            }
        });
    }

    public static String convert24To12System(int hour, int minute) {
        String time = "";
        String am_pm = "";
        if (hour < 12) {
            if (hour == 0) hour = 12;
            am_pm = "AM";
        } else {
            if (hour != 12)
                hour -= 12;
            am_pm = "PM";
        }
        String h = hour + "", m = minute + "";
        if (h.length() == 1) h = "0" + h;
        if (m.length() == 1) m = "0" + m;
        time = h + ":" + m + " " + am_pm;
        return time;
    }
}