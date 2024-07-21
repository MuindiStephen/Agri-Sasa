package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.adapter.CropCycleTaskListAdapter
import com.steve_md.smartmkulima.adapter.others.LocalFarmCycleAdapter
import com.steve_md.smartmkulima.data.remote.CyclesApiClient
import com.steve_md.smartmkulima.databinding.FragmentCropCycleListBinding
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

/**
 *
 * Hey!
 * This code looks Junk :(
 *  @author MuindiStephen - Github
 *
 *  @year 2024
 *
 *  Was written in hurry to solve an issue
 *  & to implement a feature in urgency
 *
 *
 *
 *
 */
@AndroidEntryPoint
class CropCycleTasksListFragment : Fragment() {
    private lateinit var binding: FragmentCropCycleListBinding
    //private lateinit var cycleListAdapter: CropCycleTaskListAdapter
    private var cycleList =  mutableListOf<LocalFarmCycle>()

    private lateinit var localFarmCycleAdapter: LocalFarmCycleAdapter

    private val viewModel: MainViewModel by viewModels()

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

        binding.imageViewBackFromCropCycleLists.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpBinding()
        setUpRecyclerView()
        // getAllAvailableCropCycle()

        getAllCreatedCycles()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllCreatedCycles() {
        /**viewModel.allCycles.observe(viewLifecycleOwner){ it ->
            when(it.isEmpty()) {
                true -> {
                    displaySnackBar("No created farm cycles available")
                }
                false -> {
                    it?.let {
                        cycleList.addAll(it)
                        localFarmCycleAdapter.submitList(cycleList)
                    }
                    localFarmCycleAdapter.notifyDataSetChanged()
                    binding.cropCycleRecyclerView.adapter = localFarmCycleAdapter
                    binding.cropCycleRecyclerView.visibility = View.VISIBLE

                    displaySnackBar("Alert! Created farm cycles")
                }
            }
        }
        */

        viewModel.allCycles.observe(viewLifecycleOwner) { cycles ->
            if (cycles.isNullOrEmpty()) {
                displaySnackBar("No created farm cycles available")
            } else {
                cycleList.clear()
                cycleList.addAll(cycles)
                localFarmCycleAdapter.submitList(cycleList.toList())
            }
        }
    }

    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.cropCycleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter

        localFarmCycleAdapter = LocalFarmCycleAdapter(LocalFarmCycleAdapter.OnClickListener{ cycle->
            Log.e("...CreatedFarmCycles....", cycle.toString())

            Timber.i("=====Checking=======>: ${cycle.cropName} cycle")

            val directions = CropCycleTasksListFragmentDirections
                .actionCropCycleTasksListFragmentToDetailedFarmCycleFragment(cycle)
            findNavController().navigate(directions)
        })

        // Set the adapter to the RecyclerView
        binding.cropCycleRecyclerView.adapter = localFarmCycleAdapter
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpBinding() {
       // binding.imageViewBackFromCropCycleLists.setOnClickListener { findNavController().navigateUp() }

        binding.textView84.isVisible = false
        binding.textView74.setOnClickListener {
            // binding.cropCycleRecyclerView.removeAllViews()
            binding.progressBarCycles.visibility = View.VISIBLE
            android.os.Handler().postDelayed({
                binding.progressBarCycles.visibility = View.GONE
//                getAllAvailableCropCycle()
            }, 1000)
        }

        binding.textView83CropCycle.isVisible = false
        binding.textView83CropCycle.setOnClickListener {
            //binding.cropCycleRecyclerView.removeAllViews()
            filterCycles("")
        }

        binding.textView84.isVisible = false
        binding.textView84.setOnClickListener {
            // binding.cropCycleRecyclerView.removeAllViews()
            filterCycles("")
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
            getAllCreatedCycles()
        }

    }
    private fun getAllAvailableCropCycle() {
        // fetch from api(remote web service)
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

//                        cycles?.let {
//                            cycleList.addAll(it)
//                            cycleListAdapter.submitList(cycleList)
//                        }
//                        cycleListAdapter.notifyDataSetChanged()
//                        binding.cropCycleRecyclerView.adapter = cycleListAdapter
//                        binding.cropCycleRecyclerView.visibility = View.VISIBLE
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
        val filteredList = cycleList.filter { it.cropName.equals(s, ignoreCase = true) }
        localFarmCycleAdapter.submitList(filteredList.toMutableList())
    }
    private fun filterCycles(s: String) {
        val filteredList = cycleList.filter { it.farmName.equals(s, ignoreCase = true) }
        localFarmCycleAdapter.submitList(filteredList.toMutableList())
    }
}
