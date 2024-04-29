package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.CropCycleTaskListAdapter
import com.steve_md.smartmkulima.data.remote.CyclesApiClient
import com.steve_md.smartmkulima.data.remote.FarmEquipmentsApiClient
import com.steve_md.smartmkulima.databinding.FragmentCropCycleListBinding
import com.steve_md.smartmkulima.model.CropCycleTask
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.FarmEquipment
import com.steve_md.smartmkulima.ui.fragments.main.HomeDashboardFragmentDirections
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection
import java.util.logging.Handler

class CropCycleTasksListFragment : Fragment() {
    private lateinit var binding: FragmentCropCycleListBinding
    private lateinit var cycleListAdapter: CropCycleTaskListAdapter
    private var cycleList = ArrayList<Cycle>()

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

        setUpBinding()
        setUpRecyclerView()
        getAllAvailableCropCycle()
    }

    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.cropCycleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        cycleListAdapter = CropCycleTaskListAdapter(CropCycleTaskListAdapter.OnClickListener { cycle ->
            Timber.i("=====Checking=======>: ${cycle.cropName} cycle")

            val directions = CropCycleTasksListFragmentDirections.actionCropCycleTasksListFragmentToDetailedFarmCycleFragment(
                cycle
            )
            findNavController().navigate(directions)

        })

        // Set the adapter to the RecyclerView
        binding.cropCycleRecyclerView.adapter = cycleListAdapter
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpBinding() {
        binding.imageViewBackFromCropCycleLists.setOnClickListener { findNavController().navigateUp() }

        binding.textView74.setOnClickListener {
           // binding.cropCycleRecyclerView.removeAllViews()
            binding.progressBarCycles.visibility = View.VISIBLE
            android.os.Handler().postDelayed({
                binding.progressBarCycles.visibility = View.GONE
//                getAllAvailableCropCycle()
            }, 1000)
        }

        binding.textView83CropCycle.setOnClickListener {
            //binding.cropCycleRecyclerView.removeAllViews()
            filterCycles("Crop cycle")
        }
        binding.textView84.setOnClickListener {
           // binding.cropCycleRecyclerView.removeAllViews()
            filterCycles("Livestock cycle")
        }

        binding.searchProduct.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hideKeyboard()

                val searchText = binding.searchView.editText?.text.toString().trim()

                if (searchText.isEmpty()) {
                    toast("Enter some text in order to search")
                    false
                }
                searching(searchText)
                true
            } else {
                false
            }

        }

        binding.searchView.setEndIconOnClickListener {
            hideKeyboard()

            if (binding.searchView.editText?.text.isNullOrEmpty()) {
                return@setEndIconOnClickListener
            }

            binding.searchView.editText?.setText("")
            getAllAvailableCropCycle()
        }

    }

    // Fetch from remote API (web-service)
    private fun getAllAvailableCropCycle() {
        CyclesApiClient.api.getAllFarmCycles()
            .enqueue(object : retrofit2.Callback<ArrayList<Cycle>> {
                @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
                override fun onResponse(
                    call: Call<ArrayList<Cycle>>,
                    response: Response<ArrayList<Cycle>>
                ) {
                    if (response.isSuccessful) {

                        Timber.i("==== Viewing Farm cycles${response.body()}=====")
                       // displaySnackBar("Viewing Available cycles")

                        val cycles = response.body()

                        cycles?.let {
                            cycleList.addAll(it)
                            cycleListAdapter.submitList(cycleList)
                        }
                        cycleListAdapter.notifyDataSetChanged()
                        binding.cropCycleRecyclerView.adapter = cycleListAdapter
                        binding.cropCycleRecyclerView.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<ArrayList<Cycle>>, t: Throwable) {
                    Timber.e("nothing here.${t.localizedMessage}")
                    binding.errorNotAvailable.visibility = View.VISIBLE
                    binding.cropCycleRecyclerView.visibility = View.INVISIBLE
                }
            })
    }

    private fun searching(s: String) {
        val filteredList = cycleList.filter { it.farmId.equals(s, ignoreCase = true) }
        cycleListAdapter.submitList(filteredList.toMutableList())
    }
    private fun filterCycles(s: String) {
        val filteredList = cycleList.filter { it.type.equals(s, ignoreCase = true) }
        cycleListAdapter.submitList(filteredList.toMutableList())
    }
}
