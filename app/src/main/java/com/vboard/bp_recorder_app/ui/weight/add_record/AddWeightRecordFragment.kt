package com.vboard.bp_recorder_app.ui.weight.add_record

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.vboard.bp_recorder_app.ChipAdapter
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.viewModels.WeightViewModel
import com.vboard.bp_recorder_app.databinding.FragmentAddWeightRecordBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.weight.WeightTypesModelClass
import com.vboard.bp_recorder_app.ui.weight.add_record.adapters.WeightTypeColorsAdapter
import com.vboard.bp_recorder_app.ui.weight.add_record.adapters.WeightTypesAdapter
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ChipListCallBacks
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class AddWeightRecordFragment : Fragment(), ChipListCallBacks {

    private lateinit var binding: FragmentAddWeightRecordBinding

    lateinit var viewModel: WeightViewModel
    private lateinit var adapter: WeightTypeColorsAdapter
    var weightTypesList: ArrayList<WeightTypesModelClass> = arrayListOf()
    var selectedChipsList: ArrayList<String> = arrayListOf()

    var weightTable: BodyWeightTable? = null

    private var label: String? = null

    private var selectedDate: String? = null
    var selectedTime: String? = null
    var bmi: Double = 0.0
    var weightType: String? = null
    var completeDateAndTime: String? = null
    var width: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWeightRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val wm: WindowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        width = display.width

        initViews()


    }

    private fun initViews() {
        handleBottombar()

        viewModel = ViewModelProvider(this)[WeightViewModel::class.java]

        weightTable = requireArguments().getParcelable("weighttable")
        weightTypesList = getWeightRangeTypesList(requireContext())

        initRecyclerview()
        populateValues()
        setNumberPickerValues()


        clickListeners()


    }

    private fun clickListeners() {
        binding.apply {

            singleDayPicker.addOnDateChangedListener { displayed, date ->

                val calendar: Calendar = Calendar.getInstance()
                calendar.time = date
                selectedTime = getTime(calendar)

                selectedDate = getDateToStore(calendar)
                completeDateAndTime = date.toString()

                Timber.e("displayed time is ${selectedTime} , displayed date is ${date} ")

            }

            binding.btnOk.setOnClickListener {

                if (selectedDate != null && selectedTime != null) {
                    if (weightTable != null) {
                        updateWeightRecord()
                    } else {
                        storeWeightRecord()
                    }

                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill out All fields.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            infoAboutWeightTypes.setOnClickListener {
                showBottomSheet()
            }

            tvAddNoteTitle.setOnClickListener {
                showTagsBottomSheet()
            }


        }
    }

    private fun showTagsBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.bp_types_bottomsheet)

        val chipsList: ArrayList<String> = getChipsList()

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.CENTER

        bottomSheet.findViewById<TextView>(R.id.tv_bp_types_bs_title)!!.text =
            getString(R.string.notes)
        bottomSheet.findViewById<MaterialButton>(R.id.button_got_it)!!.apply {
            this.text = getString(R.string.save)
            this.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_selected_color))
            this.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)

            this.setOnClickListener {
                bottomSheet.dismiss()
            }
        }
        bottomSheet.findViewById<ImageView>(R.id.img_cross)!!.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.findViewById<RecyclerView>(R.id.bp_types_listview)!!.apply {

            this.layoutManager = flexboxLayoutManager

            this.adapter = ChipAdapter(requireContext(), chipsList, this@AddWeightRecordFragment)

        }



        bottomSheet.show()

    }

    private fun updateWeightRecord() {

        label = if (selectedChipsList != null) {
            java.lang.String.join("# ", selectedChipsList)
        } else {
            "#default"
        }
        viewModel.updateWeightRecord(
            BodyWeightTable(
                weightTable!!.id,
                selectedDate!!.toLong(),
                selectedTime!!,
                completeDateAndTime!!,
                binding.weightNumberpicker.value,
                binding.heightNumberpicker.value,
                bmi.toString(),
                weightType!!,
                label!!
            )
        )


        Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_LONG).show()

    }

    private fun storeWeightRecord() {
        label = if (selectedChipsList != null) {
            java.lang.String.join("# ", selectedChipsList)
        } else {
            "#default"
        }


        viewModel.storeWeightRecordInDB(
            BodyWeightTable(
                0,
                selectedDate!!.toLong(),
                selectedTime!!,
                completeDateAndTime!!,
                binding.weightNumberpicker.value,
                binding.heightNumberpicker.value,
                bmi.toString(),
                weightType!!,
                label!!
            )
        )

        Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()

    }

    private fun initRecyclerview() {
        adapter = WeightTypeColorsAdapter(requireContext(), width, weightTypesList)
        binding.weightTypeColorsRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.weightTypeColorsRcv.adapter = adapter
    }


    private fun handleBottombar() {
        (activity as MainActivity).binding.bottomNavView.visibility = View.GONE
    }

    private fun populateValues() {
        if (weightTable != null) {
            // coming from history for editing

            binding.weightNumberpicker.value = weightTable!!.weight
            binding.heightNumberpicker.value = weightTable!!.height

            label = "default"

            val myCalendar: Calendar = Calendar.getInstance()
            myCalendar.time = weightTable!!.date.toDate()

            val completeDateFormat =
                SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())
            var datetoset = completeDateFormat.parse(weightTable!!.fulldate) as Date

            binding.singleDayPicker.setDefaultDate(datetoset)
            binding.singleDayPicker.setTimeZone(TimeZone.getDefault())

            completeDateAndTime = weightTable!!.fulldate
            selectedDate = weightTable!!.date.tostring()
            selectedTime = weightTable!!.time


            bmi = calculateBMI(weightTable!!.weight, weightTable!!.height)
            weightType = weightTable!!.weightType

            Timber.e("current time  is ${selectedTime} ")
            binding.btnOk.text = getString(R.string.update)

        } else {
            // coming from main
            binding.weightNumberpicker.value = 90
            binding.heightNumberpicker.value = 65

            label = "default"

            val myCalendar: Calendar = Calendar.getInstance()
            val datecurrent = Date()

            binding.singleDayPicker.setDefaultDate(datecurrent)
            binding.singleDayPicker.setTimeZone(TimeZone.getDefault())


            selectedDate = getDateToStore(myCalendar)

            completeDateAndTime = datecurrent.toString()

            selectedTime = getTime(myCalendar)

            bmi = calculateBMI(binding.weightNumberpicker.value, binding.heightNumberpicker.value)
            weightType = getWeightType(bmi)

            Timber.e("current time  is ${selectedTime} ")
            binding.btnOk.text = getString(R.string.save)

        }
    }


    private fun showBottomSheet() {
        val bottomsheet = BottomSheetDialog(requireContext())
        bottomsheet.setContentView(R.layout.bp_types_bottomsheet)


        val recView = bottomsheet.findViewById<RecyclerView>(R.id.bp_types_listview)
        recView!!.layoutManager = LinearLayoutManager(requireContext())
        recView.adapter = WeightTypesAdapter(requireContext(), weightTypesList)




        bottomsheet.findViewById<ImageView>(R.id.img_cross)!!.setOnClickListener {
            bottomsheet.dismiss()
        }

        bottomsheet.findViewById<MaterialButton>(R.id.button_got_it)!!.setOnClickListener {
            bottomsheet.dismiss()
        }







        bottomsheet.show()
    }

    private fun setNumberPickerValues() {
        Timber.e("set Number picker called")
        binding.apply {

            weightNumberpicker.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)
            heightNumberpicker.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)

            weightNumberpicker.maxValue = 300
            weightNumberpicker.minValue = 10

            heightNumberpicker.maxValue = 300
            heightNumberpicker.minValue = 10

            setWeightType(bmi)
            Toast.makeText(requireContext(),"bmi is ${bmi}",Toast.LENGTH_SHORT).show()
            Timber.e("bmi is ${bmi}")

            weightNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->

               /* val bmiValue = String.format(" %.2f", weightNumberpicker.value*703/newVal*newVal)
                bmi = bmiValue.toDouble()*/
                bmi =( weightNumberpicker.value.toDouble() / ((newVal/100) * (newVal/100)).toDouble())
                setWeightType(bmi)
                Toast.makeText(requireContext(),"bmi is ${bmi}",Toast.LENGTH_SHORT).show()
                Timber.e("bmi is $bmi")
            }

            heightNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->

        /*        val bmiValue = String.format(" %.2f", weightNumberpicker.value*703/newVal*newVal)
               bmi = bmiValue.toDouble()*/
             bmi = (weightNumberpicker.value.toDouble() / ((newVal/100) * (newVal/100)).toDouble())
                Toast.makeText(requireContext(),"bmi is ,,, $bmi",Toast.LENGTH_SHORT).show()
                Timber.e("bmi is ${bmi}")
                setWeightType(bmi)

            }


        }
    }

    private fun setWeightType(bmi: Double) {

        var weighttype = getWeightType(bmi)
        binding.weightNumberpicker.isSelected = true
        binding.heightNumberpicker.isSelected = true

        when (weighttype) {
            Constansts.verySeverlyUnderWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.very_severly_uw)

                binding.tvWeightRangeDescription.text = getString(R.string.veryseverlyuw_range)

weightTypesList[1].isSelected = true
                for (i in 0 until weightTypesList.size) {

                    weightTypesList[i].isSelected = i == 0

                }


            }
            Constansts.severlyUnderWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.severly_uw)
                binding.tvWeightRangeDescription.text = getString(R.string.severluw_range)

                weightTypesList[2].isSelected = true

                for (i in 0 until weightTypesList.size) {

                    weightTypesList[i].isSelected = i == 1

                }


            }
            Constansts.underWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.underweight)
                binding.tvWeightRangeDescription.text = getString(R.string.underweight_range)

                weightTypesList[2].isSelected = true
                for (i in 0 until weightTypesList.size) {

                    weightTypesList[i].isSelected = i == 2

                }


            }
            Constansts.normalWeight -> {

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.normalweight)
                binding.tvWeightRangeDescription.text = getString(R.string.normalweight_range)
                weightTypesList[2].isSelected = true
                for (i in 0 until weightTypesList.size) {

                    weightTypesList[i].isSelected = i == 3

                }


            }
            Constansts.overWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.overweight)
                binding.tvWeightRangeDescription.text = getString(R.string.overweight_range)
                weightTypesList[2].isSelected = true

                for (i in 0 until weightTypesList.size) {

                    weightTypesList[i].isSelected = i == 4

                }


            }
            Constansts.obeseClass1 -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.obesestage1)
                binding.tvWeightRangeDescription.text = getString(R.string.obesestage1_range)

                for (i in 0 until weightTypesList.size) {
                    weightTypesList[i].isSelected = i == 5
                }
            }
            Constansts.obeseClass2 -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.obesestage2)
                binding.tvWeightRangeDescription.text = getString(R.string.obesestage2_range)

                for (i in 0 until weightTypesList.size) {
                    weightTypesList[i].isSelected = i == 6
                }
            }
            Constansts.obeseClass3 -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.obesestage3)
                binding.tvWeightRangeDescription.text = getString(R.string.obesestage3_range)

                for (i in 0 until weightTypesList.size) {
                    weightTypesList[i].isSelected = i == 7
                }
            }
        }

        binding.tvWeightCondition.text = weighttype

        adapter.notifyDataSetChanged()


    }

    private fun setNumberPickerColor(color: Int) {

        binding.tvWeightCondition.setTextColor(ContextCompat.getColor(requireContext(), color))
        binding.imgHeartIcon.setColorFilter(color)

        binding.weightNumberpicker.selectedTextColor =
            ContextCompat.getColor(requireContext(), color)
        binding.heightNumberpicker.selectedTextColor =
            ContextCompat.getColor(requireContext(), color)


        isNumberPickersSelected(false)
    }

    private fun isNumberPickersSelected(isSelected: Boolean) {
        if (isSelected) {
            binding.weightNumberpicker.isSelected = true
            binding.heightNumberpicker.isSelected = true

        } else {
            binding.weightNumberpicker.isSelected = false
            binding.heightNumberpicker.isSelected = false
        }
    }


    private fun registerFilterChanged(chips_group: ChipGroup) {
        val ids = chips_group.getCheckedChipIds()

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

    override fun onChipSelected(tag: String) {

        selectedChipsList.add(tag)

    }


}