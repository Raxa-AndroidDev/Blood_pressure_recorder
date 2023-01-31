package com.vboard.bp_recorder_app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ActivityMainBinding
import com.vboard.bp_recorder_app.utils.interfaces.ActivityBackPressCallback
import timber.log.Timber


class MainActivity : AppCompatActivity() {
     var binding: ActivityMainBinding? = null
      var activityBackPressCallback:ActivityBackPressCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding!!.root)

        val navController = Navigation.findNavController(this, R.id.main_nav_graph)
        binding!!.bottomNavView.setupWithNavController(navController)



        navController.addOnDestinationChangedListener { _, destination, _ ->

            Timber.d("destination id id ${destination.id}")
            if(destination.id == R.id.mainFragment || destination.id == R.id.infoFragment || destination.id == R.id.settingsFragment) {

                binding!!.bottomNavView.visibility = View.VISIBLE
            } else {

                binding!!.bottomNavView.visibility = View.GONE
            }
        }





    }

    override fun onBackPressed() {
        //Timber.d("on backpress from activity")
       // if(activityBackPressCallback == null || activityBackPressCallback!!.onBackPressed()){
            super.onBackPressed()
       // }
    }


}