package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentCropCycleAddRevenueGeneratedBinding
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CropCycleAddRevenueGeneratedFragment : Fragment() {
    private lateinit var binding: FragmentCropCycleAddRevenueGeneratedBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleAddRevenueGeneratedBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        fetchFirstAvailableCropCycles()

        setupVBinding()
    }

    private fun setupVBinding() {
        binding.buttonCreateCropCycleTotalRevenue.setOnClickListener {
            if (validateInputs()) {

                val farmFinanceRevenueRecords = FarmFinanceRevenueRecords(
                    cropCycleName = binding.spinnerNameOfCycle.selectedItem.toString() ,
                    harvestedBags = binding.inputHarvestedBags.text.toString(),
                    soldBags = binding.inputSoldBags.text.toString() ,
                    revenueAfterSales = binding.inputTotalRevenueAfterSales.text.toString(),
                )

                lifecycleScope.launch {
                    try {
                        viewModel.addFarmCycleRevenue(farmFinanceRevenueRecords)
                        requireActivity().runOnUiThread {
                            displaySnackBar("Successfully Added after harvest records.")
                        }

                        // TODO()
                        // navigate to my records screen......


                    } catch (e: Exception) {
                        displaySnackBar("Your request failed.")
                        Timber.tag("Add=Expenses").e(
                            "Request==Failed ==>Could not create this after harvest record ${e.localizedMessage}"
                        )
                    }
                }

            }
        }
    }

    /**
     * Linking now already available crop cycles
     */
    private fun fetchFirstAvailableCropCycles() {

        lifecycleScope.launch {
            viewModel.allCycles.observe(viewLifecycleOwner) { cycles ->
                if (cycles.isNotEmpty()) {

                    val allCropCycles = cycles.map {
                        it.cropName
                    }

                    val adapter = ArrayAdapter<String>(this@CropCycleAddRevenueGeneratedFragment.requireContext(),
                        android.R.layout.simple_spinner_item, allCropCycles)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerNameOfCycle.adapter = adapter
                    Timber.d("Crop cycles are available and in progress...Update any records")


                } else {
                    displaySnackBar("No crop cycle found, you cannot create any records.")
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        return binding.inputHarvestedBags.text.isNullOrEmpty().not().also {
            if (!it) binding.enterHarvestedBags.error = "Invalid"
        } && binding.inputSoldBags.text.isNullOrEmpty().not().also {
            if (!it) binding.enterSoldBags.error = "Invalid"
        } && binding.inputTotalRevenueAfterSales.text.isNullOrEmpty().not().also {
            if (!it) binding.enterRevenueAfterSales.error = "Invalid"
        }
    }
}