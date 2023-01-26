package com.vboard.bp_recorder_app.ui.fragments.weight.add_record

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
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
import com.vboard.bp_recorder_app.ui.fragments.weight.WeightTypesModelClass
import com.vboard.bp_recorder_app.ui.fragments.weight.add_record.adapters.WeightTypeColorsAdapter
import com.vboard.bp_recorder_app.ui.fragments.weight.add_record.adapters.WeightTypesAdapter
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


    var tagsBottomSheet:BottomSheetDialog? = null


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
                showTagsBottomSheet("default", "")
            }


        }
    }

    private fun showTagsBottomSheet(mode: String, tag: String) {
        tagsBottomSheet = BottomSheetDialog(requireContext())
        tagsBottomSheet!!.setContentView(R.layout.bp_types_bottomsheet)

        val chipsList: ArrayList<String> = getChipsList(mode, tag)

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.CENTER

        tagsBottomSheet!!.findViewById<TextView>(R.id.tv_bp_types_bs_title)!!.text =
            getString(R.string.notes)
        tagsBottomSheet!!.findViewById<MaterialButton>(R.id.button_got_it)!!.apply {
            this.text = getString(R.string.save)
            this.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_selected_color))
            this.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)

            this.setOnClickListener {
                tagsBottomSheet!!.dismiss()
            }
        }
        tagsBottomSheet!!.findViewById<ImageView>(R.id.img_cross)!!.setOnClickListener {
            tagsBottomSheet!!.dismiss()
        }

        tagsBottomSheet!!.findViewById<RecyclerView>(R.id.bp_types_listview)!!.apply {

            this.layoutManager = flexboxLayoutManager
            val adapter = ChipAdapter(requireContext(), chipsList, this@AddWeightRecordFragment)
            this.adapter = adapter

        }



        tagsBottomSheet!!.show()

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
        Log.e("TAG", "storeWeightRecord: $weightType")

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
            binding.weightNumberpicker.value = 60
            binding.heightNumberpicker.value = 100

            label = "default"

            val myCalendar: Calendar = Calendar.getInstance()
            val datecurrent = Date()

            binding.singleDayPicker.setDefaultDate(datecurrent)
            binding.singleDayPicker.setTimeZone(TimeZone.getDefault())


            selectedDate = getDateToStore(myCalendar)

            completeDateAndTime = datecurrent.toString()

            selectedTime = getTime(myCalendar)

            bmi = calculateBMI(binding.weightNumberpicker.value, binding.heightNumberpicker.value)

            Log.e("TAG", "populateValues: ${binding.heightNumberpicker.value}  $bmi")
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
            weightNumberpicker.minValue = 20

            heightNumberpicker.maxValue = 300
            heightNumberpicker.minValue = 20

            setWeightType(bmi)

            weightNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->
                bmi = calculateBMI(newVal, heightNumberpicker.value)

                setWeightType(bmi)

            }

            heightNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->
                bmi = calculateBMI(weightNumberpicker.value, newVal)


                setWeightType(bmi)


            }


        }
    }

    fun setWeightType(bmi: Double) {

        weightType = getWeightType(bmi)
        binding.weightNumberpicker.isSelected = true
        binding.heightNumberpicker.isSelected = true

        binding.tvWeightCondition.text = weightType
        when (weightType) {
            Constansts.verySeverlyUnderWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.very_severly_uw)

                binding.tvWeightRangeDescription.text = "BMI <16.0"


                for (i in 0..5) {

                    weightTypesList[i].isSelected = i == 0

                }


            }
            Constansts.severlyUnderWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.severly_uw)
                binding.tvWeightRangeDescription.text = "BMI 16.0-16.9"

                weightTypesList[2].isSelected = true

                for (i in 0..5) {

                    weightTypesList[i].isSelected = i == 1

                }


            }
            Constansts.underWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.underweight)
                binding.tvWeightRangeDescription.text = "BMI 17.0-18.4"

                weightTypesList[2].isSelected = true
                for (i in 0..7) {

                    weightTypesList[i].isSelected = i == 2

                }


            }
            Constansts.normalWeight -> {

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.normalweight)
                binding.tvWeightRangeDescription.text = "BMI 18.5-24.9"
                weightTypesList[2].isSelected = true
                for (i in 0..7) {

                    weightTypesList[i].isSelected = i == 3

                }


            }
            Constansts.overWeight -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.overweight)
                binding.tvWeightRangeDescription.text = "BMI 25.0-29.9"
                weightTypesList[2].isSelected = true

                for (i in 0..7) {

                    weightTypesList[i].isSelected = i == 4

                }


            }
            Constansts.obeseClass1 -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.obesestage1)
                binding.tvWeightRangeDescription.text = "BMI 30.0-34.9"

                for (i in 0..7) {
                    weightTypesList[i].isSelected = i == 5
                }
            }
            Constansts.obeseClass2 -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.obesestage2)
                binding.tvWeightRangeDescription.text = "BMI 35.0-39.9"

                for (i in 0..7) {
                    weightTypesList[i].isSelected = i == 6
                }
            }
            Constansts.obeseClass3 -> {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.obesestage3)
                binding.tvWeightRangeDescription.text = "BMI >40.0"

                for (i in 0..7) {
                    weightTypesList[i].isSelected = i == 7
                }
            }
        }



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

    override fun onChipSelected(tag: String, isSelected: Boolean, position: Int) {

        if (position == 0) {
            showBSToAddChip()
            tagsBottomSheet!!.dismiss()
        } else {
            if (isSelected) {
                selectedChipsList.add(tag)
            } else if (!isSelected && selectedChipsList.isNotEmpty()) {
                selectedChipsList.remove(tag)
            }
        }


    }

    private fun showBSToAddChip() {
        val bottomsheet = BottomSheetDialog(requireContext())
        bottomsheet.setContentView(R.layout.add_tags_bottomsheet)

        bottomsheet.findViewById<MaterialButton>(R.id.button_ok)!!.setOnClickListener {
            bottomsheet.findViewById<EditText>(R.id.et_add_tag)!!.apply {
                if (this.text.isNotEmpty()) {
                    val tag = this.text.toString()

                    bottomsheet.dismiss()
                    showTagsBottomSheet("add", tag)
                } else {
                    Toast.makeText(requireContext(), "No Tag Found", Toast.LENGTH_LONG).show()
                }
            }


        }


        bottomsheet.findViewById<MaterialButton>(R.id.button_cancel)!!.setOnClickListener {
            showTagsBottomSheet("default", "")
            bottomsheet.dismiss()
        }
        bottomsheet.show()
    }


}