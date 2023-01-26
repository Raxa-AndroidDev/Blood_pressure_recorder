package com.vboard.bp_recorder_app.ui.fragments.heart_rate.history.adapter

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.button.MaterialButton
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import com.vboard.bp_recorder_app.data.viewModels.HRViewModel
import com.vboard.bp_recorder_app.databinding.FragmentHrHistoryBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ShowHRAdapterCallbacks
import timber.log.Timber
import java.util.*


class HRHistoryFragment : Fragment(),ShowHRAdapterCallbacks {

    private lateinit var binding: FragmentHrHistoryBinding
    private lateinit var viewModel: HRViewModel
    private lateinit var adapter: HRHistoryAdapter


    private var minCalendarInstance = Calendar.getInstance()
    private var maxCalendarInstance = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHrHistoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }


    private fun init() {



        binding.buttonAddHr.visibility = View.VISIBLE
        binding.chartLayout.visibility = View.VISIBLE
        binding.tvViewall.visibility = View.VISIBLE
        binding.dateWiseSearchLayout.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this)[HRViewModel::class.java]

        showEmptyListDialog()

        handleBottomBar()
        clickListeners()


        initRecyclerview()
        binding.tvEndDate.text = getWeekEndDate(Calendar.getInstance())
        binding.tvStartDate.text = getWeekStartDate(Calendar.getInstance())

        minCalendarInstance.time = getWeekStartDate(Calendar.getInstance()).toDate()
        maxCalendarInstance.time = getWeekEndDate(Calendar.getInstance()).toDate()

        dateWiseSearchView(getDateToStore(minCalendarInstance), getDateToStore(maxCalendarInstance))


    }

    private fun clickListeners() {
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


        binding.buttonAddHr.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelable("hrtable", null)
           

        }


        binding.tvViewall.setOnClickListener {

            adapter.type = "detail"
            adapter.notifyDataSetChanged()
            binding.buttonAddHr.visibility = View.GONE
            binding.chartLayout.visibility = View.GONE
            binding.dateWiseSearchLayout.visibility = View.GONE
            binding.tvViewall.visibility = View.GONE


            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.parentLayout)

            constraintSet.connect(
                binding.rcvHr.id,
                ConstraintSet.TOP,
                binding.parentLayout.id,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(binding.parentLayout)
        }
    }

    private fun showEmptyListDialog() {
        DatabaseClass.getDBInstance(requireContext()).hrDao().fetchAllHeartRateRecords()
            .observe(viewLifecycleOwner) {
                Log.e("TAG", "showEmptyListDialog: ${it.size}")
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
                            bundle.putParcelable("hrtable", null)

                            dialog.dismiss()
                        }

                    dialog.setCanceledOnTouchOutside(false)

                    dialog.show()

                }


            }
    }

    private fun dateWiseSearchView(startDate: String, endDate: String) {
        Timber.e("start date is ${startDate} , end date is ${endDate}")

        viewModel.showDateWiseRecord(
            startDate.toDate(),
            endDate.toDate()
        ).observe(viewLifecycleOwner) {
            adapter.heartrateList = ArrayList(it)
            adapter.notifyDataSetChanged()

        }


    }


    private fun initRecyclerview() {
        adapter = HRHistoryAdapter(requireContext(), this)
        binding.rcvHr.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvHr.adapter = adapter


        drawGraph()
    }

    private fun handleBottomBar() {
        if (!((activity as MainActivity).binding.bottomNavView.isVisible)) {
            (activity as MainActivity).binding.bottomNavView.visibility = View.VISIBLE
        }
    }


    private fun drawGraph() {


        binding.apply {

            hrChart.legend.isEnabled = true
            hrChart.setDrawGridBackground(false)

            // to enable or disable zooming of the axis
            hrChart.isScaleYEnabled = false
            hrChart.isScaleXEnabled = false


            hrChart.xAxis.setDrawAxisLine(false)
            hrChart.xAxis.setDrawGridLines(false)

            hrChart.axisRight.setDrawGridLines(true)
            hrChart.axisRight.setDrawAxisLine(false)
            hrChart.axisRight.isEnabled = false

            val left: YAxis = hrChart.axisLeft
            left.isEnabled = true
            left.setDrawGridLines(true)
            left.setDrawLabels(false)
            left.setDrawAxisLine(true)


            val XAxisLabels = arrayListOf<String>("1", "2", "3", "4", "5")
            hrChart.xAxis.valueFormatter = IndexAxisValueFormatter(XAxisLabels)


        }
        val chartPointsList = ArrayList<BarEntry>()

        viewModel.showHRRecordFromDB().observe(viewLifecycleOwner) { hrList ->
            if (hrList.isNotEmpty()) {
                for (i in hrList.indices) {

                    Log.e("TAG", "drawGraph:systolic  ${hrList[i].BPM.toFloat()}")
                    chartPointsList.add(
                        BarEntry(
                            (i + 1.0).toFloat(),
                            hrList[i].BPM.toFloat(),
                        )
                    )

                }


                val set1 = BarDataSet(chartPointsList, " ")

                //set1.color = Color.rgb(80, 80, 80)
                set1.formLineWidth = 1f
                set1.setDrawValues(false)


                /* val data = BarData(set1)
                 val chartatalist: ArrayList<BarData> = arrayListOf()
                 chartatalist.add(data)
                 binding.hrChart.data = data
                 binding.hrChart.invalidate()*/


                ///////////////////


                binding.hrChart.setDrawBarShadow(false)
                binding.hrChart.description.isEnabled = false
                binding.hrChart.setDrawGridBackground(false)
                binding.hrChart.setDrawValueAboveBar(true)
                binding.hrChart.animateY(1000)
                binding.hrChart.axisLeft.textColor = Color.parseColor("#000000")
                binding.hrChart.xAxis.spaceMin = 1f
                binding.hrChart.setFitBars(true)
                binding.hrChart.setTouchEnabled(true)
                binding.hrChart.setPinchZoom(false)
                binding.hrChart.isDoubleTapToZoomEnabled = false

                binding.hrChart.invalidate()


                val nameLimitLine = LimitLine(binding.hrChart.axisLeft.mAxisRange, "").apply {
                    enableDashedLine(10f, 15f, 0f)
                    lineWidth = 2f
                    lineColor = Color.GREEN
                }
                binding.hrChart.axisLeft.addLimitLine(nameLimitLine)
                /*   addCustomLayoutOnLimitLine(3f)
                   colors.add(Color.BLACK)
                   colors.add(Color.BLACK)
                   colors.add(Color.BLACK)
                   colors.add(Color.RED)
                   colors.add(Color.BLACK)
                   colors.add(Color.BLACK)*/


                val xl: XAxis = binding.hrChart.xAxis
                xl.position = XAxis.XAxisPosition.BOTTOM
                xl.setDrawAxisLine(false)
                xl.setDrawGridLines(false)
                xl.yOffset = 5f
                xl.textColor = Color.parseColor("#000000")

                val yl: YAxis = binding.hrChart.axisRight
                yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
                yl.setDrawLimitLinesBehindData(true)
                yl.setDrawGridLines(true)
                yl.setDrawAxisLine(true)
                yl.isEnabled = false

                binding.hrChart.axisLeft.setAxisMinValue(0f)
                binding.hrChart.axisLeft.setAxisMaxValue(300f)
                /// set maximum value when there is no step data (user just install app)


                /// setting xAxis bottom label according to filter (day - week - month)
                /*when (type) {
                    0 -> {
                        xl.valueFormatter = IndexAxisValueFormatter(getChartDailyLabels())
                    }
                    1 -> {
                        xl.valueFormatter = IndexAxisValueFormatter(getChartWeeklyLabels())
                    }
                    2 -> {
                        xl.valueFormatter = IndexAxisValueFormatter(getChartMonthlyLabels())
                    }
                }*/

                binding.hrChart.axisLeft.labelCount = 7
                binding.hrChart.axisLeft.setStartAtZero(true)
                binding.hrChart.axisLeft.setDrawAxisLine(true)
                binding.hrChart.axisLeft.setDrawGridLines(true)


                set1.axisDependency = YAxis.AxisDependency.LEFT


                val data = BarData(set1)
                val chartatalist: ArrayList<BarData> = arrayListOf()
                chartatalist.add(data)
                binding.hrChart.data = data
                binding.hrChart.invalidate()



                binding.hrChart.data = data
                binding.hrChart.legend.isEnabled = false


                /////////////////////////////////////


            }


        }

    }


    override fun OnEditIconClick(heartRateTable: HeartRateTable) {
        val bundle = Bundle()
        bundle.putParcelable("weighttable", heartRateTable)

        findNavController().navigate(
            R.id.action_weightMainFragment_to_addWeightRecordFragment,
            bundle
        )

    }

    override fun OnItemClick(heartRateTable: HeartRateTable) {

    }


}