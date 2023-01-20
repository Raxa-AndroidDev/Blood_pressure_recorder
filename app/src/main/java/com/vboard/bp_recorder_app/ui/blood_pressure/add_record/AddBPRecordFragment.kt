package com.vboard.bp_recorder_app.ui.blood_pressure.add_record

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
import com.vboard.bp_recorder_app.BPValuesModelClass
import com.vboard.bp_recorder_app.ChipAdapter
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import com.vboard.bp_recorder_app.databinding.FragmentAddBPRecordBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.ui.blood_pressure.add_record.adapters.BPTypeColorsAdapter
import com.vboard.bp_recorder_app.ui.blood_pressure.add_record.adapters.BPTypesAdapter
import com.vboard.bp_recorder_app.ui.blood_pressure.model_classes.BPTypesModelClass
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ChipListCallBacks
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class AddBPRecordFragment : Fragment(),ChipListCallBacks {

    private lateinit var binding: FragmentAddBPRecordBinding

    lateinit var viewModel: BPRecordViewModel
    private lateinit var adapter: BPTypeColorsAdapter
    var bptypesList: ArrayList<BPTypesModelClass> = arrayListOf()
    var selectedChipsList:ArrayList<String>  = arrayListOf()

    var bloodPressureTable: BloodPressureTable? = null

    private var label: String? = null
    private var bpType: String = Constansts.bp_Normal
    var selectedDate: String? = null
    var selectedTime: String? = null
    var completeDate: String? = null
    var width: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBPRecordBinding.inflate(inflater)
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

        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]

        bloodPressureTable = requireArguments().getParcelable("bloodpressuretable")
        bptypesList = getBPRangeTypesList(requireContext())

        initRecyclerview()
        populateValues()
        setNumberPickerValues()


        clickListeners()


    }

    private fun populateValues() {
        if (bloodPressureTable != null) {
            // coming from history for editing

            binding.systolicNumberpicker.value = bloodPressureTable!!.systolic
            binding.diastolicNumberpicker.value = bloodPressureTable!!.diaSystolic
            binding.pulseNumberpicker.value = bloodPressureTable!!.pulse
            label = "default"

            val myCalendar: Calendar = Calendar.getInstance()
            myCalendar.time = bloodPressureTable!!.date.toDate()

            val completeDateFormat = SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())
            var  datetoset =  completeDateFormat.parse(bloodPressureTable!!.fulldate) as Date

            binding.singleDayPicker.setDefaultDate(datetoset)


            binding.singleDayPicker.setTimeZone(TimeZone.getDefault())

            completeDate = bloodPressureTable!!.fulldate
            selectedDate =  getDateToStore(myCalendar)
            selectedTime = getTime(myCalendar)

            Timber.e("current time  is ${selectedTime} ")
            binding.btnOk.text = getString(R.string.update)

        } else {
            // coming from main
            binding.systolicNumberpicker.value = 90
            binding.diastolicNumberpicker.value = 65
            binding.pulseNumberpicker.value = 75
            label = "default"

            val myCalendar: Calendar = Calendar.getInstance()
            val datecurrent = Date()

            binding.singleDayPicker.setDefaultDate(datecurrent)
            binding.singleDayPicker.setTimeZone(TimeZone.getDefault())



            selectedDate =  getDateToStore(myCalendar)


            completeDate = datecurrent.toString()

            selectedTime = getTime(myCalendar)

            Timber.e("current time  is ${selectedTime} ")
            binding.btnOk.text = getString(R.string.save)

        }
    }

    private fun clickListeners() {
        binding.apply {

            singleDayPicker.addOnDateChangedListener { displayed, date ->

                val calendar: Calendar = Calendar.getInstance()
                calendar.time = date
                selectedTime = getTime(calendar)



                selectedDate = getDateToStore(calendar)


                completeDate = date.toString()

                Timber.e("displayed time is ${selectedTime} , displayed date is ${date} ")

            }

            binding.btnOk.setOnClickListener {

                if (selectedDate != null && selectedTime != null) {
                    if (bloodPressureTable != null) {
                        updateBPRecord()
                    } else {
                        storeBPRecord()
                    }

                    findNavController().popBackStack()
                }
                else {
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

            infoAboutBpTypes.setOnClickListener {
                showBottomSheet()
            }

            tvAddNoteTitle.setOnClickListener {
                showTagsBottomSheet()
            }


            //region chip group code

            //            chipGroup.forEach { child ->
//                (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
//                    registerFilterChanged(chipGroup)
//                }
//            }

            //endregion


        }
    }

    private fun showTagsBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.bp_types_bottomsheet)

        val chipsList:ArrayList<String> = getChipsList()

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.CENTER

        bottomSheet.findViewById<TextView>(R.id.tv_bp_types_bs_title)!!.text = getString(R.string.notes)
        bottomSheet.findViewById<MaterialButton>(R.id.button_got_it)!!.apply {
            this.text = getString(R.string.save)
            this.setTextColor(ContextCompat.getColor(requireContext(),R.color.tab_selected_color))
            this.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.white)

            this.setOnClickListener {
                bottomSheet.dismiss()
            }
        }
        bottomSheet.findViewById<ImageView>(R.id.img_cross)!!.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.findViewById<RecyclerView>(R.id.bp_types_listview)!!.apply {


            //this.layoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
            this.layoutManager = flexboxLayoutManager

            this.adapter = ChipAdapter(requireContext(),chipsList,this@AddBPRecordFragment)

        }



        bottomSheet.show()

    }

    private fun updateBPRecord() {

        label = if (selectedChipsList!=null){
            java.lang.String.join("# ", selectedChipsList)
        }else {
            "#default"
        }
        viewModel.UpdateBPRecord(
            BloodPressureTable(
                bloodPressureTable!!.id,
                selectedDate!!.toLong(),
                selectedTime!!,
                completeDate!!,
                binding.systolicNumberpicker.value,
                binding.diastolicNumberpicker.value,
                binding.pulseNumberpicker.value,
                label!!
            )
        )


        Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_LONG).show()

    }

    private fun storeBPRecord() {
        label = if (selectedChipsList!=null){
            java.lang.String.join("# ", selectedChipsList)
        }else {
            "#default"
        }


        viewModel.StoreBPRecordInDB(
            BloodPressureTable(
                0,
                selectedDate!!.toLong(),
                selectedTime!!,
                completeDate!!,
                binding.systolicNumberpicker.value,
                binding.diastolicNumberpicker.value,
                binding.pulseNumberpicker.value,
                label!!
            )
        )

        Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()


        Timber.e("stored date is ${selectedDate!!.toLong()}")

    }

    private fun initRecyclerview() {
        adapter = BPTypeColorsAdapter(requireContext(), width, bptypesList)
        binding.bpTypeColorsRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.bpTypeColorsRcv.adapter = adapter
    }


    private fun handleBottombar() {
        (activity as MainActivity).binding.bottomNavView.visibility = View.GONE
    }



    private fun showBottomSheet() {
        val bottomsheet = BottomSheetDialog(requireContext())
        bottomsheet.setContentView(R.layout.bp_types_bottomsheet)


        val recView = bottomsheet.findViewById<RecyclerView>(R.id.bp_types_listview)
        recView!!.layoutManager = LinearLayoutManager(requireContext())
        recView.adapter = BPTypesAdapter(requireContext(), bptypesList)




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

            systolicNumberpicker.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)
            diastolicNumberpicker.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)
            pulseNumberpicker.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)


            systolicNumberpicker.maxValue = 300
            systolicNumberpicker.minValue = 20



            diastolicNumberpicker.maxValue = 300
            diastolicNumberpicker.minValue = 20


            pulseNumberpicker.maxValue = 200
            pulseNumberpicker.minValue = 20


            setBPType(BPValuesModelClass(systolicNumberpicker.value, diastolicNumberpicker.value))




            systolicNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->

                setBPType(BPValuesModelClass(newVal, diastolicNumberpicker.value))
            }

            diastolicNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->

                setBPType(BPValuesModelClass(systolicNumberpicker.value, newVal))

            }

            pulseNumberpicker.setOnValueChangedListener { picker, oldVal, newVal ->
                viewModel.getPulseNumberPickerValue(oldVal, newVal)

            }


        }
    }

    fun setBPType(bpValuesModelClass: BPValuesModelClass) {


        binding.systolicNumberpicker.isSelected = true
        binding.diastolicNumberpicker.isSelected = true
        binding.pulseNumberpicker.isSelected = true
        Timber.e("set Bp type called")
        bpValuesModelClass.apply {

            Timber.e("bptype systolic ${this.systolic}, diastolic ${this.diastolic} , bp type is $bpType")

            if (this.systolic < 90 || this.diastolic < 60) {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hypotension_bp_color)

                binding.tvBpRangeDescription.text = getString(R.string.hypo_bp_range)


                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 0

                }


            } else if (this.systolic in 90..119 && this.diastolic in 60..79) {
                bpType = Constansts.bp_Normal

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.normal_bp_color)
                binding.tvBpRangeDescription.text = getString(R.string.normal_bp_range)

                bptypesList[2].isSelected = true

                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 1

                }


            } else if (this.systolic in 120..129 && this.diastolic in 60..79) {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.elevated_bp_color)
                binding.tvBpRangeDescription.text = getString(R.string.elevated_bp_range)

                bptypesList[2].isSelected = true
                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 2

                }


            } else if (this.systolic in 130..139 || this.diastolic in 80..89) {

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hyper_stage1_color)
                binding.tvBpRangeDescription.text = getString(R.string.stage1_bp_range)
                bptypesList[2].isSelected = true
                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 3

                }


            } else if (this.systolic in 140..180 || this.diastolic in 90..120) {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hyper_stage2_color)
                binding.tvBpRangeDescription.text = getString(R.string.stage2_bp_range)
                bptypesList[2].isSelected = true

                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 4

                }


            } else if (this.systolic > 180 || this.diastolic > 120) {


                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hyper_crisis_color)
                binding.tvBpRangeDescription.text = getString(R.string.critical_bp_range)

                for (i in 0..5) {
                    bptypesList[i].isSelected = i == 5
                }
            }

            binding.tvBpCondition.text = getBPType(this.systolic, this.diastolic)

            adapter.notifyDataSetChanged()

        }


    }

    private fun setNumberPickerColor(color: Int) {

        binding.tvBpCondition.setTextColor(ContextCompat.getColor(requireContext(), color))
        binding.imgHeartIcon.setColorFilter(color)

        binding.systolicNumberpicker.selectedTextColor =
            ContextCompat.getColor(requireContext(), color)
        binding.diastolicNumberpicker.selectedTextColor =
            ContextCompat.getColor(requireContext(), color)
        binding.pulseNumberpicker.selectedTextColor =
            ContextCompat.getColor(requireContext(), color)

        isNumberPickersSelected(false)
    }

    private fun isNumberPickersSelected(isSelected: Boolean) {
        if (isSelected) {
            binding.systolicNumberpicker.isSelected = true
            binding.diastolicNumberpicker.isSelected = true
            binding.pulseNumberpicker.isSelected = true
        } else {
            binding.systolicNumberpicker.isSelected = false
            binding.diastolicNumberpicker.isSelected = false
            binding.pulseNumberpicker.isSelected = false
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