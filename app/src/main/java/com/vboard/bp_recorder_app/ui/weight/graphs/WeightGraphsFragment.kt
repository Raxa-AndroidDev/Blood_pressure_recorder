package com.vboard.bp_recorder_app.ui.weight.graphs

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightGraphsBinding
import java.io.File
import java.io.FileWriter


class WeightGraphsFragment : Fragment() {

    private lateinit var binding: FragmentWeightGraphsBinding
    private lateinit var viewModel: WeightViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeightGraphsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initViews()


    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[WeightViewModel::class.java]


        drawGraph()
    }



    private fun drawGraph() {
        val chartPointsList = ArrayList<PieEntry>()



        chartPointsList.add(PieEntry(200F,"normal"))
        chartPointsList.add(PieEntry(300F,"obese"))
        chartPointsList.add(PieEntry(400F,"obese"))
        chartPointsList.add(PieEntry(150f,"underweight"))
        chartPointsList.add(PieEntry(190f,"normal"))
        chartPointsList.add(PieEntry(200F,"normal"))


val pieDataset = PieDataSet(chartPointsList,"weight Pie Chart")
      //  pieDataset.setColors(ColorTemplate.COLORFUL_COLORS,requireContext())

        val pieData = PieData(pieDataset)
        binding.weightPieChart.data = pieData



        viewModel.showWeightRecordFromDB().observe(viewLifecycleOwner) { weightRecordList ->
            if (weightRecordList.isNotEmpty()) {

                calculateMinMaxAvg(weightRecordList)


                for (i in weightRecordList.indices) {


                }


            }


        }

    }

    private fun calculateMinMaxAvg(weightRecordList: List<BodyWeightTable>) {
        var  sumOfWeights = 0
        var max = 0
        var min = 0
            for ( i in weightRecordList.indices){
                sumOfWeights += weightRecordList[i].weight
                if (weightRecordList[i].weight>max){
                    max = weightRecordList[i].weight
                }

                if (weightRecordList[i].weight<min){
                    min = weightRecordList[i].weight
                }
        }

        var avg = sumOfWeights/weightRecordList.size
        binding.tvAvgValue.text = avg.toString()
        binding.tvMinValue.text = min.toString()
        binding.tvMaxValue.text = max.toString()



    }


    private fun exportCSV() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "BloodPressure app csv" + ".csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            // val curCSV: Cursor = DatabaseClass.getDBInstance(requireContext()).query("SELECT * FROM " + BloodPressureTable, null)
            DatabaseClass.getDBInstance(requireContext()).bpDao().fetchAllBPRecords()
                .observe(viewLifecycleOwner) {
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