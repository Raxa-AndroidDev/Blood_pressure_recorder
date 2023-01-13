package com.vboard.bp_recorder_app.ui.blood_pressure.add_bp_record

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.vboard.bp_recorder_app.BPValuesModelClass
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.databinding.FragmentAddBPRecordBinding
import com.vboard.bp_recorder_app.data.viewModels.BPRecordViewModel
import com.vboard.bp_recorder_app.ui.blood_pressure.BPTypesModelClass
import com.vboard.bp_recorder_app.ui.blood_pressure.add_bp_record.adapters.BPTypeColorsAdapter
import com.vboard.bp_recorder_app.ui.blood_pressure.add_bp_record.adapters.BPTypesAdapter
import com.vboard.bp_recorder_app.utils.Constansts
import com.vboard.bp_recorder_app.utils.CurrentDate
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddBPRecordFragment : Fragment() {
    private lateinit var binding: FragmentAddBPRecordBinding

    lateinit var viewModel: BPRecordViewModel
    private lateinit var adapter: BPTypeColorsAdapter
    private var label: String? = null
    private var bpType: String = Constansts.bp_Normal
    var selectedDate: String? = null
    var selectedTime: String? = null
    var pasTime:String? = null
    var width: Int = 0

     var table_id:Int =0

    val bptypesList: ArrayList<BPTypesModelClass> = arrayListOf()
    var bloodPressureTable:BloodPressureTable? = null


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
        val display: Display = wm.getDefaultDisplay()
        width = display.width



        initViews()


    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[BPRecordViewModel::class.java]



         bloodPressureTable = requireArguments().getParcelable<BloodPressureTable>("bloodpressuretable")

        populateValues()

        bptypesList.add(
            BPTypesModelClass(
                R.color.hypotension_bp_color,
                Constansts.bp_hypotension,
                getString(R.string.hypo_bp_range),
                false
            )
        )
        bptypesList.add(
            BPTypesModelClass(
                R.color.normal_bp_color,
                Constansts.bp_Normal,
                getString(R.string.normal_bp_range), false
            )
        )
        bptypesList.add(
            BPTypesModelClass(
                R.color.elevated_bp_color,
                Constansts.bp_Elevated,
                getString(R.string.elevated_bp_range), false
            )
        )
        bptypesList.add(
            BPTypesModelClass(
                R.color.hyper_stage1_color,
                Constansts.bp_Hypertension1,
                getString(R.string.stage1_bp_range), false
            )
        )
        bptypesList.add(
            BPTypesModelClass(
                R.color.hyper_stage2_color,
                Constansts.bp_Hypertension2,
                getString(R.string.stage2_bp_range), false
            )
        )
        bptypesList.add(
            BPTypesModelClass(
                R.color.hyper_crisis_color,
                Constansts.bp_Crisis,
                getString(R.string.critical_bp_range), false
            )
        )






        adapter = BPTypeColorsAdapter(requireContext(), width, bptypesList)
        binding.bpTypeColorsRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.bpTypeColorsRcv.adapter = adapter


        setNumberPickerValues()

        binding.singleDayPicker.setTimeZone(TimeZone.getDefault())

        binding.apply {




            singleDayPicker.addOnDateChangedListener { displayed, date ->


                selectedDate =
                    SimpleDateFormat(Constansts.dateFormate, Locale.getDefault()).format(date)

                selectedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)

                Timber.e("displayed date is ${selectedTime} ")


            }

            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            infoAboutBpTypes.setOnClickListener {
                showBottomSheet()
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

    private fun populateValues() {
        if (bloodPressureTable!=null){
            // coming from history for editing
            binding.systolicNumberpicker.value = bloodPressureTable!!.systolic
            binding.diastolicNumberpicker.value = bloodPressureTable!!.diaSystolic
            binding.pulseNumberpicker.value = bloodPressureTable!!.pulse

            selectedDate = bloodPressureTable!!.date
            selectedTime = bloodPressureTable!!.time
            pasTime = bloodPressureTable!!.DateAndTime




            binding.btnOk.setOnClickListener {

                label = "default"

                if (selectedDate != null && selectedTime != null) {

                    Timber.e("stored time is ${selectedTime} ")
                    Timber.e("stored date is ${selectedDate} ")

                    viewModel.UpdateBPRecord(
                        BloodPressureTable(
                            bloodPressureTable!!.id,
                            selectedDate!!,
                            selectedTime!!,
                            pasTime!!,
                            binding.systolicNumberpicker.value,
                            binding.diastolicNumberpicker.value,
                            binding.pulseNumberpicker.value,
                            label!!
                        )
                    )

                    Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_LONG)
                        .show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill out All fields.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

        }
        else{
            // coming from main
            binding.systolicNumberpicker.value = 90
            binding.diastolicNumberpicker.value = 65
            binding.pulseNumberpicker.value = 75

            val myCalendar: Calendar = Calendar.getInstance()

            val datecurrent = Date()
            val dateFormatcurrent = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
            pasTime = dateFormatcurrent.format(datecurrent)

            val currentDateTime = System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val currentTime = simpleDateFormat.format(currentDateTime)

            selectedDate = CurrentDate(myCalendar)
            selectedTime = currentTime



            binding.btnOk.setOnClickListener {


                label = "default"


                if (selectedDate != null && selectedTime != null) {

                    Timber.e("stored time is ${selectedTime} ")
                    Timber.e("stored date is ${selectedDate} ")

                    viewModel.StoreBPRecordInDB(
                        BloodPressureTable(
                            0,
                            selectedDate!!,
                            selectedTime!!,
                            pasTime!!,
                            binding.systolicNumberpicker.value,
                            binding.diastolicNumberpicker.value,
                            binding.pulseNumberpicker.value,
                            label!!
                        )
                    )

                    Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG)
                        .show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill out All fields.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

        }
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
                bpType = Constansts.bp_hypotension

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
                bpType = Constansts.bp_Elevated

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.elevated_bp_color)
                binding.tvBpRangeDescription.text = getString(R.string.elevated_bp_range)

                bptypesList[2].isSelected = true
                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 2

                }


            } else if (this.systolic in 130..139 || this.diastolic in 80..89) {
                bpType = Constansts.bp_Hypertension1
                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hyper_stage1_color)
                binding.tvBpRangeDescription.text = getString(R.string.stage1_bp_range)
                bptypesList[2].isSelected = true
                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 3

                }


            } else if (this.systolic in 140..180 || this.diastolic in 90..120) {
                bpType = Constansts.bp_Hypertension2

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hyper_stage2_color)
                binding.tvBpRangeDescription.text = getString(R.string.stage2_bp_range)
                bptypesList[2].isSelected = true

                for (i in 0..5) {

                    bptypesList[i].isSelected = i == 4

                }


            } else if (this.systolic > 180 || this.diastolic > 120) {
                bpType = Constansts.bp_Crisis

                isNumberPickersSelected(true)
                setNumberPickerColor(R.color.hyper_crisis_color)
                binding.tvBpRangeDescription.text = getString(R.string.critical_bp_range)

                for (i in 0..5) {
                    bptypesList[i].isSelected = i == 5
                }
            }

            binding.tvBpCondition.text = bpType

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


}