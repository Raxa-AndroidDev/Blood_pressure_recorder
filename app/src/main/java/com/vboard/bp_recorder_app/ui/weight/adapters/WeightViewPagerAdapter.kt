package com.vboard.bp_recorder_app.ui.weight.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vboard.bp_recorder_app.ui.blood_pressure.graphs.BPGraphsFragment
import com.vboard.bp_recorder_app.ui.weight.WeightHistoryFragment

class WeightViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int  = 2



    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return BPGraphsFragment()

        }
        return WeightHistoryFragment()
    }
}