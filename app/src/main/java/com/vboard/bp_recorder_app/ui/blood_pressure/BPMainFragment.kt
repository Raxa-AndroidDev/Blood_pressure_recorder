package com.vboard.bp_recorder_app.ui.blood_pressure

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.databinding.FragmentBPMainBinding
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class BPMainFragment : Fragment() {

    lateinit var binding: FragmentBPMainBinding
    lateinit var viewModel: BPRecordViewModel
    val myCalendar: Calendar = Calendar.getInstance()
    var sDate: String? = null
    var sTime: String? = null
    var sSYS: Int = 0
    var sDIA: Int = 0
    var sPUL: Int = 0
    var label: String = "testable"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentBPMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        binding.buttonAddBp.setOnClickListener {

            insertBPDialogue()
        }

        binding.buttonViewBpTrends.setOnClickListener {
            findNavController().navigate(R.id.action_BPMainFragment_to_BPGraphsFragment)
        }

        binding.buttonViewBp.setOnClickListener {
            findNavController().navigate(R.id.action_BPMainFragment_to_showBPRecordFragment)
        }

        binding.buttonSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_BPMainFragment_to_createAlarmFragment)
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

        val SYSNumberPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.systolic_numberpicker)
        val DIANumberPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.diastolic_numberpicker)
        val PULNumberPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.pulse_numberpicker)

        val btnCancel: AppCompatButton = view.findViewById(R.id.btn_cancel)
        val btnYes: AppCompatButton = view.findViewById(R.id.btn_ok)

        val chipGroup: ChipGroup = view.findViewById(R.id.chip_group)


        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        btnDate.text = dateFormat.format(myCalendar.time)
        sDate = dateFormat.format(myCalendar.time)


        SYSNumberPicker.maxValue = 250
        SYSNumberPicker.minValue = 1
        SYSNumberPicker.value = 115
        sSYS = 115
        tv_instructions.text =
            "Your blood pressure is in good condition. Try to keep it the same way!"
        DIANumberPicker.maxValue = 250
        DIANumberPicker.minValue = 1
        DIANumberPicker.value = 79
        sDIA = 79
        PULNumberPicker.maxValue = 200
        PULNumberPicker.minValue = 1
        PULNumberPicker.value = 75
        sPUL = 75


        btnCancel.setOnClickListener {
            builder.dismiss()
        }

        val datecurrent = Date()
        val dateFormatcurrent = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        val pasTime = dateFormatcurrent.format(datecurrent)

        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            val myFormat = "MM/dd/yy"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            btnDate.text = dateFormat.format(myCalendar.time)
            sDate = dateFormat.format(myCalendar.time)
        }

        btnDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }


        val currentDateTime = System.currentTimeMillis()
        @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("h:mm a")
        val currentTime = simpleDateFormat.format(currentDateTime)
        btnTime.text = currentTime
        sTime = currentTime

        btnTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker =
                TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        sTime = getTime(hourOfDay, minute)
                        btnTime.text = getTime(hourOfDay, minute)
                    }
                }, hour, minute, false)
            mTimePicker.show()
        }


        SYSNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sSYS = newVal
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



        DIANumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sDIA = newVal
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

        PULNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sPUL = newVal
        }

        chipGroup.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                registerFilterChanged(chipGroup)
            }
        }

        btnYes.setOnClickListener {
            if (sDate != null && sTime != null && sSYS != null && sDIA != null && sPUL != null) {

                viewModel.StoreBPRecordInDB(
                    BloodPressureTable(
                        0,
                        sDate!!,
                        sTime!!,
                        pasTime,
                        sSYS!!,
                        sDIA!!,
                        sPUL!!,
                        label!!
                    )
                )
                builder.dismiss()
                Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_BPMainFragment_to_showBPRecordFragment)


            }
            else {
                Toast.makeText(requireContext(), "Please Fill out All fields.", Toast.LENGTH_LONG)
                    .show()
            }
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

        //Toast.makeText(requireContext(), label, Toast.LENGTH_SHORT).show()
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