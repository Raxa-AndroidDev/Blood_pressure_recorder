package com.vboard.bp_recorder_app.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import java.io.File
import java.io.FileWriter


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        createCSV()


    }




    fun createCSV(){
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "bp_table" + ".csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val bpList = ArrayList<BloodPressureTable>()

            // Coloumn Names are written
            csvWrite.writeNext(arrayOf("ID","Name","BP"))


            bpList.forEach {
                val arrStr = arrayOf("${it.id}","${it.tag}","${it.systolic}")
                csvWrite.writeNext(arrStr)
            }

            csvWrite.close()

            Toast.makeText(requireContext(), "Exported", Toast.LENGTH_SHORT).show()

        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }

}

