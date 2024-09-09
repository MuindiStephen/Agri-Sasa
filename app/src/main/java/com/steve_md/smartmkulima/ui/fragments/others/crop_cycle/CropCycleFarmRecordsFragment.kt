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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.fragmentadapter.FragmentAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleFarmRecordsBinding
import com.steve_md.smartmkulima.model.FarmEquipment
import com.steve_md.smartmkulima.utils.Event
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Entry to work with records ie. expenses, revenues, summary records
 * and deleting records
 */
@AndroidEntryPoint
class CropCycleFarmRecordsFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
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

            // Deleting All Records or Clearing the records
            binding.btnClearAllRecords.setOnClickListener {

                lifecycleScope.launch(Dispatchers.Main) {
                    viewModel.deleteFarmCycleExpense()
                    viewModel.deleteFarmCycleRevenue()
                    viewModel.deleteFarmSummaryRecords()
                }

                showProgressDeletingRecordsStatus()

                hideAllRecyclerViews()
            }
        }
    }

    private fun hideAllRecyclerViews() {
        view?.findViewById<RecyclerView>(R.id.recyclerView3)?.visibility = View.GONE
        view?.findViewById<RecyclerView>(R.id.recyclerView3)?.removeAllViews()

        view?.findViewById<RecyclerView>(R.id.recyclerViewExpenses)?.visibility = View.GONE
        view?.findViewById<RecyclerView>(R.id.recyclerViewExpenses)?.removeAllViews()

        view?.findViewById<RecyclerView>(R.id.recyclerViewRevenues)?.visibility = View.GONE
        view?.findViewById<RecyclerView>(R.id.recyclerViewExpenses)?.removeAllViews()
    }

    /**
     * Showing status to deleting / clearing the records
     */
    private fun showProgressDeletingRecordsStatus() {

        lifecycleScope.launch {
            binding.progressBarLoadingClearRecords.visibility = View.VISIBLE
            delay(4000L)
            binding.progressBarLoadingClearRecords.visibility = View.GONE

            requireActivity().runOnUiThread {
                displaySnackBar("All Records Successfully Cleared.")
            }
        }
    }

    /**
     * Setting up the tab layout with respective fragments
     */
    private fun setUpViewPager() {
        val fragmentsAdapter = FragmentAdapter(childFragmentManager).apply {
            addFragment(CropCycleExpensesListFragment (),"Expenses")
            addFragment(CropCycleRevenuesListFragment(), "Revenues")
        }

        binding.apply {
            viewPager.adapter = fragmentsAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
    }
}