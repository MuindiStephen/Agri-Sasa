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
import com.steve_md.smartmkulima.adapter.FarmFieldsAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleExpensesListBinding
import com.steve_md.smartmkulima.databinding.FragmentCropCycleRevenuesListBinding
import com.steve_md.smartmkulima.model.FarmEquipment
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CropCycleExpensesListFragment : Fragment() {

    private lateinit var binding: FragmentCropCycleExpensesListBinding
    private val viewModel: MainViewModel by viewModels()
    private val farmExpensesRecAdapter by lazy { FarmExpensesRecordsAdapter() }
    private var farmExpRecordsList =  mutableListOf<FarmFinanceExpenseRecords>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleExpensesListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
        fetchAllExpenseRecords()
    }

    private fun fetchAllExpenseRecords() {
        lifecycleScope.launch {
            viewModel.allFarmCycleExp.observe(viewLifecycleOwner) { expenses ->
                if (!expenses.isNullOrEmpty()) {
                    farmExpRecordsList.clear()
                    farmExpRecordsList.addAll(expenses)
                    farmExpensesRecAdapter.submitList(farmExpRecordsList)
                    binding.textViewNoExpensesRecordss.isVisible = false

                    binding.recyclerViewExpenses.adapter = farmExpensesRecAdapter

                } else {
                    binding.textViewNoExpensesRecordss.isVisible = true
                }
            }
        }

    }


    private fun setUpUi() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}