package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Inflate the layout for this fragment
        binding = FragmentCropCycleFinancialRecordsAnalyticsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        setUpRecyclerView()

        fetchAllFarmSummaryRecordsAvailable()
    }

    private fun setUpRecyclerView() {

        // Set the layout manager
        binding.recyclerView3.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        farmAnalyticsRecordListAdapter = FarmAnalyticsRecordListAdapter(FarmAnalyticsRecordListAdapter.OnClickListener{ farmFinancialSummary ->
            Timber.tag("...CreatedFarmCycles....").e(farmFinancialSummary.toString())

            Timber.i("=====Checking=======>: ${farmFinancialSummary.nameOfCropCycle} cycle")

            findNavController().navigate(
                R.id.viewFarmFinancialRecordsSummaryInDetailsFragment,
                bundleOf (
                    Pair("cropCycleName","${farmFinancialSummary.nameOfCropCycle}"),
                    Pair("totalInputs","${farmFinancialSummary.totalInputCosts}"),
                    Pair("totalSales","${farmFinancialSummary.totalRevenueGenerated}")
                )
            )
        })
        // Set the adapter to the RecyclerView
        binding.recyclerView3.adapter = farmAnalyticsRecordListAdapter
    }

    private fun fetchAllFarmSummaryRecordsAvailable() {
        lifecycleScope.launch {
            viewModel.allSummaryRecords.observe(viewLifecycleOwner) { summary ->
                if (summary.isNotEmpty()) {
                    binding.textViewNoFinancialRecords.isVisible = false
                    farmSummaryList.clear()
                    farmSummaryList.addAll(summary)
                    binding.recyclerView3.adapter = farmAnalyticsRecordListAdapter
                } else {
                    binding.textViewNoFinancialRecords.isVisible = true
                }
            }
        }
    }

    private fun setUpUi() {
        binding.toolbarCropCycleAnalytics.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}