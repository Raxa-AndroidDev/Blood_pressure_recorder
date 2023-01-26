package com.vboard.bp_recorder_app.ui.fragments.blood_pressure.graphs

import android.R
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.databinding.FragmentBPGraphsBinding
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import java.io.File
import java.io.FileWriter


class BPGraphsFragment : Fragment() {

    private lateinit var binding: FragmentBPGraphsBinding
    private lateinit var viewModel: BPRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBPGraphsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initilization()
        drawGraph()
    }

    private fun initilization() {
        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]
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

        viewModel.ShowBPRecordFromDB().observe(viewLifecycleOwner){
            bplist ->
            if (bplist.isNotEmpty()) {
                for (i in bplist.indices) {

                    Log.e("TAG", "drawGraph:systolic  ${bplist[i].systolic.toFloat()}")
                    chartPointsList.add(
                        CandleEntry(
                            (i+1.0).toFloat(),
                            (bplist[i].systolic+2.0).toFloat(),
                            (bplist[i].diaSystolic-2.0).toFloat(),
                            bplist[i].systolic.toFloat(),
                            bplist[i].diaSystolic.toFloat()
                        )
                    )
                }



                val yValsCandleStick = ArrayList<CandleEntry>()
                yValsCandleStick.add(CandleEntry(0f,190.0f,  170.0f, 92f,68f))
                yValsCandleStick.add(CandleEntry(1f,190.0f,  60.0f, 97f,58f))
                yValsCandleStick.add(CandleEntry(2f,40.0f,  30.0f, 82f,68f))







                val set1 = CandleDataSet(chartPointsList, "DataSet 1")
                set1.color = Color.rgb(80, 80, 80)
                set1.shadowWidth = 0f
                set1.formLineWidth =  1f
                set1.decreasingColor = resources.getColor(R.color.holo_red_dark)
                set1.increasingColor = resources.getColor(R.color.holo_red_dark)
                set1.decreasingPaintStyle = Paint.Style.FILL
                set1.increasingPaintStyle = Paint.Style.FILL
                set1.neutralColor =  resources.getColor(R.color.holo_red_dark)
                set1.setDrawValues(false)

                val data = CandleData(set1)
                binding.bpChart.setData(data)
                binding.bpChart.invalidate()



            }


        }

    }


    private fun exportCSV(){
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "BloodPressure app csv" + ".csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
           // val curCSV: Cursor = DatabaseClass.getDBInstance(requireContext()).query("SELECT * FROM " + BloodPressureTable, null)
           DatabaseClass.getDBInstance(requireContext()).bpDao().fetchAllBPRecords().observe(viewLifecycleOwner){
              for (i in it.indices) {
                //  csvWrite.writeNext(it[i].systolic)

              }
           }
//            csvWrite.writeNext(curCSV.getColumnNames())
//            while (curCSV.moveToNext()) {
//                //Which column you want to exprort
//                val arrStr = arrayOfNulls<String>(curCSV.getColumnCount())
//                for (i in 0 until curCSV.getColumnCount() - 1) arrStr[i] = curCSV.getString(i)
//                csvWrite.writeNext(arrStr)
//            }
//            csvWrite.close()
//            curCSV.close()
            //ToastHelper.showToast(this, "Exported", Toast.LENGTH_SHORT)
        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }

}