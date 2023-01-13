package com.vboard.bp_recorder_app.ui.Alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vboard.bp_recorder_app.R;
import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.databinding.FragmentAlarmsListBinding;
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel;
import com.vboard.bp_recorder_app.utils.interfaces.OnToggleAlarmListener;

import java.util.List;


public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener {
    public static AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private BPRecordViewModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    private FragmentAlarmsListBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel = ViewModelProviders.of(this).get(BPRecordViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (!alarms.isEmpty()) {
                    binding.fragmentListalarmsRecylerView.setVisibility(View.VISIBLE);
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                    binding.fragmentListalarmsMyAlarms.setText("Reminder History");
                   /* binding.animationView.pauseAnimation();
                    binding.animationView.setVisibility(View.GONE);*/

                } else {
                    binding.fragmentListalarmsRecylerView.setVisibility(View.GONE);

                    binding.fragmentListalarmsMyAlarms.setText("Add Reminder");


                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAlarmsListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        alarmsRecyclerView = binding.fragmentListalarmsRecylerView;
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);



       /* Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,-2);
        binding.clock.setCalendar(calendar)
                .setDiameterInDp(400.0f)
                .setOpacity(1.0f)
                .setShowSeconds(true)
                .setColor(Color.BLACK);*/

        binding.fragmentListalarmsAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
            }
        });
        return view;
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
        } else {
            alarm.schedule(getContext());

        }
        alarmsListViewModel.update(alarm);
    }

    @Override
    public void onDelete(Alarm alarm) {
        if (alarm.isStarted())
            alarm.cancelAlarm(getContext());
        alarmsListViewModel.delete(alarm.getAlarmId());
    }

    @Override
    public void onItemClick(Alarm alarm, View view) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
        }
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.arg_alarm_obj), alarm);
       // Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment, args);

    }
}