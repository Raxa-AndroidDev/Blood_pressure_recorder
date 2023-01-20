package com.vboard.bp_recorder_app.ui.blood_pressure.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vboard.bp_recorder_app.ui.weight.graphs.WeightGraphsFragment
import com.vboard.bp_recorder_app.ui.blood_pressure.history.BPHistoryFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int  = 2



    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return WeightGraphsFragment()

        }
        return BPHistoryFragment()
    }
}