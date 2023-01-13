package com.vboard.bp_recorder_app.ui

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentMainBinding
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

        // loadNativeAd()

        askPermission()


    }

    private fun askPermission() {
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

               permissions.entries.forEach {
                  val isGranted = it.value
                   if (isGranted){



                       initListeners()


                   }
                   else {
                      Toast.makeText(requireContext(),"Storage Permission Required",Toast.LENGTH_LONG).show()
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
        binding.bpLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_BPMainFragment)
        }

        binding.heartrateLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_heartRateFragment)
        }




        binding.weightLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_weightMainFragment)
        }


        // will be dealt on monday
       /* (activity as MainActivity).binding.bottomNavView.setOnItemSelectedListener {
            Timber.e("ids are ${it}")
            when(it){
               0 ->{
                   findNavController().navigate(Uri.parse("yourapp://BloodPressure/app/main/frag"  ))
                }
               1->{
                   findNavController().navigate(Uri.parse(  "BloodPressure_app_info_frag"  ))
                }
                2 ->{
                    findNavController().navigate(Uri.parse(  "yourapp://BloodPressure/app/bp/frag"))
                }
            }
        }*/


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


}