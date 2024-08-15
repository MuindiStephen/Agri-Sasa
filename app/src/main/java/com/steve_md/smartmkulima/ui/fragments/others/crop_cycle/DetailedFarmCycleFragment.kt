package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.adapter.DetailTaskAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleTasksAdapter
import com.steve_md.smartmkulima.databinding.FragmentDetailedFarmCycleBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailedFarmCycleFragment : Fragment() {
    private lateinit var binding: FragmentDetailedFarmCycleBinding
    private val tasksAdapter by lazy { LocalFarmCycleTasksAdapter() }

    // Navigation Args
    private val args: DetailedFarmCycleFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedFarmCycleBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()


        val setStatus =  binding.spinnerStatusOfFarmCycle.selectedItem.toString()
        viewModel.updateTaskStatus(setStatus)
        binding.textView86.text = setStatus

        val cycle = args.cycle
        binding.apply {
            // Link data with respective views


            showFarmId.text = ""
            ShowCropName.text = "Crop Name: ${cycle.cropName}"
            ShowStartDate.text = "Cycle start day: ${cycle.startDate}"
            textView78.text = "Crop cycle"

            Timber.tag(this@DetailedFarmCycleFragment.toString()).i("Viewing ${cycle.cropName} cycle")
            displaySnackBar("Viewing ${cycle.cropName} cycle")

            tasksAdapter.submitList(cycle.tasks)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = tasksAdapter
            }
        }
    }
}