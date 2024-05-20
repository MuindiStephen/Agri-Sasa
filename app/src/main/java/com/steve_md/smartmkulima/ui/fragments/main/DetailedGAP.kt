package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.DetailGapAdapter
import com.steve_md.smartmkulima.databinding.FragmentDetailedGAPBinding
import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.utils.displaySnackBar
import timber.log.Timber


class DetailedGAP : Fragment() {

    private lateinit var binding: FragmentDetailedGAPBinding
    private val args: DetailedGAPArgs by navArgs()
    private val gapTaskAdapter by lazy { DetailGapAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedGAPBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (activity as AppCompatActivity).supportActionBar?.hide()
//
//
//        val gap = args.gap
//        binding.apply {
//            // Link data with respective views
//
//            showFarmId.text = gap.nameGAP
//            ShowCropName.visibility = View.GONE
//            ShowStartDate.visibility = View.GONE
//            textView78.visibility = View.INVISIBLE
//
//            Timber.tag(this@DetailedGAP.toString())
//                .i("Viewing ${gap.nameGAP} gap")
//            displaySnackBar("Viewing ${gap.nameGAP} gap")
//
//            gapTaskAdapter.submitList(gap.gap ?: emptyList())
//            recyclerView.apply {
//                layoutManager = LinearLayoutManager(requireContext())
//                adapter = gapTaskAdapter
//            }
//        }


        val gap = arguments?.getParcelable<GAP>("gap")
        if (gap != null) {
            binding.apply {
                showFarmId.text = gap.nameGAP
                ShowCropName.visibility = View.GONE
                ShowStartDate.visibility = View.GONE
                textView78.visibility = View.INVISIBLE

                Timber.tag(this@DetailedGAP.toString()).i("Viewing ${gap.nameGAP} gap")
                displaySnackBar("Viewing ${gap.nameGAP} gap")

                gapTaskAdapter.submitList(gap.gap ?: emptyList())
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
}
