package com.steve_md.smartmkulima.ui.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentAddNewFarmFieldBinding
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Create a new Farm field
 */
@AndroidEntryPoint
class AddNewFarmFieldFragment : Fragment() {

    private lateinit var binding: FragmentAddNewFarmFieldBinding
    private val viewModel: MainViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null
    private var farmMappingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewFarmFieldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieving values from Bundle() and Setting the arguments.
        val farmSize = arguments?.getString("FARM_SIZE")
        val farmCoordinates = arguments?.getString("FARM_COORDINATES")

        binding.farmSizeInHaInput.setText(farmSize)
        binding.autoFillFarmCoordinatesEditText.setText(farmCoordinates)

        setUpBinding()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback!!
        )
    }


    private fun setUpBinding() {

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.switchAllowMapOutFarmBoundaries.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState(isChecked)
            handleSwitchState(isChecked)
        }

        // Getting the Switch State
        val isSwitchChecked = getSwitchState()
        binding.switchAllowMapOutFarmBoundaries.isChecked = isSwitchChecked
        handleSwitchState(isSwitchChecked)

        binding.apply {
            cardViewClickToMap.setOnClickListener {
                initDialogToStartMapping()
            }
        }

          /**
           * Collecting UI inputs
           */
            val farmName = binding.inputFarmName.text.toString()
            val yourCrop = binding.spinnerYourCropName.selectedItem.toString()
            val farmDescription = binding.inputFarmLocationDescription.text.toString()
            val farmSize = binding.farmSizeInHaInput.text.toString()
            val farmOwnershipType = binding.spinnerOwnershipType.selectedItem.toString()
            val farmingSeason = binding.spinnerSeason.selectedItem.toString()
            val yearOfFarming = binding.spinnerYearOfFarming.selectedItem.toString()
            val countyLocationOfTheFarm = binding.spinnerCountyLocationOftheFarm.selectedItem.toString()


            binding.buttonClickCreateFarm.setOnClickListener {

                if (inputsAreValidated()) {
                    hideKeyboard()

                    val newFarmField = NewFarmField(
                        farmName =  binding.inputFarmName.text.toString(),
                        cropName = binding.spinnerYourCropName.selectedItem.toString(),
                        farmDescription =  binding.inputFarmLocationDescription.text.toString(),
                        farmSizeInHa =  binding.farmSizeInHaInput.text.toString(),
                        estimatedNumberOfFarmersPerDay = binding.stimatedFarmersPerDayInput.text.toString(),
                        ownershipType = binding.spinnerOwnershipType.selectedItem.toString(),
                        season = binding.spinnerSeason.selectedItem.toString(),
                        year = binding.spinnerYearOfFarming.selectedItem.toString(),
                        countyLocationOfTheFarm = binding.spinnerCountyLocationOftheFarm.selectedItem.toString()
                    )


                    lifecycleScope.launch {

                        try {
                            viewModel.addFarmField(newFarmField)
                            requireActivity().runOnUiThread {
                                displaySnackBar("Successfully created a new farm")
                            }

                            findNavController().popBackStack()

                            findNavController().navigate(R.id.addedNewFarmFieldsFragment)

                            Timber
                                .tag("AddNewFarmFieldFragment")
                                .e("Request==Success ==> Successfully created a new farm: ${newFarmField.farmName}")

                        } catch (e: Exception) {

                            displaySnackBar("Your request to add a new farm failed.")
                            Timber.tag("AddNewFarmFieldFragment").e(
                                "Request==Failed ==>Could not create a new farm ${e.localizedMessage}"
                            )
                        }
                    }
                }
            }
    }

    private fun inputsAreValidated(): Boolean {
        return if (binding.inputFarmName.text.toString().isEmpty() ||
            binding.farmSizeInHaInput.text.toString().isEmpty() ||
            binding.inputFarmLocationDescription.text.toString().isEmpty()
        ) {

            binding.enterFarmNameTl.error = "**required"
            binding.yourFarmSizeInHectares.error = "**required"
            binding.enterFarmLocationDescription.error = "**required"

            false
        } else {
            true
        }
    }


    private fun handleSwitchState(isChecked: Boolean) {

        if (isChecked) {
            binding.imageView12.isVisible = true
            binding.textView115.isVisible = true
            binding.autoFillFarmCoordinatesTl.isVisible = true
            binding.cardViewClickToMap.isVisible = true


            //binding.farmSizeInHaInput.isEnabled = false
            //binding.autoFillFarmCoordinatesEditText.isEnabled = false
            binding.farmSizeInHaInput.hint = null
            binding.autoFillFarmCoordinatesEditText.hint = null


            binding.farmSizeInHaInput.apply {
                isClickable = true
                isFocusable = false
                isFocusableInTouchMode = false
            }

            binding.autoFillFarmCoordinatesEditText.apply {
                isClickable = true
                isFocusable = false
                isFocusableInTouchMode = false
            }
            binding.farmSizeInHaInput.setOnClickListener {
                showDialogFarmMappingIsON()
            }

            binding.autoFillFarmCoordinatesEditText.setOnClickListener {
                showDialogFarmMappingIsON()
            }

            autoFillFarmDetails()

        } else {
            binding.imageView12.isVisible = false
            binding.textView115.isVisible = false
            binding.autoFillFarmCoordinatesTl.isVisible = false
            binding.cardViewClickToMap.isVisible = false
            binding.farmSizeInHaInput.isEnabled = true
            binding.autoFillFarmCoordinatesEditText.isEnabled = true

            // if switched back / off
            // clear the prefs
            clearSwitchState()

            binding.farmSizeInHaInput.apply {
                isClickable = true
                isFocusable = true
                isFocusableInTouchMode = true
            }

            binding.autoFillFarmCoordinatesEditText.apply {
                isClickable = true
                isFocusable = true
                isFocusableInTouchMode = true
            }

            dismissVisibleDialogs1()
            dismissVisibleDialogs()

            autoFillFarmDetails()
        }
    }

    private fun dismissVisibleDialogs() {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val dialogs = fragmentManager.fragments.filterIsInstance<AlertDialog>()
        dialogs.forEach { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }

    private fun autoFillFarmDetails() {

        // Calculating the estimated no. of farmers
        /**
         * if 1 hectare = 3 farmers
         * then return, otherwise calculate the number of farmers
         */
        binding.apply {


            val theFarmSize = binding.farmSizeInHaInput.text.toString().toDoubleOrNull()

            val calculatedFarmersValue = theFarmSize?.times(3)

            if (theFarmSize != null) {
                if (calculatedFarmersValue == 0.0) {
                    binding.stimatedFarmersPerDayInput.setText("0")
                } else if (calculatedFarmersValue != null) {
                    if (calculatedFarmersValue > 0 && calculatedFarmersValue < 1) {
                        binding.stimatedFarmersPerDayInput.setText("3")
                    } else {
                        val estimatedFarmers = calculatedFarmersValue.toInt().toString()
                        binding.stimatedFarmersPerDayInput.setText(estimatedFarmers)
                    }
                }
            } else {
                // Handle the case when farmSize is empty
                binding.stimatedFarmersPerDayInput.setText("0")
            }
        }
    }

    private fun saveSwitchState(checked: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences("farm_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isSwitchChecked", checked)
        editor.apply()
    }


    private fun clearSwitchState() {
        val sharedPreferences = requireContext().getSharedPreferences("farm_prefs", Context.MODE_PRIVATE)
        val editor1 = sharedPreferences.edit()
        editor1.clear()
        editor1.apply()
    }

    private fun getSwitchState(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("farm_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isSwitchChecked", false)
    }

    /**
     * Show Mapping Of Farms is in Progress!
     */
    private fun showDialogFarmMappingIsON() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Farm Mapping is ON")
//            .setMessage("Since you want to map your farm now, the size will be automatically inserted for you.")
//            .setPositiveButton("I Understand") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .show()
        farmMappingDialog = AlertDialog.Builder(requireContext())
            .setTitle("Farm Mapping is ON")
            .setMessage("Since you want to map your farm now, the size will be automatically inserted for you.")
            .setPositiveButton("I Understand") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        farmMappingDialog?.show()
    }
    private fun dismissVisibleDialogs1() {
        farmMappingDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }

    private fun initDialogToStartMapping() {
        AlertDialog.Builder(requireContext())
            .setTitle("Farm Mapping")
            .setMessage("Choose between Manual mapping by walking to map your farm or Automatic farm mapping by placing pins on the map?")
            .setPositiveButton("Automatic Mapping") { dialog, _ ->
                dialog.cancel()
                navigateToAutoFarmMapping()
            }
            .setNegativeButton("Manual Mapping") { _, _ ->
                startManualFarmMapping()
            }
            .setCancelable(true)
            .show()
    }

    private fun startManualFarmMapping() {
        findNavController().navigate(R.id.manualWalkingFarmMappingFragment)
    }

    private fun navigateToAutoFarmMapping() {
        findNavController().navigate(R.id.mappingFarmLocationWithPinsFragment)
    }

    fun exitDialog() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Do you really want to exit?")
            .setMessage("Your entered data will be lost")
            .setPositiveButton("Yes") { _, which ->
                removeFocus()
                clearInputFields()
                findNavController().navigateUp()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
        builder.show()
    }

    private fun removeFocus() {
        binding.root.requestFocus()
    }
    private fun clearInputFields() {
        binding.inputFarmName.text?.clear()
        binding.inputFarmLocationDescription.text?.clear()
        binding.farmSizeInHaInput.text?.clear()
        binding.stimatedFarmersPerDayInput.text?.clear()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (onBackPressedCallback != null) {
            onBackPressedCallback!!.isEnabled = false
            onBackPressedCallback!!.remove()
        }
    }
}