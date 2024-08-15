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
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 * The Receiving Fragment with ->
 * LocalFarmCycle Parcelable data class as argument
 */
@AndroidEntryPoint
class DetailedFarmCycleFragment : Fragment() {
    private lateinit var binding: FragmentDetailedFarmCycleBinding
    private val tasksAdapter by lazy { LocalFarmCycleTasksAdapter() }

    // Navigation Args
    private val args: DetailedFarmCycleFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels()
    private var localFarmCycle: LocalFarmCycle? = null

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

        arguments?.let {
            localFarmCycle = it.getParcelable("localFarmCycle") // Use the argument name defined in the previous fragment
        }

        val setStatus =  binding.spinnerStatusOfFarmCycle.selectedItem.toString()
        viewModel.updateTaskStatus(setStatus)
        binding.textView86.text = setStatus

       // val cycle = args.cycle
        binding.apply {

            // Link data with respective views
            showFarmId.text = ""
            ShowCropName.text = "Crop: ${localFarmCycle?.cropName}"
            ShowStartDate.text = "Start day: ${localFarmCycle?.startDate}"
            textView78.text = "."

            Timber.tag(this@DetailedFarmCycleFragment.toString()).i("Viewing ${localFarmCycle?.cropName} cycle")
            displaySnackBar("Viewing ${localFarmCycle?.cropName} cycle")

            tasksAdapter.submitList(localFarmCycle!!.tasks)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = tasksAdapter
            }
        }
    }
}