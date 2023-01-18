package com.vboard.bp_recorder_app.ui.info_module

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentInfoBinding
import com.vboard.bp_recorder_app.ui.MainActivity


class InfoFragment : Fragment() {

lateinit var binding:FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentInfoBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).binding.bottomNavView.apply {


            if (!(this.isVisible)) {
                this.visibility = View.VISIBLE
            }



            this.setOnItemSelectedListener {
            when (it){
                0 ->{
                    findNavController().navigate(R.id.action_infoFragment_to_mainFragment)
                }
                1 ->{}
                2 ->{
                    findNavController().navigate(R.id.action_infoFragment_to_BPMainFragment)


                }
            }

        }
        }

    }

}