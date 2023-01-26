package com.vboard.bp_recorder_app.ui.fragments.weight

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightMainBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.fragments.weight.adapters.ViewPagerAdapter
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.util.*

class WeightMainFragment : Fragment() {

lateinit var binding:FragmentWeightMainBinding
    lateinit var viewModel: WeightViewModel

    val tabList = arrayOf("History", "Analysis")


    var label: String = "default"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWeightMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initValues()


    }


    private fun initValues() {
        viewModel = ViewModelProvider(this)[WeightViewModel::class.java]

        handleBottombar()
        tabInitialization()
        initListeners()
    }




    private fun handleBottombar() {
        (activity as MainActivity).binding.bottomNavView.apply {
           Timber.e("bar visibility is ${this.visibility}")
            if ((this.isVisible)) {

                this.visibility = View.GONE
            }

            Timber.e("bar visibility after moving to module is ${this.visibility}")

            this.setOnItemSelectedListener {
                when (it) {
                    0 -> {
                        findNavController().navigate(R.id.action_weightMainFragment_to_mainFragment)
                    }
                    1 -> {
                        findNavController().navigate(R.id.action_weightMainFragment_to_infoFragment)
                    }
                    2 -> {


                    }
                }

            }
        }
    }

    private fun tabInitialization() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

    }

    private fun initListeners() {


        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.tvExport.setOnClickListener {
            DatabaseClass.getDBInstance(requireContext()).bpDao().fetchAllBPRecords()
                .observe(viewLifecycleOwner) {

                    if (it.isEmpty()){
                        Toast.makeText(requireContext(),"No Data to Export", Toast.LENGTH_LONG).show()
                    }else{
                        createCSV()
                    }

                }

        }


    }

    fun createCSV() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "weight_table" + ".csv")
        try {

            if(file.exists()){
                file.delete()
            }
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))


            // Coloumn Names are written
            csvWrite.writeNext(
                arrayOf(
                    "id",
                    "date",
                    "time",
                    "completeDate",
                    "weight",
                    "height",
                    "tag"
                )
            )

            DatabaseClass.getDBInstance(requireContext()).weightDao().fetchAllBodyWeightRecords().observe(viewLifecycleOwner){ bp ->
                bp.forEach { it ->


                    val arrStr = arrayOf("${it.id}",
                        it.date.toString(),
                        it.time,
                        it.fulldate, "${it.weight}", "${it.height}",  it.tag
                    )


                    Timber.e("size is ${arrStr}")
                    csvWrite.writeNext(arrStr)
                }

                csvWrite.close()

            }






            Toast.makeText(requireContext(), "Exported", Toast.LENGTH_SHORT).show()

        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }


    /* private fun insertBPDialogue() {
         val view = LayoutInflater.from(requireContext())
             .inflate(R.layout.fragment_add_bp_record, null)
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

         val systolicNumPicker: com.shawnlin.numberpicker.NumberPicker =
             view.findViewById(R.id.systolic_numberpicker)
         systolicNumPicker.maxValue = 300
         systolicNumPicker.minValue = 20
         systolicNumPicker.value = 115


         val diastolicNumPicker: com.shawnlin.numberpicker.NumberPicker =
             view.findViewById(R.id.diastolic_numberpicker)
         diastolicNumPicker.maxValue = 300
         diastolicNumPicker.minValue = 20
         diastolicNumPicker.value = 79


         val pulseNumPicker: com.shawnlin.numberpicker.NumberPicker =
             view.findViewById(R.id.pulse_numberpicker)
         pulseNumPicker.maxValue = 200
         pulseNumPicker.minValue = 20
         pulseNumPicker.value = 75


         choosenDate = CurrentDate(myCalendar)

         btnDate.text = choosenDate




         tv_instructions.text =
             "Your blood pressure is in good condition. Try to keep it the same way!"


         btnDate.setOnClickListener {
             viewModel.getUpdatedDateFromPicker(requireContext(), myCalendar)
                 .observe(viewLifecycleOwner) {
                     choosenDate = it
                     btnDate.text = it
                 }


         }

         btnTime.setOnClickListener {

             viewModel.getTimeFromPicker(requireContext()).observe(viewLifecycleOwner) {
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
             if (choosenDate != null && choosenTime != null) {

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
                 Log.e("TAG", "insertBPDialogue: diastolic value is " + diastolicNumPicker.value)

                 builder.dismiss()
                 Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()

                 findNavController().navigate(R.id.action_BPMainFragment_to_showBPRecordFragment)


             } else {
                 Toast.makeText(requireContext(), "Please Fill out All fields.", Toast.LENGTH_LONG)
                     .show()
             }
         }

         btnCancel.setOnClickListener {
             builder.dismiss()
         }

     }*/


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