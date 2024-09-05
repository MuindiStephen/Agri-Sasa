package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.fragmentadapter.FragmentAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleFarmRecordsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CropCycleFarmRecordsFragment : Fragment() {

    private lateinit var binding: FragmentCropCycleFarmRecordsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleFarmRecordsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpViewPager()
        setUpViews()
    }

    private fun setUpViews() {
        binding.apply {
            binding.btnRecordFarmExpenses.setOnClickListener {
                findNavController().navigate(R.id.cropCycleAddNewExpensesFragment)
            }
            binding.btnRecordAfterHarvest.setOnClickListener {
                findNavController().navigate(R.id.cropCycleAddRevenueGeneratedFragment)
            }
            binding.btnAnalytics.setOnClickListener {
                findNavController().navigate(R.id.cropCycleFinancialRecordsAnalyticsFragment)
            }
            binding.toolBarFarmRecords.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setUpViewPager() {
        val fragmentsAdapter = FragmentAdapter(childFragmentManager).apply {
            addFragment(CropCycleExpensesListFragment(), "Expenses")
            addFragment(CropCycleRevenuesListFragment(), "Revenues")
        }

        binding.apply {
            viewPager.adapter = fragmentsAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
    }
}