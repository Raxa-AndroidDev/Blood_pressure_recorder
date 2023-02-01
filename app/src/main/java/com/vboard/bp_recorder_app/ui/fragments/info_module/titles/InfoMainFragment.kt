package com.vboard.bp_recorder_app.ui.fragments.info_module.titles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentInfoBinding
import com.vboard.bp_recorder_app.ui.fragments.info_module.details.InfoDetailsCallBack
import com.vboard.bp_recorder_app.utils.getInfoBgColorsList
import com.vboard.bp_recorder_app.utils.getInfoTitleIconsList
import com.vboard.bp_recorder_app.utils.getInfoTitlesList


class InfoMainFragment : Fragment(), InfoDetailsCallBack {

    lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()


    }

    private fun initViews() {


        initRecView()

        binding.imgBackIcon.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initRecView() {
        val infoTitlesList = populateInfoModelClass()
        binding.infoTitlesRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.infoTitlesRcv.adapter = InfoTitlesAdapter(requireContext(), this, infoTitlesList)
    }


    private fun populateInfoModelClass(): ArrayList<InfoTitlesModelClass> {
        val infoTitlesList: ArrayList<InfoTitlesModelClass> = arrayListOf()

        val colorsList = getInfoBgColorsList()
        val imagesList = getInfoTitleIconsList()
        val titlesList = getInfoTitlesList()

        for (i in colorsList.indices) {
            infoTitlesList.add(InfoTitlesModelClass(titlesList[i], colorsList[i], imagesList[i]))
        }




        return infoTitlesList
    }

    override fun OnDetailClick(icon: Int, color: String, title: String) {
        val bundle = Bundle()

        bundle.putInt("icon", icon)
        bundle.putString("color", color)
        bundle.putString("title", title)


        findNavController().navigate(R.id.action_infoFragment_to_infoDetailFragment, bundle)

    }

}