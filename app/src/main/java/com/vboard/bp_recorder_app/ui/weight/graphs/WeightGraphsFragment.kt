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
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightGraphsBinding
import java.io.File
import java.io.FileWriter


class WeightGraphsFragment : Fragment() {

    private lateinit var binding: FragmentWeightGraphsBinding
    private lateinit var viewModel: BPRecordViewModel

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
        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        drawGraph()
    }

    private fun drawGraph() {
        val chartPointsList = ArrayList<CandleEntry>()


        viewModel.ShowBPRecordFromDB().observe(viewLifecycleOwner) { bplist ->
            if (bplist.isNotEmpty()) {
                for (i in bplist.indices) {


                }


            }


        }

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