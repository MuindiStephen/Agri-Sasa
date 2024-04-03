package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.steve_md.smartmkulima.databinding.FragmentDetailedFarmCycleBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import timber.log.Timber

class DetailedFarmCycleFragment : Fragment() {
    private lateinit var binding: FragmentDetailedFarmCycleBinding

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


            Timber.i("\n"+"=====Viewing farm cycle====> {} ",cycle.farmId + cycle.cropName + cycle.startDate+"\n\n")
            displaySnackBar("You are viewing Farm Cycle for ${cycle.cropName}")

        }
    }
}