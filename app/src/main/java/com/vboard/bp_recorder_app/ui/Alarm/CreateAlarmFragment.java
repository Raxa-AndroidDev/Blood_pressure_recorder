package com.vboard.bp_recorder_app.ui.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.vboard.bp_recorder_app.R;
import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.databinding.FragmentCreateAlarmBinding;
import com.vboard.bp_recorder_app.ui.blood_pressure.BPRecordViewModel;
import com.vboard.bp_recorder_app.utils.DayUtil;
import com.vboard.bp_recorder_app.utils.TimePickerUtil;

import java.util.Calendar;
import java.util.Random;

public class CreateAlarmFragment extends Fragment {
    FragmentCreateAlarmBinding binding;
    private BPRecordViewModel createAlarmViewModel;
    boolean isVibrate = false;
    String tone;
    Alarm alarm;
    Ringtone ringtone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alarm = (Alarm) getArguments().getSerializable(getString(R.string.arg_alarm_obj));
        }
        createAlarmViewModel = ViewModelProviders.of(this).get(BPRecordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateAlarmBinding.inflate(inflater, container, false);
      return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tone = RingtoneManager.getActualDefaultRingtoneUri(this.getContext(), RingtoneManager.TYPE_ALARM).toString();
        ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
        binding.fragmentCreatealarmSetToneName.setText(ringtone.getTitle(getContext()));
        if (alarm != null) {
            updateAlarmInfo(alarm);
        }


        binding.fragmentCreatealarmScheduleAlarm.setOnClickListener(v1 -> {
            if (alarm != null) {
                updateAlarm();
            } else {
                scheduleAlarm();
            }

        });

        binding.fragmentCreatealarmCardSound.setOnClickListener(view1 -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) Uri.parse(tone));
            startActivityForResult(intent, 5);
        });


        binding.fragmentCreatealarmTimePicker.setIs24HourView(false);
        binding.fragmentCreatealarmTimePicker.setOnTimeChangedListener((timePicker, i, i1) -> binding.fragmentCreatealarmScheduleAlarmHeading.setText(DayUtil.getDay(TimePickerUtil.getTimePickerHour(timePicker), TimePickerUtil.getTimePickerMinute(timePicker))));


    }

    private void scheduleAlarm() {
        Boolean isrecrring;
        if (binding.fragmentCreatealarmCheckMon.isChecked() || binding.fragmentCreatealarmCheckThu.isChecked() || binding.fragmentCreatealarmCheckWed.isChecked() || binding.fragmentCreatealarmCheckThu.isChecked() || binding.fragmentCreatealarmCheckFri.isChecked() || binding.fragmentCreatealarmCheckSat.isChecked() || binding.fragmentCreatealarmCheckSun.isChecked()) {
            isrecrring = true;
        } else {
            isrecrring = false;
        }
        String alarmTitle = getString(R.string.alarm_title);
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);
        if (!binding.fragmentCreatealarmTitle.getText().toString().isEmpty()) {
            alarmTitle = binding.fragmentCreatealarmTitle.getText().toString();
        } else {
            alarmTitle = "My Alarm";
        }
        Alarm alarm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarm = new Alarm(
                    alarmId,
                    binding.fragmentCreatealarmTimePicker.getHour(),
                    binding.fragmentCreatealarmTimePicker.getMinute(),

                    alarmTitle,
                    true,
                    isrecrring,
                    binding.fragmentCreatealarmCheckMon.isChecked(),
                    binding.fragmentCreatealarmCheckTue.isChecked(),
                    binding.fragmentCreatealarmCheckWed.isChecked(),
                    binding.fragmentCreatealarmCheckThu.isChecked(),
                    binding.fragmentCreatealarmCheckFri.isChecked(),
                    binding.fragmentCreatealarmCheckSat.isChecked(),
                    binding.fragmentCreatealarmCheckSun.isChecked(),
                    RingtoneManager.getActualDefaultRingtoneUri(requireContext(), RingtoneManager.TYPE_ALARM).toString(),
                    isVibrate
            );
        }

        createAlarmViewModel.insert(alarm);

        alarm.schedule(requireContext());
    }


    private void updateAlarm() {
        Boolean isrecrring;
        if (binding.fragmentCreatealarmCheckMon.isChecked() || binding.fragmentCreatealarmCheckThu.isChecked() || binding.fragmentCreatealarmCheckWed.isChecked() || binding.fragmentCreatealarmCheckThu.isChecked() || binding.fragmentCreatealarmCheckFri.isChecked() || binding.fragmentCreatealarmCheckSat.isChecked() || binding.fragmentCreatealarmCheckSun.isChecked()) {
            isrecrring = true;
        } else {
            isrecrring = false;
        }
        String alarmTitle = getString(R.string.alarm_title);
        if (!binding.fragmentCreatealarmTitle.getText().toString().isEmpty()) {
            alarmTitle = binding.fragmentCreatealarmTitle.getText().toString();
        }
        Alarm updatedAlarm = new Alarm(
                alarm.getAlarmId(),
                TimePickerUtil.getTimePickerHour(binding.fragmentCreatealarmTimePicker),
                TimePickerUtil.getTimePickerMinute(binding.fragmentCreatealarmTimePicker),
                alarmTitle,
                true,
                isrecrring,
                binding.fragmentCreatealarmCheckMon.isChecked(),
                binding.fragmentCreatealarmCheckTue.isChecked(),
                binding.fragmentCreatealarmCheckWed.isChecked(),
                binding.fragmentCreatealarmCheckThu.isChecked(),
                binding.fragmentCreatealarmCheckFri.isChecked(),
                binding.fragmentCreatealarmCheckSat.isChecked(),
                binding.fragmentCreatealarmCheckSun.isChecked(),
                RingtoneManager.getActualDefaultRingtoneUri(requireContext(), RingtoneManager.TYPE_ALARM).toString(),
                isVibrate
        );

        createAlarmViewModel.update(updatedAlarm);
        updatedAlarm.schedule(getContext());

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtone = RingtoneManager.getRingtone(getContext(), uri);
            String title = ringtone.getTitle(getContext());
            if (uri != null) {
                tone = uri.toString();
                if (title != null && !title.isEmpty())
                    binding.fragmentCreatealarmSetToneName.setText(title);

            } else {
                binding.fragmentCreatealarmSetToneName.setText("");
            }
        }
    }

    private void updateAlarmInfo(Alarm alarm) {

        binding.fragmentCreatealarmScheduleAlarmHeading2.setText("Modify");
        binding.fragmentCreatealarmTitle.setText(alarm.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.fragmentCreatealarmTimePicker.setHour(alarm.getHour());

            binding.fragmentCreatealarmTimePicker.setMinute(alarm.getMinute());
        }

        if (alarm.isRecurring()) {
            binding.fragmentCreatealarmRecurringOptions.setVisibility(View.VISIBLE);
            if (alarm.isMonday())
                binding.fragmentCreatealarmCheckMon.setChecked(true);
            if (alarm.isTuesday())
                binding.fragmentCreatealarmCheckTue.setChecked(true);
            if (alarm.isWednesday())
                binding.fragmentCreatealarmCheckWed.setChecked(true);
            if (alarm.isThursday())
                binding.fragmentCreatealarmCheckThu.setChecked(true);
            if (alarm.isFriday())
                binding.fragmentCreatealarmCheckFri.setChecked(true);
            if (alarm.isSaturday())
                binding.fragmentCreatealarmCheckSat.setChecked(true);
            if (alarm.isSunday())
                binding.fragmentCreatealarmCheckSun.setChecked(true);
            if (alarm.getTone()!=null){
            tone = alarm.getTone();
            }else {
                tone=  RingtoneManager.getActualDefaultRingtoneUri(requireContext(), RingtoneManager.TYPE_ALARM).toString();
            }
            ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
            binding.fragmentCreatealarmSetToneName.setText(ringtone.getTitle(getContext()));

        } else {

            tone = alarm.getTone();
            ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(tone));
            binding.fragmentCreatealarmSetToneName.setText(ringtone.getTitle(getContext()));

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}