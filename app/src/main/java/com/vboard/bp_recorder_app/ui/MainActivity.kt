package com.vboard.bp_recorder_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.main_nav_graph)
        binding.bottomNavView.setupWithNavController(navController)




        // will be dealt on monday
        /* binding.bottomNavView.setOnItemSelectedListener {
             Timber.e("ids are ${it}")
             when(it){
                0 ->{
                    findNavController().navigate(Uri.parse("yourapp://BloodPressure/app/main/frag"  ))
                 }
                1->{
                    findNavController().navigate(Uri.parse(  "BloodPressure_app_info_frag"  ))
                 }
                 2 ->{
                     findNavController().navigate(Uri.parse(  "yourapp://BloodPressure/app/bp/frag"))
                 }
             }
         }*/


    }


}