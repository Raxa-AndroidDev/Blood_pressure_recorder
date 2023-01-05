package com.vboard.bp_recorder_app.ui.show_history_record

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.databinding.FragmentShowBPRecordBinding
import com.vboard.bp_recorder_app.ui.blood_pressure.BPRecordViewModel
import com.vboard.bp_recorder_app.ui.show_history_record.adapter.HistoryAdapter
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class ShowHistoryFragment : Fragment(), ShowBPAdapterCallbacks {

    private lateinit var binding: FragmentShowBPRecordBinding
    private lateinit var showBPRecordViewModel: BPRecordViewModel
    private lateinit var adapter: HistoryAdapter


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
    ): View {
        binding = FragmentShowBPRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initializeAdapter()
    }

    private fun init() {
        showBPRecordViewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        adapter = HistoryAdapter(this)
        showBPRecordViewModel.ShowBPRecordFromDB().observe(requireActivity()) {
            adapter.bloodPressureRecordList = ArrayList(it)
        }

        binding.searchView.setOnClickListener {


            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)

            val now = Calendar.getInstance()
            builder.setSelection(androidx.core.util.Pair(now.timeInMillis, now.timeInMillis))

            val picker = builder.build()
            picker.show(activity?.supportFragmentManager!!, picker.toString())

            picker.addOnNegativeButtonClickListener { picker.dismiss() }
            picker.addOnPositiveButtonClickListener {

                Log.e("TAG", "init: first is ${it.first},second is ${it.second}")

                DatabaseClass.getDBInstance(requireContext()).bpDao()
                    .searchBPRecordByDate(it.first, it.second)

            }
        }
    }

    private fun initializeAdapter() {

        binding.rcvBloodPressure.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvBloodPressure.adapter = adapter

    }

    override fun OnDeleteIconClick(position: Int) {
        showBPRecordViewModel.DeleteSpecificBPRecord((position + 1))
    }

    override fun OnItemClick(bloodPressureTable: BloodPressureTable) {
        updateBPDialogue(bloodPressureTable)
    }


    private fun updateBPDialogue(data: BloodPressureTable) {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_add_b_p_record, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .show()
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val tv_operation: TextView = view.findViewById(R.id.tv_Operation_name)
        val btnDate: TextView = view.findViewById(R.id.btn_datePick)
        val btnTime: TextView = view.findViewById(R.id.btn_timePicker)
        val tv_instructions: TextView = view.findViewById(R.id.tv_instructions)

        val sysNumberPicker: com.shawnlin.numberpicker.NumberPicker =
            view.findViewById(R.id.systolic_numberpicker)
        val diaNumberPicker: com.shawnlin.numberpicker.NumberPicker =
            view.findViewById(R.id.diastolic_numberpicker)
        val pulNumberPicker: com.shawnlin.numberpicker.NumberPicker =
            view.findViewById(R.id.pulse_numberpicker)

        val btnCancel: AppCompatButton = view.findViewById(R.id.btn_cancel)
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
        btnTime.text = data.time




        sysNumberPicker.maxValue = 250
        sysNumberPicker.minValue = 1

        if (data.systolic.toInt() in 0..89) {
            tv_instructions.text =
                "Your blood pressure seems a little low.If the situation continues, do not hesitate to consult your doctor."
        }
        if (data.systolic.toInt() in 90..119) {
            tv_instructions.text =
                "Your blood pressure is in good condition. Try to keep it the same way!"
        }
        if (data.systolic.toInt() in 120..129) {
            tv_instructions.text =
                "Your blood pressure is a little above normal.We recommend that you adopt a healthier lifestyle and measure your blood pressure more frequently."
        }
        if (data.systolic.toInt() in 130..139) {
            tv_instructions.text =
                "If you have 3 or more measurements in this area, it is time to consult your doctor to improve your lifestyle."
        }
        if (data.systolic.toInt() in 140..179) {
            tv_instructions.text =
                "If you have 3 or more measurements in this area, you should take care of yourself with medical treatment and lifestyle changes."
        }
        if (data.systolic.toInt() in 180..250) {
            tv_instructions.text =
                "We're worried about you.Call an emergency medical service immediately!"

        }



        diaNumberPicker.maxValue = 250
        diaNumberPicker.minValue = 1
        pulNumberPicker.maxValue = 200
        pulNumberPicker.minValue = 1

        sysNumberPicker.value = data.systolic.toInt()
        diaNumberPicker.value = data.diaSystolic.toInt()
        pulNumberPicker.value = data.pulse.toInt()




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
                TimePickerDialog(requireContext(),
                    { view, hourOfDay, minute ->
                        btnTime.setText(String.format("%d : %d", hourOfDay, minute))
                        sTime = getTime(hourOfDay, minute)
                        btnTime.text = getTime(hourOfDay, minute);
                    }, hour, minute, false)
            mTimePicker.show()
        }


        sysNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
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

        diaNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
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
        pulNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sPUL = newVal
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
            if (sDate != null && sTime != null && sSYS != null && sDIA != null && sPUL != null && label != null) {
                showBPRecordViewModel.UpdateBPRecord(
                    BloodPressureTable(
                        data.id,
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
                Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_LONG).show()
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