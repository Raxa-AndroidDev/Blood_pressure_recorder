package com.vboard.bp_recorder_app.ui.fragments.info_module.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vboard.bp_recorder_app.databinding.FragmentInfoDetailBinding
import com.vboard.bp_recorder_app.utils.getInfoDetailsData

class InfoDetailFragment : Fragment() {
    lateinit var binding: FragmentInfoDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()


    }

    private fun initViews() {

        populateTopBar()

        initRecView()


    }

    private fun populateTopBar() {
        val title = requireArguments().getString("title")
        val icon = requireArguments().getInt("icon")
        val color = requireArguments().getString("color")



        binding.topTitlebar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                color!!.toInt()
            )
        )


        binding.tvInfoDetailTitle.text = title
        binding.imgInfoIcon.setImageResource(icon)
    }

    private fun initRecView() {
        val info_detail_list =getInfoDetailsData(requireArguments().getString("title")!!)

        binding.infoDetailRcv.layoutManager = LinearLayoutManager(requireContext())
        binding.infoDetailRcv.adapter = InfoDetailAdapter(requireContext(), info_detail_list)
    }


}