package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.steve_md.smartmkulima.adapter.CropCycleTaskListAdapter
import com.steve_md.smartmkulima.databinding.FragmentCropCycleListBinding
import com.steve_md.smartmkulima.model.CropCycleTask
import timber.log.Timber
import java.net.HttpURLConnection

class CropCycleTasksListFragment : Fragment() {
    private lateinit var binding: FragmentCropCycleListBinding
    var cropCycleTaskList: ArrayList<CropCycleTask> = ArrayList()
    var cropCycleListAdapter: CropCycleTaskListAdapter? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropCycleListBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("crop_cycle_tasks")

        getAllAvailableCropCycle()
        setUpBinding()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.cropCycleRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            LinearLayoutManager.VERTICAL
        }

        // Initialize the adapter
        cropCycleListAdapter = CropCycleTaskListAdapter()

        // Set the adapter to the RecyclerView
        binding.cropCycleRecyclerView.adapter = cropCycleListAdapter
    }
    private fun setUpBinding() {
        binding.imageViewBackFromCropCycleLists.setOnClickListener { findNavController().navigateUp() }
    }

    private fun getAllAvailableCropCycle() {
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {

                        try {
                            val cropCycle: CropCycleTask? = i.getValue(CropCycleTask::class.java)

                            cropCycle?.let { cropCycleTaskList.add(it) }
                        } catch (e: Exception) {
                            Timber.d(e.localizedMessage)
                        }
                    }
                    cropCycleListAdapter = CropCycleTaskListAdapter()
                    cropCycleListAdapter!!.submitList(cropCycleTaskList)
                    binding.cropCycleRecyclerView.adapter = cropCycleListAdapter
                    Timber.tag(this.toString()).d("Alert! crop cycles available {}")
                } else {
                    Timber.tag(this.toString())
                        .d("No crop cycle found {}, an error occurred while fetching")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.tag(this.toString())
                    .d("an error occurred, try again!" +
                            "server connection failed" +
                            "${HttpURLConnection.HTTP_CLIENT_TIMEOUT}")
            }
        })
    }
}