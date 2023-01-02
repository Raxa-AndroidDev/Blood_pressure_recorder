package com.vboard.bp_recorder_app.ui.Alarm;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vboard.bp_recorder_app.data.Alarm;
import com.vboard.bp_recorder_app.databinding.ItemAlarmBinding;
import com.vboard.bp_recorder_app.utils.interfaces.OnToggleAlarmListener;

import java.util.ArrayList;
import java.util.List;


public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private List<Alarm> alarms;
    private OnToggleAlarmListener listener;
    private ItemAlarmBinding itemAlarmBinding;
    public AlarmRecyclerViewAdapter(OnToggleAlarmListener listener) {
        this.alarms = new ArrayList<Alarm>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemAlarmBinding=ItemAlarmBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AlarmViewHolder(itemAlarmBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.bind(alarm, listener);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }
}
