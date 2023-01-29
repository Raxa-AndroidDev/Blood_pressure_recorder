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
import com.google.android.gms.ads.nativead.NativeAd
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentMainBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.utils.applyOnClick
import timber.log.Timber


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
       // askPermission()


    }

// storage permissions
    private fun askPermission() {
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                permissions.entries.forEach {
                    val isGranted = it.value
                    if (isGranted) {

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Storage Permission Required",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


            }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    private fun initListeners() {

        binding.bpLayout.applyOnClick {
            findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)
            (activity as MainActivity).binding.bottomNavView.setSelectedItem(2)
        }

       /* binding.bpLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)
            (activity as MainActivity).binding.bottomNavView.setSelectedItem(2)
        }*/

        binding.heartrateLayout.applyOnClick {
            findNavController().navigate(R.id.action_mainFragment_to_heartRateFragment)
            (activity as MainActivity).binding.bottomNavView.setSelectedItem(2)
        }




        binding.weightLayout.applyOnClick {
            findNavController().navigate(R.id.action_mainFragment_to_weightMainFragment)
            (activity as MainActivity).binding.bottomNavView.setSelectedItem(2)
        }

//        (activity as MainActivity).binding.bottomNavView.apply {
//
//
//            this.setOnItemSelectedListener {
//                when (it) {
//                    0 -> {
//
//                    }
//                    1 -> {
//                        findNavController().navigate(R.id.action_mainFragment_to_infoFragment)
//                    }
//                    2 -> {
//                        findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)
//
//
//                    }
//                }
//
//            }
//        }


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