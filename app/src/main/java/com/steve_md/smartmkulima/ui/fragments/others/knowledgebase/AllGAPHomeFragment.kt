package com.steve_md.smartmkulima.ui.fragments.others.knowledgebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.DetailGapAdapter
import com.steve_md.smartmkulima.databinding.FragmentAllGAPHomeBinding
import com.steve_md.smartmkulima.databinding.FragmentDetailedGAPBinding
import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.model.GAPtask
import com.steve_md.smartmkulima.ui.fragments.main.DetailedGAPArgs
import com.steve_md.smartmkulima.utils.displaySnackBar
import timber.log.Timber


/**
 * Show a detailed Good agricultural practice for each farm cycle
 */
class AllGAPHomeFragment : Fragment() {

    private lateinit var binding: FragmentAllGAPHomeBinding
    private val args: DetailedGAPArgs by navArgs()
    private val gapTaskAdapter by lazy { DetailGapAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllGAPHomeBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gap = arguments?.getParcelable<GAP>("gap")
        if (gap != null) {
            binding.apply {
                showFarmId.text = gap.nameGAP
                ShowCropName.text = "Good Agricultural Practice"
                ShowStartDate.visibility = View.GONE
                textView78.visibility = View.INVISIBLE
                spinnerStatusOfFarmCycle.visibility = View.GONE

                Timber.tag(this@AllGAPHomeFragment.toString()).i("Viewing ${gap.nameGAP} gap")
                displaySnackBar("Viewing ${gap.nameGAP} gap")

                gapTaskAdapter.submitList(gap.gap ?: getDefaultGapList())
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = gapTaskAdapter
                }
            }
        } else {
            Timber.e("GAP data is null")
            displaySnackBar("Error loading GAP details")
        }
    }

    private fun getDefaultGapList(): List<GAPtask> {
        return listOf(
            GAPtask("Seedling Acquisition", "1", "1"),
            GAPtask("Media Acquisition", "1", "1"),
            GAPtask("Media washing and Treatment", "2", "2"),
            GAPtask("Preparation of grow trays (washing and sterilizing)/potting of media", "3", "3"),
            GAPtask("Loading of treated media and seedlings", "4", "5"),
            GAPtask("Seed sowing", "5", "5"),
            GAPtask("Watering and fertigation", "6", "48"),
            GAPtask("Hardening", "42", "48"),
            GAPtask("Land preparation", "41", "48"),
            GAPtask("Manure Application", "41", "48"),
            GAPtask("bed making/gypsum application", "42", "44"),
            GAPtask("Flushing with plain water", "45", "44"),
            GAPtask("Transplanting", "46", "46"),
            GAPtask("Trellising", "47", "47"),
            GAPtask("Prunning suckers", "48", "48"),
            GAPtask("Fertilizer Application", "49", "49")
        )
    }
}