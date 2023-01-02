package com.vboard.bp_recorder_app.ui.Alarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class notificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals("snooze")) {
               // snoozeAlarm();
            } else if (intent.getAction().equals("stop")) {
              //  dismissAlarm();
            }
            Toast.makeText(context, "" + intent.getAction(), Toast.LENGTH_SHORT).show();
        }
    }

}
