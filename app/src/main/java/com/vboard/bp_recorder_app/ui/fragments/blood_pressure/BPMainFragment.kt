package com.vboard.bp_recorder_app.ui.fragments.blood_pressure

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import com.vboard.bp_recorder_app.databinding.FragmentBPMainBinding
import com.vboard.bp_recorder_app.ui.fragments.blood_pressure.adapter.ViewPagerAdapter
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.util.*

class BPMainFragment : Fragment() {

    lateinit var binding: FragmentBPMainBinding
    lateinit var viewModel: BPRecordViewModel

    val tabList = arrayOf("History", "Analysis")
    val myCalendar: Calendar = Calendar.getInstance()
    var choosenDate: String? = null
    var choosenTime: String? = null

    var label: String = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBPMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initValues()


    }


    private fun initValues() {
        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]



        handleBottombar()
        tabInitialization()
        initListeners()
    }


    fun createCSV() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "bp_table" + ".csv")
        try {

            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()

            val charThatWillNotAppearInText = 127.toChar()
            val csvWrite = CSVWriter(FileWriter(file),charThatWillNotAppearInText,',','.',null )


            // Coloumn Names are written
            csvWrite.writeNext(
                arrayOf(
                    "id",
                    "date",
                    "time",
                    "DateAndTime",
                    "systolic",
                    "diaSystolic",
                    "pulse",
                    "tag"
                )
            )

            DatabaseClass.getDBInstance(requireContext()).bpDao().fetchAllBPRecords()
                .observe(viewLifecycleOwner) { bp ->
                    bp.forEach { it ->


                        val arrStr = arrayOf(
                            "${it.id}",
                            it.date.toString(),
                            it.time,
                            it.fulldate,
                            "${it.systolic}",
                            "${it.diaSystolic}",
                            "${it.pulse}",
                            it.tag
                        )


                        Timber.e("size is ${arrStr}")
                        csvWrite.writeNext(arrStr)
                    }

                    csvWrite.close()

                }






            Toast.makeText(requireContext(), "Exported", Toast.LENGTH_SHORT).show()

        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
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





    private fun registerFilterChanged(chips_group: ChipGroup) {
        val ids = chips_group.checkedChipIds

        val titles = mutableListOf<CharSequence>()

        ids.forEach { id ->
            titles.add(chips_group.findViewById<Chip>(id).text)
        }

        label = if (titles.isNotEmpty()) {
            titles.joinToString(", ")
        } else {
            ""
        }

    }




}