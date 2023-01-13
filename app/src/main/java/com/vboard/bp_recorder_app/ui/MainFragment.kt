package com.vboard.bp_recorder_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.NativeTemplateStyle.Builder
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.nativead.NativeAd
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

       // loadNativeAd()
        initListeners()

        val navController = findNavController()

        binding.bottomNavView.setupWithNavController(navController)


    }

//    private fun loadNativeAd() {
//
//        val adLoader:AdLoader = Builder()
//
//        val adLoader: AdLoader = Builder(this, "ca-app-pub-3940256099942544/2247696110")
//            .forNativeAd(object : NativeAd.OnNativeAdLoadedListener() {
//                fun onNativeAdLoaded(nativeAd: NativeAd?) {
//                    val styles =
//                        NativeTemplateStyle.Builder().build()
//                    val template: TemplateView = findViewById(R.id.native_ad_template)
//                    template.setStyles(styles)
//                    template.setNativeAd(nativeAd)
//                }
//            })
//            .build()
//
//        adLoader.loadAd(NativeTemplateStyle.Builder().build())
//    }

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
}