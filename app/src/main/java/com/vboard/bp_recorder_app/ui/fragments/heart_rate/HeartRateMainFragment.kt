package com.vboard.bp_recorder_app.ui.fragments.heart_rate

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.viewModels.HRViewModel
import com.vboard.bp_recorder_app.databinding.FragmentMainHeartRateBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.fragments.heart_rate.adapters.ViewPagerAdapter
import timber.log.Timber
import java.io.File
import java.io.FileWriter

class HeartRateMainFragment : Fragment() {

    private lateinit var binding: FragmentMainHeartRateBinding

    lateinit var viewModel: HRViewModel
    val tabList = arrayOf("History", "Analysis")
    var label: String = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainHeartRateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonMeasureHr.setOnClickListener {
            startActivity(Intent(requireContext(), HeartRateMonitor::class.java))

        }
        binding.buttonAddHr.setOnClickListener {
            findNavController().navigate(R.id.action_heartRateFragment_to_addHeartRateFragment)
        }
*/
        initValues()
    }


    private fun initValues() {
        viewModel = ViewModelProvider(this)[HRViewModel::class.java]

        handleBottombar()
        tabInitialization()
        initListeners()
    }


    private fun handleBottombar() {

    }

    private fun tabInitialization() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

    }

    private fun initListeners() {


        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.tvExport.setOnClickListener {
            DatabaseClass.getDBInstance(requireContext()).bpDao().fetchAllBPRecords()
                .observe(viewLifecycleOwner) {

                    if (it.isEmpty()) {
                        Toast.makeText(requireContext(), "No Data to Export", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        createCSV()
                    }

                }

        }


    }

    fun createCSV() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "Heart_Rate_Table" + ".csv")
        try {

            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))


            // Coloumn Names are written
            csvWrite.writeNext(
                arrayOf(
                    "id",
                    "date",
                    "time",
                    "completeDate",
                    "weight",
                    "height",
                    "tag"
                )
            )

            DatabaseClass.getDBInstance(requireContext()).hrDao().fetchAllHeartRateRecords()
                .observe(viewLifecycleOwner) { bp ->
                    bp.forEach { it ->


                        val arrStr = arrayOf(
                            "${it.id}",
                            it.date.toString(),
                            it.time,
                            it.DateAndTime, it.BPM, it.label
                        )



                        csvWrite.writeNext(arrStr)
                    }

                    csvWrite.close()

                }






            Toast.makeText(requireContext(), "Exported", Toast.LENGTH_SHORT).show()

        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }


}