package com.vboard.bp_recorder_app.ui.weight.history

import android.app.Dialog
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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.button.MaterialButton
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightHistoryRecordBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.weight.history.adapter.WeightHistoryAdapter
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

        showEmptyListDialog()

        handleBottomBar()
        clickListeners()
        viewModel = ViewModelProvider(this)[WeightViewModel::class.java]

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

            // to enable or disable zooming of the axis
            weightChart.isScaleYEnabled = false
            weightChart.isScaleXEnabled = false

            weightChart.xAxis.setDrawAxisLine(false)
            weightChart.xAxis.setDrawGridLines(false)

            weightChart.setDrawGridBackground(false)

            weightChart.axisLeft.setDrawGridLines(false)
            weightChart.axisLeft.isEnabled = true

            weightChart.axisRight.setDrawGridLines(true)
            weightChart.axisRight.setDrawAxisLine(false)
            weightChart.axisRight.isEnabled = false
            weightChart.legend.isEnabled = true

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


                val data = BarData(set1)
                val chartatalist: ArrayList<BarData> = arrayListOf()
                chartatalist.add(data)
                binding.weightChart.data = data
                binding.weightChart.invalidate()


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