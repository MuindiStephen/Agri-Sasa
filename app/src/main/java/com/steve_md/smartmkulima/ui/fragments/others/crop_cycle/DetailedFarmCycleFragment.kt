package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.adapter.DetailTaskAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleTasksAdapter
import com.steve_md.smartmkulima.databinding.FragmentDetailedFarmCycleBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import timber.log.Timber

class DetailedFarmCycleFragment : Fragment() {
    private lateinit var binding: FragmentDetailedFarmCycleBinding
    private val tasksAdapter by lazy { LocalFarmCycleTasksAdapter() }

    // Navigation Args
    private val args: DetailedFarmCycleFragmentArgs by navArgs()

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


        val cycle = args.cycle
        binding.apply {
            // Link data with respective views

            showFarmId.text = ""
            ShowCropName.text = cycle.cropName
            ShowStartDate.text = cycle.startDate
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