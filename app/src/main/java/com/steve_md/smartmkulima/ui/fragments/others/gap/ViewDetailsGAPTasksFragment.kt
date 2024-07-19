package com.steve_md.smartmkulima.ui.fragments.others.gap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.DetailGapAdapter
import com.steve_md.smartmkulima.adapter.DetailTaskAdapter
import com.steve_md.smartmkulima.databinding.FragmentViewDetailsGAPTasksBinding
import com.steve_md.smartmkulima.ui.fragments.main.DetailedGAPArgs
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.DetailedFarmCycleFragmentArgs
import com.steve_md.smartmkulima.utils.displaySnackBar
import timber.log.Timber


class ViewDetailsGAPTasksFragment : Fragment() {

    private lateinit var binding: FragmentViewDetailsGAPTasksBinding
    private val detailedGapAdapter by lazy { DetailGapAdapter() }

    // Navigation Args
    private val args: ViewDetailsGAPTasksFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewDetailsGAPTasksBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()


        val gap = args.gap
        binding.apply {
            // Link data with respective views

            showFarmId.text = gap.nameGAP
            ShowCropName.text = "Good Agricultural Practice"
            ShowStartDate.visibility = View.GONE
            textView78.visibility = View.INVISIBLE
            spinnerStatusOfFarmCycle.visibility = View.GONE

            Timber.tag("Detailed===>GAP").i("Viewing ${gap.nameGAP} cycle")
            displaySnackBar("Viewing ${gap.nameGAP} cycle")

            detailedGapAdapter.submitList(gap.gap)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = detailedGapAdapter
            }
        }
    }
}