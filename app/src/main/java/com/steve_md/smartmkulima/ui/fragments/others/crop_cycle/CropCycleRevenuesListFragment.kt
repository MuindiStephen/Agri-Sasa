package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.FarmExpensesRecordsAdapter
import com.steve_md.smartmkulima.adapter.FarmRevenuesRecordsAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleRevenuesListBinding
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CropCycleRevenuesListFragment : Fragment() {

    private lateinit var binding: FragmentCropCycleRevenuesListBinding
    private val viewModel: MainViewModel by viewModels()
    private val farmRevenuesRecordsAdapter by lazy { FarmRevenuesRecordsAdapter() }
    private var farmRevRecordsList =  mutableListOf<FarmFinanceRevenueRecords>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleRevenuesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
        fetchAllRevenuesList()
    }

    private fun fetchAllRevenuesList() {
        lifecycleScope.launch {
            viewModel.allFarmCycleRevenues.observe(viewLifecycleOwner) { expenses ->
                if (!expenses.isNullOrEmpty()) {
                    farmRevRecordsList.clear()
                    farmRevRecordsList.addAll(expenses)
                    farmRevenuesRecordsAdapter.submitList(farmRevRecordsList)
                    binding.textViewNoRevenueRecords.isVisible = false

                    binding.recyclerViewRevenues.adapter = farmRevenuesRecordsAdapter

                } else {
                    binding.textViewNoRevenueRecords.isVisible = true
                }
            }
        }
    }

    private fun setUpUi() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}