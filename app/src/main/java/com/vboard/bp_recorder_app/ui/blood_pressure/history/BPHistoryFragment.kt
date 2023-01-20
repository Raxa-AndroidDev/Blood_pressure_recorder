package com.vboard.bp_recorder_app.ui.blood_pressure.history

import android.app.Dialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import com.vboard.bp_recorder_app.databinding.FragmentBpHistoryRecordBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.blood_pressure.history.adapter.BPHistoryAdapter
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class BPHistoryFragment : Fragment(), ShowBPAdapterCallbacks {

    private lateinit var binding: FragmentBpHistoryRecordBinding
    private lateinit var showBPRecordViewModel: BPRecordViewModel
    private lateinit var adapter: BPHistoryAdapter

    private var minCalendarInstance = Calendar.getInstance()
    private var maxCalendarInstance = Calendar.getInstance()

    var label: String = "testable"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBpHistoryRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        init()



        binding.imgPreviousDate.setOnClickListener {

            binding.tvStartDate.text = getPreviousDayDate(minCalendarInstance)

            dateWiseSearchView(
                (getDateToStore(minCalendarInstance)),
                (getDateToStore(maxCalendarInstance))
            )

        }

        binding.imgNextDate.setOnClickListener {

            binding.tvEndDate.text = getNextDayDate(maxCalendarInstance)

            dateWiseSearchView(
                (getDateToStore(minCalendarInstance)),
                (getDateToStore(maxCalendarInstance))
            )

        }


        binding.buttonAddBp.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelable("bloodpressuretable", null)
            findNavController().navigate(R.id.action_BPMainFragment_to_addBPRecordFragment, bundle)

        }


        binding.tvViewall.setOnClickListener {

            adapter.type = "detail"
            adapter.notifyDataSetChanged()
            binding.buttonAddBp.visibility = View.GONE
            binding.chartLayout.visibility = View.GONE
            binding.dateWiseSearchLayout.visibility = View.GONE
            binding.tvViewall.visibility = View.GONE


            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.parentLayout)

            constraintSet.connect(
                binding.rcvBloodPressure.id,
                ConstraintSet.TOP,
                binding.parentLayout.id,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(binding.parentLayout)
        }


    }

    private fun init() {

        binding.buttonAddBp.visibility = View.VISIBLE
        binding.chartLayout.visibility = View.VISIBLE
        binding.tvViewall.visibility = View.VISIBLE
        binding.dateWiseSearchLayout.visibility = View.VISIBLE

        showEmptyListDialog()

        handleBottomBar()
        showBPRecordViewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        initRecyclerview()
        binding.tvEndDate.text = getWeekEndDate(Calendar.getInstance())
        binding.tvStartDate.text = getWeekStartDate(Calendar.getInstance())

        minCalendarInstance.time = (getWeekStartDate(Calendar.getInstance())).toDate()
        maxCalendarInstance.time = (getWeekEndDate(Calendar.getInstance())).toDate()



        dateWiseSearchView(getDateToStore(minCalendarInstance), getDateToStore(maxCalendarInstance))



//        binding.btnSearch.setOnClickListener {
//
//            Log.e("testing", "init: min date ${mindate}")
//            Log.e("testing", "init: max date ${maxdate}")
//
//            DatabaseClass.getDBInstance(requireContext()).bpDao()
//                .searchBPRecordByDate(mindate, maxdate).observe(viewLifecycleOwner) {
//
//                    if (it.isNotEmpty()) {
//
//                        it.forEach {
//                            Log.e("testing", "init:  ${it.date}")
//                        }
//
//                        Log.e("testing", "init:  ${it.size}")
//
//                    } else {
//                        Log.e("testing", "init:  empty list")
//                    }
//
//
//                }
//        }
//
//
//
//        binding.searchView.setOnClickListener {
//
//
//            val builder = MaterialDatePicker.Builder.dateRangePicker()
//            builder.setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)
//
//
//            builder.setSelection(
//                androidx.core.util.Pair(
//                    calendarInstance.timeInMillis,
//                    calendarInstance.timeInMillis
//                )
//            )
//
//            val picker = builder.build()
//            picker.show(activity?.supportFragmentManager!!, picker.toString())
//
//            picker.addOnNegativeButtonClickListener { picker.dismiss() }
//            picker.addOnPositiveButtonClickListener {
//                val minimumdate = Date(it.first)
//                val maximumdate = Date(it.second)
//                minCalendarInstance = DateToCalendar(minimumdate)!!
//                maxCalendarInstance = DateToCalendar(maximumdate)!!
//                mindate = CurrentDate(minCalendarInstance)
//                maxdate = CurrentDate(maxCalendarInstance)
//
//
//                Log.e("TAG", "init: first is ${it.first},second is ${it.second}")
//                binding.tvDateSearch.text =
//                    "${DateLongtoString(it.first)} - ${DateLongtoString(it.second)}"
//
//
//            }
//        }

    }

    private fun showEmptyListDialog() {
        DatabaseClass.getDBInstance(requireContext()).bpDao().fetchAllBPRecords()
            .observe(viewLifecycleOwner) {

                if (it.isEmpty()) {

                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.empty_layout)
                    dialog.window!!.setLayout(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                    dialog.findViewById<MaterialButton>(R.id.button_add_now_empty)
                        .setOnClickListener {
                            val bundle = Bundle()
                            bundle.putParcelable("bloodpressuretable", null)
                            findNavController().navigate(
                                R.id.action_BPMainFragment_to_addBPRecordFragment,
                                bundle
                            )
                            dialog.dismiss()
                        }

                    dialog.setCanceledOnTouchOutside(false)

                    dialog.show()

                }


            }
    }

    private fun dateWiseSearchView(startDate: String, endDate: String) {




        showBPRecordViewModel.showDateWiseRecord(
            startDate.toDate(),
            endDate.toDate()
        ).observe(viewLifecycleOwner) {
            adapter.bloodPressureRecordList = ArrayList(it)
            adapter.notifyDataSetChanged()

        }


    }


    private fun initRecyclerview() {
        adapter = BPHistoryAdapter(requireContext(), this)
        binding.rcvBloodPressure.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvBloodPressure.adapter = adapter


        drawGraph()
    }

    private fun handleBottomBar() {
        if (!((activity as MainActivity).binding.bottomNavView.isVisible)) {
            (activity as MainActivity).binding.bottomNavView.visibility = View.VISIBLE
        }
    }


    private fun drawGraph() {
        val chartPointsList = ArrayList<CandleEntry>()


        binding.apply {

            // to enable or disable zooming of the axis
            bpChart.isScaleYEnabled = false
            bpChart.isScaleXEnabled = false

            bpChart.xAxis.setDrawAxisLine(false)
            bpChart.xAxis.setDrawGridLines(false)

            bpChart.setDrawGridBackground(false)

            bpChart.axisLeft.setDrawGridLines(false)
            bpChart.axisLeft.isEnabled = true

            bpChart.axisRight.setDrawGridLines(true)
            bpChart.axisRight.setDrawAxisLine(false)
            bpChart.axisRight.isEnabled = false
            bpChart.legend.isEnabled = true

        }

        showBPRecordViewModel.ShowBPRecordFromDB().observe(viewLifecycleOwner) { bplist ->
            if (bplist.isNotEmpty()) {
                for (i in bplist.indices) {

                    Log.e("TAG", "drawGraph:systolic  ${bplist[i].systolic.toFloat()}")
                    chartPointsList.add(
                        CandleEntry(
                            (i + 1.0).toFloat(),
                            (bplist[i].systolic + 2.0).toFloat(),
                            (bplist[i].diaSystolic - 2.0).toFloat(),
                            bplist[i].systolic.toFloat(),
                            bplist[i].diaSystolic.toFloat()
                        )
                    )

                }


                val set1 = CandleDataSet(chartPointsList, " ")


                set1.neutralColor = resources.getColor(R.color.normal_bp_color)
                set1.decreasingColor = resources.getColor(R.color.normal_bp_color)
                set1.increasingColor = resources.getColor(R.color.normal_bp_color)


                //set1.color = Color.rgb(80, 80, 80)
                set1.shadowWidth = 0f
                set1.formLineWidth = 1f
                set1.barSpace = 3f


                set1.decreasingPaintStyle = Paint.Style.FILL
                set1.increasingPaintStyle = Paint.Style.FILL
                set1.setDrawValues(false)


                val data = CandleData(set1)
                val chartatalist: ArrayList<CandleData> = arrayListOf()
                chartatalist.add(data)
                binding.bpChart.data = data
                binding.bpChart.invalidate()


            }


        }

    }

    override fun OnEditIconClick(bloodPressureTable: BloodPressureTable) {
        val bundle = Bundle()
        bundle.putParcelable("bloodpressuretable", bloodPressureTable)

        findNavController().navigate(R.id.action_BPMainFragment_to_addBPRecordFragment, bundle)
        // updateBPDialogue(bloodPressureTable)
        //showBPRecordViewModel.DeleteSpecificBPRecord((position + 1))
    }

    override fun OnItemClick(bloodPressureTable: BloodPressureTable) {

    }


    /*private fun updateBPDialogue(data: BloodPressureTable) {
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
                TimePickerDialog(
                    requireContext(),
                    { view, hourOfDay, minute ->
                        btnTime.setText(String.format("%d : %d", hourOfDay, minute))
                        sTime = getTime(hourOfDay, minute)
                        btnTime.text = getTime(hourOfDay, minute);
                    }, hour, minute, false
                )
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

    }*/


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