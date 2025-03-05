package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.FarmMachineryAdapter
import com.steve_md.smartmkulima.databinding.FragmentOfflineFarmMachineryBinding
import com.steve_md.smartmkulima.model.FarmMachinery
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class OfflineFarmMachineryFragment : Fragment() {

    private lateinit var binding: FragmentOfflineFarmMachineryBinding
    private val viewModel: MainViewModel  by viewModels()
    private lateinit var adapter: FarmMachineryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentOfflineFarmMachineryBinding.inflate(
            inflater, container, false
        )

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvOfflineFarmMachinery)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        adapter = FarmMachineryAdapter(machineryList = dummyList())
        Log.d("OfflineFarmMachinery","FM: ${dummyList()}")
        Log.d("OfflineFarmMachinery","FM: ${dummyList()}")

        recyclerView?.adapter = adapter

        viewModel.allMachinery.observe(viewLifecycleOwner) { machineryList ->
//            adapter = FarmMachineryAdapter(dummyList())
//            Log.d("OfflineFarmMachinery","FM: ${dummyList()}")
//            recyclerView?.adapter = adapter
        }

        return binding.root
    }

    private fun dummyList(): List<FarmMachinery> = listOf(
        FarmMachinery(
            "1",
            "Tractor",
            "Heavy and quality tractor",
            1000.00,
            "",
            false
        ),
        FarmMachinery(
            "1",
            "Jembe",
            "quality jembe",
            120.00,
            "",
            false
        ),
        )
    }
