package com.steve_md.smartmkulima.ui.fragments.main.buyer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.FragmentMarketProduceInDetailsBinding
import com.steve_md.smartmkulima.utils.OverlayService
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideSupportActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketProduceInDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMarketProduceInDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketProduceInDetailsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSupportActionBar()

        Glide.with(binding.imageView32)
            .load(arguments?.getString("productImage"))
            .centerCrop()
            .into(binding.imageView32)
        binding.textView175.text = arguments?.getString("productTitle")
        binding.textView176.text = ""+arguments?.getString("productDescription")

        binding.addToCart.setOnClickListener {
            // check permission for overlay over other apps.
            checkOverlayPermission()
        }
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                // Permission is not granted, request it
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))

                startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
            } else {
                // Permission is granted, show the overlay
                showOverLay()
            }
        } else {
            // For devices below API 23, permission is granted by default
            showOverLay()
        }
    }

    private fun showOverLay() {
        val intent = Intent(requireContext(), OverlayService::class.java)
        requireContext().startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(requireContext())) {
                    // Permission is granted
                    showOverLay()

                    displaySnackBar("Permitted to overlay over other apps")
                } else {
                    displaySnackBar("No permission to overlay over other apps")
                }
            }
        }
    }



    companion object {
        const val REQUEST_CODE_OVERLAY_PERMISSION = 1
    }
}