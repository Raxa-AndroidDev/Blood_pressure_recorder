package com.vboard.bp_recorder_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        val navController = findNavController()

        binding.bottomNavView.setupWithNavController(navController)


    }

    private fun initListeners() {
        binding.constBloodpressureRed.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)
        }

        binding.constHeartrateblue.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_heartRateFragment)
        }




    }
}