package com.vboard.bp_recorder_app.ui.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightMainBinding
import com.vboard.bp_recorder_app.ui.weight.adapters.WeightViewPagerAdapter

import java.util.*

class WeightMainFragment : Fragment() {

    lateinit var binding: FragmentWeightMainBinding
    lateinit var viewModel: WeightViewModel

    val tabList = arrayOf("History", "Analysis")
    val myCalendar: Calendar = Calendar.getInstance()
    var choosenDate: String? = null
    var choosenTime: String? = null

    var label: String = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentWeightMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabInitialization()
        initListeners()

    }

    private fun tabInitialization() {
        val viewPagerAdapter = WeightViewPagerAdapter(childFragmentManager,lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){
            tab,position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun initListeners() {
        viewModel = ViewModelProvider(this)[WeightViewModel::class.java]

        binding.buttonAddBp.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelable("weighttable", null)
            findNavController().navigate(R.id.action_weightMainFragment_to_addWeightRecordFragment, bundle)


            //insertBPDialogue()
        }


    }






    private fun registerFilterChanged(chips_group: ChipGroup) {
        val ids = chips_group.checkedChipIds

        val titles = mutableListOf<CharSequence>()

        ids.forEach { id ->
            titles.add(chips_group.findViewById<Chip>(id).text)
        }

        label = if (titles.isNotEmpty()) {
            titles.joinToString(", ")
        } else {
            ""
        }

    }








}