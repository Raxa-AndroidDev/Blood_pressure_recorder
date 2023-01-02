package com.vboard.bp_recorder_app.utils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.vboard.bp_recorder_app.R;

public class workActivity extends AppCompatActivity {
    public static final String MESSAGE_STATUS = "message_status";
    TextView tvStatus;
    Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        tvStatus = findViewById(R.id.textView);
        btnSend = findViewById(R.id.button);
        final WorkManager mWorkManager = WorkManager.getInstance();
        final OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkManager.enqueue(mRequest);
            }
        });
        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    tvStatus.append(state.toString() + "\n");
                }
            }
        });
    }
}