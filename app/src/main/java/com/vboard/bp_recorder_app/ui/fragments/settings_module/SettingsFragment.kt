package com.vboard.bp_recorder_app.ui.fragments.settings_module

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vboard.bp_recorder_app.BuildConfig
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        initViews()
    }

    private fun initViews() {

        binding.imgBackButton.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.tvExportSettings.setOnClickListener {
            showExportOptionsBottomSheet()
        }

        binding.tvShareSettings.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                var shareMessage = "\nLet me recommend you\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (_: Exception) {

            }
        }


        binding.tvRateSettings.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=${requireActivity().packageName}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)

            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=${requireActivity().packageName}")
                    )
                )
            }
        }
    }

    private fun showExportOptionsBottomSheet() {

    }

}