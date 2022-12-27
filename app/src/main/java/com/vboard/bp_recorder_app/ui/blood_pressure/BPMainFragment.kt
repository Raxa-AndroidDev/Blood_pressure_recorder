package com.vboard.bp_recorder_app.ui.blood_pressure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentBPMainBinding

class BPMainFragment : Fragment() {

    lateinit var binding: FragmentBPMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentBPMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {

        binding.buttonAddBp.setOnClickListener {
            findNavController().navigate(R.id.action_BPMainFragment_to_addBPRecordFragment)
        }

        binding.buttonViewBp.setOnClickListener {
            findNavController().navigate(R.id.action_BPMainFragment_to_showBPRecordFragment)
        }
    }

}