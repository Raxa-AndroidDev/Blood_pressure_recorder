package com.vboard.bp_recorder_app.ui.fragments.weight.history

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
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightHistoryRecordBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.fragments.weight.history.adapter.WeightHistoryAdapter
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ShowWeightAdapterCallbacks
import timber.log.Timber
import java.util.*


class WeightHistoryFragment : Fragment() ,ShowWeightAdapterCallbacks {

    private lateinit var binding: FragmentWeightHistoryRecordBinding
    private lateinit var viewModel: WeightViewModel
    private lateinit var adapter: WeightHistoryAdapter


    private var minCalendarInstance = Calendar.getInstance()
    private var maxCalendarInstance = Calendar.getInstance()

    var label: String = "testable"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeightHistoryRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()




    }

   private fun init() {


        binding.buttonAddWeight.visibility = View.VISIBLE
        binding.chartLayout.visibility = View.VISIBLE
        binding.tvViewall.visibility = View.VISIBLE
        binding.dateWiseSearchLayout.visibility = View.VISIBLE
       viewModel = ViewModelProvider(this)[WeightViewModel::class.java]

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


        binding.buttonAddWeight.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelable("weighttable", null)
            findNavController().navigate(R.id.action_weightMainFragment_to_addWeightRecordFragment, bundle)

        }


        binding.tvViewall.setOnClickListener {

            adapter.type = "detail"
            adapter.notifyDataSetChanged()
            binding.buttonAddWeight.visibility = View.GONE
            binding.chartLayout.visibility = View.GONE
            binding.dateWiseSearchLayout.visibility = View.GONE
            binding.tvViewall.visibility = View.GONE


            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.parentLayout)

            constraintSet.connect(
                binding.rcvWeight.id,
                ConstraintSet.TOP,
                binding.parentLayout.id,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(binding.parentLayout)
        }
    }

    private fun showEmptyListDialog() {
        DatabaseClass.getDBInstance(requireContext()).weightDao().fetchAllBodyWeightRecords()
            .observe(viewLifecycleOwner) {
                Log.e("TAG", "showEmptyListDialog: ${it.size}", )
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
                            bundle.putParcelable("weighttable", null)
                            findNavController().navigate(
                                R.id.action_weightMainFragment_to_addWeightRecordFragment,
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
        Timber.e("start date is ${startDate} , end date is ${endDate}")

        viewModel.showDateWiseRecord(
            startDate.toDate(),
            endDate.toDate()
        ).observe(viewLifecycleOwner) {
            adapter.weightRecordList = ArrayList(it)
            adapter.notifyDataSetChanged()

        }


    }


    private fun initRecyclerview() {
        adapter = WeightHistoryAdapter(requireContext(), this)
        binding.rcvWeight.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvWeight.adapter = adapter


        drawGraph()
    }

    private fun handleBottomBar() {
        if (!((activity as MainActivity).binding.bottomNavView.isVisible)) {
            (activity as MainActivity).binding.bottomNavView.visibility = View.VISIBLE
        }
    }


    private fun drawGraph() {


        binding.apply {

            weightChart.legend.isEnabled = true
            weightChart.setDrawGridBackground(false)

            // to enable or disable zooming of the axis
            weightChart.isScaleYEnabled = false
            weightChart.isScaleXEnabled = false


            weightChart.xAxis.setDrawAxisLine(false)
            weightChart.xAxis.setDrawGridLines(false)

            weightChart.axisRight.setDrawGridLines(true)
            weightChart.axisRight.setDrawAxisLine(false)
            weightChart.axisRight.isEnabled = false

            val left: YAxis = weightChart.axisLeft
            left.isEnabled = true
            left.setDrawGridLines(true)
            left.setDrawLabels(false)
            left.setDrawAxisLine(true)


val XAxisLabels = arrayListOf<String>("1","2","3","4","5")
            weightChart.xAxis.valueFormatter = IndexAxisValueFormatter(XAxisLabels)


        }
        val chartPointsList = ArrayList<BarEntry>()

        viewModel.showWeightRecordFromDB().observe(viewLifecycleOwner) { bplist ->
            if (bplist.isNotEmpty()) {
                for (i in bplist.indices) {

                    Log.e("TAG", "drawGraph:systolic  ${bplist[i].weight.toFloat()}")
                    chartPointsList.add(
                        BarEntry(
                            (i + 1.0).toFloat(),
                            bplist[i].weight.toFloat(),
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
                binding.weightChart.data = data
                binding.weightChart.invalidate()*/


               ///////////////////


                binding.weightChart.setDrawBarShadow(false)
                binding.weightChart.description.isEnabled = false
                binding.weightChart.setDrawGridBackground(false)
                binding.weightChart.setDrawValueAboveBar(true)
                binding.weightChart.animateY(1000)
                binding.weightChart.axisLeft.textColor = Color.parseColor("#000000")
                binding.weightChart.xAxis.spaceMin = 1f
                binding.weightChart.setFitBars(true)
                binding.weightChart.setTouchEnabled(true)
                binding.weightChart.setPinchZoom(false)
                binding.weightChart.isDoubleTapToZoomEnabled = false

                binding.weightChart.invalidate()



                val nameLimitLine = LimitLine(binding.weightChart.axisLeft.mAxisRange, "").apply{
                    enableDashedLine(10f, 15f, 0f)
                    lineWidth = 2f
                    lineColor=Color.GREEN
                }
                binding.weightChart.axisLeft.addLimitLine(nameLimitLine)
                /*   addCustomLayoutOnLimitLine(3f)
                   colors.add(Color.BLACK)
                   colors.add(Color.BLACK)
                   colors.add(Color.BLACK)
                   colors.add(Color.RED)
                   colors.add(Color.BLACK)
                   colors.add(Color.BLACK)*/





                val xl: XAxis = binding.weightChart.xAxis
                xl.position = XAxis.XAxisPosition.BOTTOM
                xl.setDrawAxisLine(false)
                xl.setDrawGridLines(false)
                xl.yOffset = 5f
                xl.textColor = Color.parseColor("#000000")

                val yl: YAxis = binding.weightChart.axisRight
                yl.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
                yl.setDrawLimitLinesBehindData(true)
                yl.setDrawGridLines(true)
                yl.setDrawAxisLine(true)
                yl.isEnabled = false

                binding.weightChart.axisLeft.setAxisMinValue(0f)
                binding.weightChart.axisLeft.setAxisMaxValue(300f)
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

                binding.weightChart.axisLeft.labelCount = 7
                binding.weightChart.axisLeft.setStartAtZero(true)
                binding.weightChart.axisLeft.setDrawAxisLine(true)
                binding.weightChart.axisLeft.setDrawGridLines(true)


                set1.axisDependency = YAxis.AxisDependency.LEFT





                val data = BarData(set1)
                val chartatalist: ArrayList<BarData> = arrayListOf()
                chartatalist.add(data)
                binding.weightChart.data = data
                binding.weightChart.invalidate()



                binding.weightChart.data = data
                binding.weightChart.legend.isEnabled = false





















                /////////////////////////////////////


            }


        }

    }


    override fun OnEditIconClick(weightTable: BodyWeightTable) {
        val bundle = Bundle()
        bundle.putParcelable("weighttable", weightTable)

        findNavController().navigate(
            R.id.action_weightMainFragment_to_addWeightRecordFragment,
            bundle
        )

    }

    override fun OnItemClick(weightTable: BodyWeightTable) {

    }


}