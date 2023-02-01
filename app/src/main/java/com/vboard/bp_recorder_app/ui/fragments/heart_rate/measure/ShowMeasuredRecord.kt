package com.vboard.bp_recorder_app.ui.fragments.heart_rate.measure

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentShowMeasuredRecordBinding
import com.vboard.bp_recorder_app.utils.getHRRangeTypesList


class ShowMeasuredRecord : Fragment() {
    lateinit var binding: FragmentShowMeasuredRecordBinding

    var hrtypesList: ArrayList<HRTypesModelClass> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowMeasuredRecordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.infoAboutHrTypes.setOnClickListener {
            showBottomSheet()
        }


        hrtypesList = getHRRangeTypesList(requireContext())


    }




    private fun showBottomSheet() {
        val bottomsheet = BottomSheetDialog(requireContext())
        bottomsheet.setContentView(R.layout.hr_types_bottomsheet)







        bottomsheet.findViewById<ImageView>(R.id.img_cross)!!.setOnClickListener {
            bottomsheet.dismiss()
        }

        bottomsheet.findViewById<MaterialButton>(R.id.button_got_it)!!.setOnClickListener {
            bottomsheet.dismiss()
        }

        bottomsheet.show()
    }


}