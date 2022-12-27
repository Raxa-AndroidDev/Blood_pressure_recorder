package com.vboard.bp_recorder_app.ui.blood_pressure.show_bp_record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vboard.bp_recorder_app.databinding.FragmentShowBPRecordBinding
import com.vboard.bp_recorder_app.ui.blood_pressure.show_bp_record.adapter.ShowBloodPressureAdapter
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks


class ShowBPRecordFragment : Fragment(),ShowBPAdapterCallbacks {

    private lateinit var binding:FragmentShowBPRecordBinding
    private lateinit var  showBPRecordViewModel: BPRecordViewModel
    private lateinit var adapter :ShowBloodPressureAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowBPRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initializeAdapter()
    }

    private fun init() {
        showBPRecordViewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        adapter = ShowBloodPressureAdapter(this)
        showBPRecordViewModel.ShowBPRecordFromDB().observe(requireActivity()){
         adapter.bloodPressureRecordList = ArrayList(it)
        }
    }

    private fun initializeAdapter() {

        binding.rcvBloodPressure.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvBloodPressure.adapter = adapter

    }

    override fun OnDeleteIconClick(position: Int) {

    }

    override fun OnItemClick(position: Int) {

    }


}