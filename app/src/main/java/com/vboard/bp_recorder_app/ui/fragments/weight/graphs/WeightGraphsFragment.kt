package com.vboard.bp_recorder_app.ui.fragments.weight.graphs

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.opencsv.CSVWriter
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentWeightGraphsBinding
import com.vboard.bp_recorder_app.utils.Constansts
import java.io.File
import java.io.FileWriter


class WeightGraphsFragment : Fragment() {

    private lateinit var binding: com.vboard.bp_recorder_app.databinding.FragmentWeightGraphsBinding
    private lateinit var viewModel: WeightViewModel

    var pointList = ArrayList<PieEntry>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("test", "on  create view: of weight graph frag", )
        binding = FragmentWeightGraphsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("test", "on view created: of weight graph frag", )


        initViews()


    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[WeightViewModel::class.java]

        drawGraph()
    }

    private fun drawGraph() {


        viewModel.showWeightRecordFromDB().observe(viewLifecycleOwner) { weightRecordList ->
            val typesArray: ArrayList<String> = arrayListOf()

            if (weightRecordList.isNotEmpty()) {

                var avg = 0
                var min = weightRecordList[0].weight
                var max = weightRecordList[0].weight
                var sumofWeights = 0

                var verySUWCount = 0
                var severlyUWCount = 0
                var underWCount = 0
                var normalWCount = 0
                var overWCount = 0
                var obese1Count = 0
                var obese2Count = 0
                var obese3Count = 0



                for (i in weightRecordList.indices) {


                    if (!(typesArray.contains(weightRecordList[i].weightType))) {
                        typesArray.add(weightRecordList[i].weightType)
                    }

                    sumofWeights += weightRecordList[i].weight
                    Log.e("TAG", "drawGraph: ${weightRecordList[i].weightType}")


                    if (max < weightRecordList[i].weight) {
                        max = weightRecordList[i].weight
                    }

                    if (min > weightRecordList[i].weight) {
                        min = weightRecordList[i].weight
                    }

                    if (weightRecordList[i].weightType == Constansts.verySeverlyUnderWeight) {
                        verySUWCount += 1
                    }

                    if (weightRecordList[i].weightType == Constansts.severlyUnderWeight) {
                        severlyUWCount += 1
                    }

                    if (weightRecordList[i].weightType == Constansts.underWeight) {
                        underWCount += 1
                    }


                    if (weightRecordList[i].weightType == Constansts.normalWeight) {
                        normalWCount += 1
                    }

                    if (weightRecordList[i].weightType == Constansts.overWeight) {
                        overWCount += 1
                    }

                    if (weightRecordList[i].weightType == Constansts.obeseClass1) {
                        obese1Count += 1
                    }

                    if (weightRecordList[i].weightType == Constansts.obeseClass2) {
                        obese2Count += 1
                    }

                    if (weightRecordList[i].weightType == Constansts.obeseClass3) {
                        obese3Count += 1
                    }


                }

                avg = sumofWeights / weightRecordList.size

                binding.tvAvgValue.text = avg.toString()
                binding.tvMaxValue.text = max.toString()
                binding.tvMinValue.text = min.toString()





                if (verySUWCount > 0) {
                    pointList.add(
                        PieEntry(
                            verySUWCount.toFloat(),
                            Constansts.verySeverlyUnderWeight
                        )
                    )
                }
                if (severlyUWCount > 0) {
                    pointList.add(PieEntry(severlyUWCount.toFloat(), Constansts.severlyUnderWeight))
                }
                if (underWCount > 0) {
                    pointList.add(PieEntry(underWCount.toFloat(), Constansts.underWeight))
                }
                if (normalWCount > 0) {
                    pointList.add(PieEntry(normalWCount.toFloat(), Constansts.normalWeight))
                }
                if (overWCount > 0) {
                    pointList.add(PieEntry(overWCount.toFloat(), Constansts.overWeight))
                }
                if (obese1Count > 0) {
                    pointList.add(PieEntry(obese1Count.toFloat(), Constansts.obeseClass1))
                }
                if (obese2Count > 0) {
                    pointList.add(PieEntry(obese2Count.toFloat(), Constansts.obeseClass2))
                }
                if (obese3Count > 0) {
                    pointList.add(PieEntry(obese3Count.toFloat(), Constansts.obeseClass3))
                }


            }

            var pieDataSet = PieDataSet(pointList, "pie chart")


            pieDataSet.valueTextSize = 18f
            pieDataSet.valueTextColor = ContextCompat.getColor(requireContext(),R.color.black)


            pieDataSet.setAutomaticallyDisableSliceSpacing(false)


            var colorsArray = arrayListOf<Int>()
            for (i in typesArray.indices) {
                if (typesArray[i] == Constansts.verySeverlyUnderWeight) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.very_severly_uw
                        )
                    )
                }

                if (typesArray[i] == Constansts.severlyUnderWeight) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.severly_uw
                        )
                    )
                }
                if (typesArray[i] == Constansts.underWeight) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.underweight
                        )
                    )
                }

                if (typesArray[i] == Constansts.normalWeight) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.normalweight
                        )
                    )
                }

                if (typesArray[i] == Constansts.overWeight) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.overweight
                        )
                    )
                }

                if (typesArray[i] == Constansts.obeseClass1) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.obesestage1
                        )
                    )
                }
                if (typesArray[i] == Constansts.obeseClass2) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.obesestage2
                        )
                    )
                }
                if (typesArray[i] == Constansts.obeseClass3) {
                    colorsArray.add(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.obesestage3
                        )
                    )
                }


            }

            val colors: ArrayList<Int> = ArrayList()
            for (color in colorsArray) {
                colors.add(color)
            }

            pieDataSet.colors = colors
            pieDataSet.sliceSpace = 6f
            pieDataSet.iconsOffset = MPPointF(0f, 40f)
            pieDataSet.selectionShift = 5f


            var pieData = PieData(pieDataSet)



            binding.weightPieChart.data = pieData




            // entry label styling
            binding.weightPieChart.setEntryLabelColor(Color.WHITE)
            binding.weightPieChart.setDrawEntryLabels(true)

            binding.weightPieChart.setEntryLabelTextSize(10f)

            binding.weightPieChart.minAngleForSlices = 20f

            binding.weightPieChart.isDrawHoleEnabled = false
            binding.weightPieChart.setHoleColor(Color.WHITE)

            binding.weightPieChart.setTransparentCircleColor(Color.WHITE)
            binding.weightPieChart.setTransparentCircleAlpha(110)

            //binding.weightPieChart.holeRadius = 58f
            binding.weightPieChart.animateY(1000, Easing.EaseInOutQuad)
            binding.weightPieChart.transparentCircleRadius = 61f

            binding.weightPieChart.setDrawCenterText(true)
            binding.weightPieChart.description.isEnabled = false
            binding.weightPieChart.legend.isEnabled = false

            binding.weightPieChart.invalidate()


        }

    }




}