package com.vboard.bp_recorder_app.ui.heart_rate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentMainHeartRateBinding

class HeartRateMainFragment : Fragment() {

private lateinit var binding:FragmentMainHeartRateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
binding = FragmentMainHeartRateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonMeasureHr.setOnClickListener {
         startActivity(Intent(requireContext(),HeartRateMonitor::class.java))

        }

        binding.buttonAddHr.setOnClickListener {
            findNavController().navigate(R.id.action_heartRateFragment_to_addHeartRateFragment)
        }
    }


}