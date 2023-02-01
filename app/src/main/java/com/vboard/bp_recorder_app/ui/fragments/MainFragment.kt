package com.vboard.bp_recorder_app.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadNativeAd()
        initListeners()



    }



    private fun initListeners() {

        binding.bpLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)

        }


        binding.heartrateLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_heartRateFragment)

        }




        binding.weightLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_weightMainFragment)

        }

    }

    private fun loadNativeAd() {

        val adLoader: AdLoader =
            AdLoader.Builder(requireContext(), "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd {
                    val styles = NativeTemplateStyle.Builder().build()
                    val template: TemplateView = binding.nativeAdTemplate
                    template.setStyles(styles)
                    template.setNativeAd(it)
                }
                .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }


}