package com.vboard.bp_recorder_app.utils.interfaces;

import android.view.View;

import com.vboard.bp_recorder_app.data.Alarm;


public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
    void onItemClick(Alarm alarm,View view);
}
