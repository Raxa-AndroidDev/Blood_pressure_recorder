package com.vboard.bp_recorder_app.ui.blood_pressure.add_bp_record

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.databinding.FragmentAddBPRecordBinding
import com.vboard.bp_recorder_app.ui.blood_pressure.show_bp_record.BPRecordViewModel
import com.vboard.bp_recorder_app.utils.CurrentDate
import com.vboard.bp_recorder_app.utils.DatePickerDialog
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class AddBPRecordFragment : Fragment() {
    private lateinit var binding: FragmentAddBPRecordBinding

     lateinit var viewModel: BPRecordViewModel
     private var label:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBPRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

        setNumberPickerValues()


        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]


        binding.apply {
            val myCalendar: Calendar = Calendar.getInstance()

            var sDate: String?
            var sTime: String?

            binding.btnDatePick.text = CurrentDate(Calendar.getInstance())
sDate = CurrentDate(myCalendar)




            btnCancel.setOnClickListener {

            }

            val datecurrent = Date()
            val dateFormatcurrent = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
            val pasTime = dateFormatcurrent.format(datecurrent)



            btnDatePick.setOnClickListener {

                sDate =DatePickerDialog(requireContext(),myCalendar)
                btnDatePick.text = sDate
            }


            val currentDateTime = System.currentTimeMillis()
            @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("h:mm a")
            val currentTime = simpleDateFormat.format(currentDateTime)
            btnTimePicker.text = currentTime
            sTime = currentTime

            btnTimePicker.setOnClickListener {
                val mTimePicker: TimePickerDialog
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)

                mTimePicker =
                    TimePickerDialog(requireContext(),
                        { _, hourOfDay, minute ->
                            sTime = getTime(hourOfDay, minute)
                            btnTimePicker.text = getTime(hourOfDay, minute)
                        }, hour, minute, false)
                mTimePicker.show()
            }






            chipGroup.forEach { child ->
                (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                    registerFilterChanged(chipGroup)
                }
            }

            btnOk.setOnClickListener {
              label = "default"
                if (sDate != null && sTime != null && systolicNumberpicker.value != null && diastolicNumberpicker.value != null && pulseNumberpicker.value != null && label!=null) {

                    viewModel.StoreBPRecordInDB(
                        BloodPressureTable(
                            0,
                            sDate!!,
                            sTime!!,
                            pasTime,
                            systolicNumberpicker.value!!,
                            diastolicNumberpicker.value!!,
                            pulseNumberpicker.value!!,
                            label!!
                        )
                    )

                    Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG)
                        .show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill out All fields.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }


        }



    }

    private fun setNumberPickerValues() {
        binding.apply {

            systolicNumberpicker.maxValue = 300
            systolicNumberpicker.minValue = 20
            systolicNumberpicker.value = 90

            diastolicNumberpicker.maxValue = 300
            diastolicNumberpicker.minValue = 20
            diastolicNumberpicker.value = 120

            pulseNumberpicker.maxValue = 200
            pulseNumberpicker.minValue = 20
            pulseNumberpicker.value = 75
        }
    }


    private fun registerFilterChanged(chips_group: ChipGroup) {
        val ids = chips_group.getCheckedChipIds()

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

    private fun getTime(hr: Int, min: Int): String? {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = hr
        cal[Calendar.MINUTE] = min
        val formatter: Format
        formatter = SimpleDateFormat("h:mm a")
        return formatter.format(cal.time)
    }

}