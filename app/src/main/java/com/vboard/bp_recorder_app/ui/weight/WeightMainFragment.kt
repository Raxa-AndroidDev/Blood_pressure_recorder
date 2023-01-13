package com.vboard.bp_recorder_app.ui.weight

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.ui.blood_pressure.add_bp_record.adapters.ViewPagerAdapter
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import com.vboard.bp_recorder_app.databinding.FragmentBPMainBinding
import com.vboard.bp_recorder_app.databinding.FragmentWeightMainBinding
import com.vboard.bp_recorder_app.utils.CurrentDate
import com.vboard.bp_recorder_app.utils.dateAndTime
import java.util.*

class WeightMainFragment : Fragment() {

    lateinit var binding: FragmentWeightMainBinding
    lateinit var viewModel: BPRecordViewModel

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
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){
            tab,position ->
            tab.text = tabList[position]
        }.attach()
    }

    private fun initListeners() {
        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        binding.buttonAddBp.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelable("bloodpressuretable", null)
            findNavController().navigate(R.id.action_BPMainFragment_to_addBPRecordFragment, bundle)


            //insertBPDialogue()
        }


    }



    private fun insertBPDialogue() {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_add_b_p_record, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .show()
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnDate: TextView = view.findViewById(R.id.btn_datePick)
        val btnTime: TextView = view.findViewById(R.id.btn_timePicker)
        val tv_instructions: TextView = view.findViewById(R.id.tv_instructions)
        val btnCancel: AppCompatButton = view.findViewById(R.id.btn_cancel)
        val btnYes: AppCompatButton = view.findViewById(R.id.btn_ok)
        val chipGroup: ChipGroup = view.findViewById(R.id.chip_group)

        val systolicNumPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.systolic_numberpicker)
        systolicNumPicker.maxValue = 300
        systolicNumPicker.minValue = 20
        systolicNumPicker.value = 115


        val diastolicNumPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.diastolic_numberpicker)
        diastolicNumPicker.maxValue = 300
        diastolicNumPicker.minValue = 20
        diastolicNumPicker.value = 79


        val pulseNumPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.pulse_numberpicker)
        pulseNumPicker.maxValue = 200
        pulseNumPicker.minValue = 20
        pulseNumPicker.value = 75


        choosenDate = CurrentDate(myCalendar)

        btnDate.text = choosenDate




        tv_instructions.text =
            "Your blood pressure is in good condition. Try to keep it the same way!"


        btnDate.setOnClickListener {
            viewModel.getUpdatedDateFromPicker(requireContext(),myCalendar).observe(viewLifecycleOwner){
                choosenDate = it
                btnDate.text = it
            }


        }

        btnTime.setOnClickListener {

           viewModel.getTimeFromPicker(requireContext()).observe(viewLifecycleOwner){
               choosenTime = it
               btnTime.text = it

           }

        }

        systolicNumPicker.setOnValueChangedListener { picker, oldVal, newVal ->
systolicNumPicker.value = newVal
            if (newVal in 0..89) {
                tv_instructions.text =
                    "Your blood pressure seems a little low.If the situation continues, do not hesitate to consult your doctor."
            }
            if (newVal in 90..119) {
                tv_instructions.text =
                    "Your blood pressure is in good condition. Try to keep it the same way!"
            }
            if (newVal in 120..129) {
                tv_instructions.text =
                    "Your blood pressure is a little above normal.We recommend that you adopt a healthier lifestyle and measure your blood pressure more frequently."
            }
            if (newVal in 130..139) {
                tv_instructions.text =
                    "If you have 3 or more measurements in this area, it is time to consult your doctor to improve your lifestyle."
            }
            if (newVal in 140..179) {
                tv_instructions.text =
                    "If you have 3 or more measurements in this area, you should take care of yourself with medical treatment and lifestyle changes."
            }
            if (newVal in 180..250) {
                tv_instructions.text =
                    "We're worried about you.Call an emergency medical service immediately!"

            }
        }

        diastolicNumPicker.setOnValueChangedListener { picker, oldVal, newVal ->

            if (newVal in 0..59) {
                tv_instructions.text =
                    "Your blood pressure seems a little low.If the situation continues, do not hesitate to consult your doctor."
            }
            if (newVal in 60..79) {
                tv_instructions.text =
                    "Your blood pressure is in good condition. Try to keep it the same way!"
            }
            if (newVal in 80..89) {
                tv_instructions.text =
                    "If you have 3 or more measurements in this area, it is time to consult your doctor to improve your lifestyle."
            }
            if (newVal in 90..120) {
                tv_instructions.text =
                    "If you have 3 or more measurements in this area, you should take care of yourself with medical treatment and lifestyle changes."
            }
            if (newVal in 121..250) {
                tv_instructions.text =
                    "We're worried about you.Call an emergency medical service immediately!"

            }

        }





        chipGroup.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                registerFilterChanged(chipGroup)
            }
        }

        btnYes.setOnClickListener {
            if (choosenDate != null && choosenTime != null ) {

                viewModel.StoreBPRecordInDB(
                    BloodPressureTable(
                        0,
                        choosenDate!!,
                        choosenTime!!,
                        dateAndTime(),
                        systolicNumPicker.value,
                        diastolicNumPicker.value,
                        pulseNumPicker.value,
                        label
                    )
                )
                Log.e("TAG", "insertBPDialogue: diastolic value is "+diastolicNumPicker.value)

                builder.dismiss()
                Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_BPMainFragment_to_showBPRecordFragment)


            }
            else {
                Toast.makeText(requireContext(), "Please Fill out All fields.", Toast.LENGTH_LONG)
                    .show()
            }
        }

        btnCancel.setOnClickListener {
            builder.dismiss()
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