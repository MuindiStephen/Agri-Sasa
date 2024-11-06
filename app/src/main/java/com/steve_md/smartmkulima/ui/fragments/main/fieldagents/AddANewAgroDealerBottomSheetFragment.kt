package com.steve_md.smartmkulima.ui.fragments.main.fieldagents

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.databinding.FragmentAddANewAgroDealerBottomsheetBinding
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentAddAgroDealerData
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentEarnings
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Launched as sheet from bottom
 * to add a new agro-dealer
 */
@AndroidEntryPoint
class AddANewAgroDealerBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddANewAgroDealerBottomsheetBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddANewAgroDealerBottomsheetBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.tag("-on View Created - ShowAddAgrodealerBottomSheet 2").d("Received arg is: ${arguments?.getString("fieldAgentEmail")}")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.inputAgroDealerLocation.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // pick current location of the agrodealer
                // during visits made
                getCurrentLocation { location ->
                    binding.inputAgroDealerLocation.setText("${location.latitude}, ${location.longitude}")
                }
            }
        }

        binding.addAgroDealerByFieldAgentBtn.setOnClickListener {

            if (inputsAreValidated()) {
                val fieldAgentAddAgroDealerData = FieldAgentAddAgroDealerData(
                        name = binding.inputAgroDealerName.text.toString(),
                        email = binding.inputAgroDealerEmail.text.toString(),
                        phone = binding.inputAgroDealerPhone.text.toString(),
                        location = binding.inputAgroDealerLocation.text.toString(),
                        physicalLocationAddress = binding.inputAgroDealerPhysicalAddress.text.toString(),
                        offers = binding.spinnerSelectOfferCategories.selectedItem.toString(),
                        agentId = "${arguments?.getString("fieldAgentEmail")}"
                    )


                try {
                    viewModel.fieldAgentAddANewAgroDealer(fieldAgentAddAgroDealerData)

                    requireActivity().runOnUiThread {
                        displaySnackBar("A new Agro-dealer was added.")
                    }
                    Timber.e("REQUEST=SUCCEEDED: Added a new legit agro-dealer")
                    dismiss()
                } catch (e: Exception) {
                    Timber.e("REQUEST=FAILED: Could not add a new agrodealer: "+e.message.toString())
                    dismiss()
                }

            }

        }

    }

    private fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onLocationReceived(location)
                } else {
                    toast("Unable to get location")
                }
            }
    }

    private fun inputsAreValidated(): Boolean {
        return if (binding.inputAgroDealerName.text.toString().isEmpty() ||
            binding.inputAgroDealerEmail.text.toString().isEmpty() ||
            binding.inputAgroDealerPhone.text.toString().isEmpty() ||
            binding.inputAgroDealerLocation.text.toString().isEmpty() ||
            binding.inputAgroDealerPhysicalAddress.text.toString().isEmpty()

        ) {

            binding.enterAgroDealerName.error = "**required"
            binding.enterAgroDealerPhone.error = "**required"
            binding.enterAgroDealerEmailAddress.error = "**required"
            binding.enterAgroDealerPhysicalAddress.error = "**required"
            binding.enterAgroDealerExactLocation.error = "**required"

            false
        } else {
            true
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val TAG = "AddNewAgroDealerBottomSheet"
    }


}