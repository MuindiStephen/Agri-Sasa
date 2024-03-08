package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.steve_md.smartmkulima.adapter.CropCycleTaskListAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleListBinding
import com.steve_md.smartmkulima.model.CropCycleTask
import com.steve_md.smartmkulima.model.Cycle
import timber.log.Timber
import java.net.HttpURLConnection

class CropCycleTasksListFragment : Fragment() {
    private lateinit var binding: FragmentCropCycleListBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var cycleListAdapter: CropCycleTaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        firestore = FirebaseFirestore.getInstance()

        setUpBinding()
        setUpRecyclerView()
        getAllAvailableCropCycle()
    }

    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.cropCycleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        cycleListAdapter = CropCycleTaskListAdapter()

        // Set the adapter to the RecyclerView
        binding.cropCycleRecyclerView.adapter = cycleListAdapter
    }

    private fun setUpBinding() {
        binding.imageViewBackFromCropCycleLists.setOnClickListener { findNavController().navigateUp() }
    }

    private fun getAllAvailableCropCycle() {
        firestore.collection("cycles")
            .get()
            .addOnSuccessListener { documents ->
                val cropCycleTaskList = mutableListOf<Cycle>()
                for (document in documents) {
                    try {
                        val cropCycle = document.toObject(Cycle::class.java)
                        cropCycleTaskList.add(cropCycle)
                    } catch (e: Exception) {
                        Timber.d(e.localizedMessage)
                    }
                }
                cycleListAdapter.submitList(cropCycleTaskList)
                Timber.d("Alert! crop cycles available: $cropCycleTaskList")
            }
            .addOnFailureListener { exception ->
                Timber.d("Error getting crop cycles: $exception")
            }
    }
}
