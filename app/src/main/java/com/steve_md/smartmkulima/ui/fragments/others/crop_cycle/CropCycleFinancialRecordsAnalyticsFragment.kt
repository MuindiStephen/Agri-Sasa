package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.FarmAnalyticsRecordListAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleFinancialRecordsAnalyticsBinding
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinancialDataSummary
import com.steve_md.smartmkulima.ui.activities.DetailedFarmCycleActivity
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CropCycleFinancialRecordsAnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentCropCycleFinancialRecordsAnalyticsBinding
    private lateinit var farmAnalyticsRecordListAdapter: FarmAnalyticsRecordListAdapter
    private var farmSummaryList =  mutableListOf<FarmFinancialDataSummary>()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleFinancialRecordsAnalyticsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        fetchFirstAvailableCropCycles()

        setUpRecyclerView()

        fetchAllFarmSummaryRecordsAvailable()
    }

    private fun setUpRecyclerView() {

        // Set the layout manager
        binding.recyclerView3.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        farmAnalyticsRecordListAdapter = FarmAnalyticsRecordListAdapter(

            FarmAnalyticsRecordListAdapter.OnClickListener { farmFinancialSummary ->
            Timber.tag("...Created-Farm-Financial-Records....").e(farmFinancialSummary.toString())

            Timber.i("=====Checking=======>: ${farmFinancialSummary.nameOfCropCycle} cycle")

              val directions =  CropCycleFinancialRecordsAnalyticsFragmentDirections
                  .actionCropCycleFinancialRecordsAnalyticsFragmentToViewFarmFinancialRecordsSummaryInDetailsFragment(farmFinancialSummary)

              findNavController().navigate(directions)

            /*
            findNavController().navigate(
                R.id.viewFarmFinancialRecordsSummaryInDetailsFragment,
                bundleOf(
                    Pair("cropCycleName","${farmFinancialSummary.nameOfCropCycle}"),
                    Pair("totalInputs","${farmFinancialSummary.totalInputCosts}"),
                    Pair("totalSales","${farmFinancialSummary.totalRevenueGenerated}")
                )
            )

             */
        })
    }

    private fun fetchAllFarmSummaryRecordsAvailable() {
        lifecycleScope.launch {
            viewModel.allSummaryRecords.observe(viewLifecycleOwner) { summary ->
                if (summary.isNotEmpty()) {
                    binding.textViewNoFinancialRecords.isVisible = false
                    farmSummaryList.clear()
                    farmSummaryList.addAll(summary)
                    farmAnalyticsRecordListAdapter.submitList(farmSummaryList)
                    binding.recyclerView3.adapter = farmAnalyticsRecordListAdapter
                } else {
                    binding.textViewNoFinancialRecords.isVisible = true
                }
            }
        }
    }


    // submit this list to the spinner
    private fun fetchFirstAvailableCropCycles() {

        lifecycleScope.launch {
            viewModel.allCycles.observe(viewLifecycleOwner) { cycles ->
                if (cycles.isNotEmpty()) {

                    val allCropCycles = cycles.map {
                        it.cropName
                    }

                    val adapter = ArrayAdapter<String>(this@CropCycleFinancialRecordsAnalyticsFragment.requireContext(),
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

    private fun setUpUi() {
        binding.toolbarCropCycleAnalytics.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.buttonConfirmFinancialAnalytics.setOnClickListener {

            if (validateInputs()) {
                val summary = FarmFinancialDataSummary (
                    nameOfCropCycle = binding.spinnerNameOfCycle.selectedItem.toString(),
                    totalInputCosts = binding.inputTotalCosts.text.toString(),
                    totalRevenueGenerated = binding.inputSales.text.toString()
                )
                lifecycleScope.launch {
                    try {
                        viewModel.addFarmSummaryRecords(summary)
                        displaySnackBar("New Farm Record for Analysis Created")
                        Timber.d("Request Succeeded")
                    } catch (e: Exception) {
                        displaySnackBar("Your Request Has Failed.")
                        Timber.d("Request Failed.")
                    }
                }
            }
        }
    }
    private fun validateInputs(): Boolean {
        return binding.inputTotalCosts.text.isNullOrEmpty().not().also {
            if (!it) binding.enterTotalCosts.error = "Invalid"
        } && binding.inputSales.text.isNullOrEmpty().not().also {
            if (!it) binding.enterSales.error = "Invalid"
        }
    }
}