package com.example.bloodpressureapp.fragments.heartrate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import com.vboard.bp_recorder_app.databinding.FragmentAddHeartRateBinding
import com.vboard.bp_recorder_app.data.viewModels.HRViewModel
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class AddHeartRateFragment : Fragment(), HeartAdapter.ItemClick {

    lateinit var binding: FragmentAddHeartRateBinding
    val myCalendar: Calendar = Calendar.getInstance()
    lateinit var  viewModel: HRViewModel
    var sDate: String? = null
    var sTime: String? = null

    var sBpm: String? = null
    var label: String = "testable"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHeartRateBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HRViewModel::class.java]


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }



        binding.addNew.setOnClickListener {
            insertDialogue()
        }


        val adapter = HeartAdapter(this)
        val recyclerView = binding.recViewHeart
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.readAllHeartRateData.observe(
            viewLifecycleOwner
        ) { user ->
            if (user.isNotEmpty()) {

                adapter.setData(user)
                binding.addNew.visibility = View.VISIBLE
            } else {
                binding.addNew.visibility = View.GONE
            }

        }

    }




    override fun onClick(item: HeartRateTable) {
        updateDialogue(item)
    }

    private fun updateDialogue(data: HeartRateTable) {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.heart_rate_dialoge, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .show()
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val tv_operation: TextView = view.findViewById(R.id.tv_Operation_name)
        val btnDate: TextView = view.findViewById(R.id.btn_datePick)
        val btnTime: TextView = view.findViewById(R.id.btn_timePicker)
        val tv_instructions: TextView = view.findViewById(R.id.tv_instructions)


        val bpmNumberPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.picker_bpm)

        val btnCancel: AppCompatButton = view.findViewById(R.id.btnno)
        val btnYes: AppCompatButton = view.findViewById(R.id.btn_ok)

        val chipGroup: ChipGroup = view.findViewById(R.id.chip_group)


        val strs = data.label.split(",").toTypedArray()

        for (i in strs.indices) {

        }


        for (a in strs.indices) {
            var chip: Chip
            var text = strs[a].toLowerCase()

            for (i in 0 until chipGroup.childCount) {

                chip = chipGroup.getChildAt(i) as Chip
                val chiptext = chip.text.toString().toLowerCase()
                if (chiptext == text) {

                    chip.isChecked = true
                }
            }

        }



        tv_operation.text = "Modify"
        btnYes.text = "Update"


        btnDate.text = data.date
        sDate = data.date
        btnTime.text = data.time
        sTime = data.time




        bpmNumberPicker.maxValue = 200
        bpmNumberPicker.minValue = 0
        bpmNumberPicker.value = data.BPM.toInt()
        sBpm = data.BPM

        bpmNumberPicker.value = data.BPM.toInt()




        btnCancel.setOnClickListener {
            builder.dismiss()
        }


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



        btnTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker =
                TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        btnTime.setText(String.format("%d : %d", hourOfDay, minute))
                        sTime = getTime(hourOfDay, minute)
                        btnTime.text = getTime(hourOfDay, minute);
                    }
                }, hour, minute, false)
            mTimePicker.show()
        }



        bpmNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sBpm = newVal.toString()
        }

        chipGroup.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                registerFilterChanged(chipGroup)
            }
        }
        val datecurrent = Date()
        val dateFormatcurrent = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        val pasTime = dateFormatcurrent.format(datecurrent)
        btnYes.setOnClickListener {
            if (sDate != null && sTime != null && sBpm != null && label != null) {
                viewModel.updateHeartRate(
                    HeartRateTable(
                        data.id,
                        sDate!!,
                        sTime!!,
                        pasTime,

                        sBpm!!,
                        label!!
                    )
                );
                builder.dismiss()
                Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Please Fill out All fields.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun insertDialogue() {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.heart_rate_dialoge, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .show()
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnDate: TextView = view.findViewById(R.id.btn_datePick)
        val btnTime: TextView = view.findViewById(R.id.btn_timePicker)
        val tv_instructions: TextView = view.findViewById(R.id.tv_instructions)

        val bpmNumberPicker: com.shawnlin.numberpicker.NumberPicker = view.findViewById(R.id.picker_bpm)

        val btnCancel: AppCompatButton = view.findViewById(R.id.btnno)
        val btnYes: AppCompatButton = view.findViewById(R.id.btn_ok)

        val chipGroup: ChipGroup = view.findViewById(R.id.chip_group)


        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        btnDate.text = dateFormat.format(myCalendar.time)
        sDate = dateFormat.format(myCalendar.time)




        bpmNumberPicker.maxValue = 200
        bpmNumberPicker.minValue = 1
        bpmNumberPicker.value = 75
        sBpm = 75.toString()


        btnCancel.setOnClickListener {
            builder.dismiss()
        }


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



        bpmNumberPicker.setOnValueChangedListener(com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
            sBpm = newVal.toString()
        })

        chipGroup.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                registerFilterChanged(chipGroup)
            }
        }
        val datecurrent = Date()
        val dateFormatcurrent = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        val pasTime = dateFormatcurrent.format(datecurrent)

        btnYes.setOnClickListener {
            if (sDate != null && sTime != null  && sBpm != null) {

                    viewModel.addHeartRate(
                        HeartRateTable(
                            0,
                            sDate!!,
                            sTime!!,
                            pasTime,

                            sBpm!!,
                            label
                        )
                    )
                    builder.dismiss()
                    Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()


            } else {
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