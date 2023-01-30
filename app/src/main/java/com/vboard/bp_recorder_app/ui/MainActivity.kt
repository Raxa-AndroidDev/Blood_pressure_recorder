package com.vboard.bp_recorder_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        //binding = ActivityMainBinding.inflate(layoutInflater, R.layout.activity_main)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.main_nav_graph)
        binding.bottomNavView.setupWithNavController(navController)

      /*  binding.bottomNavView.apply {
            this.setOnItemSelectedListener {
                when (it) {
                    0 -> {

                    }
                    1 -> {
                        findNavController().navigate(R.id.action_mainFragment_to_infoFragment)
                    }
                    2 -> {
                        findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)


                    }
                }

            }
        }*/

    }
}