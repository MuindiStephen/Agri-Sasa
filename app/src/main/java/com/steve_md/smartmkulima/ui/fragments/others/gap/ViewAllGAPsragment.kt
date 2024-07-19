package com.steve_md.smartmkulima.ui.fragments.others.gap

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.Gap
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.CropCycleTaskListAdapter
import com.steve_md.smartmkulima.adapter.GapAdapter
import com.steve_md.smartmkulima.data.remote.GapApiClient
import com.steve_md.smartmkulima.databinding.FragmentViewAllGAPsragmentBinding
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.ui.fragments.others.crop_cycle.CropCycleTasksListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

/**
 * This screen will list all Good Agricultural Practices(GAPs)
 * nameGAP, startDate, endDate and the task list -> taskName
 */
@AndroidEntryPoint
class ViewAllGAPsragment : Fragment() {

    private lateinit var binding: FragmentViewAllGAPsragmentBinding
    private lateinit var cycleListAdapter: GapAdapter
    private var gapList = ArrayList<GAP>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewAllGAPsragmentBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpBinding()
        setUpRecyclerView()
        getGoodAgriculturalPractices()
    }
    private fun setUpRecyclerView() {
        // Set the layout manager
        binding.allGAPRecViewAll.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        cycleListAdapter = GapAdapter(GapAdapter.OnClickListener { gap ->
            Timber.i("=====Checking=======>: ${gap.nameGAP} cycle")

            val dir = ViewAllGAPsragmentDirections.actionViewAllGAPsragmentToViewDetailsGAPTasksFragment(gap)
            findNavController().navigate(directions = dir)

        })

        // Set the adapter to the RecyclerView
        binding.allGAPRecViewAll.adapter = cycleListAdapter
    }

    private fun setUpBinding() {

    }

    private fun getGoodAgriculturalPractices() {
        GapApiClient.api.getAllGAPs()
            .enqueue(object : retrofit2.Callback<java.util.ArrayList<GAP>> {
                @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
                override fun onResponse(
                    call: Call<java.util.ArrayList<GAP>>,
                    response: Response<java.util.ArrayList<GAP>>
                ) {
                    if (response.isSuccessful) {

                        Timber.i("==== Viewing Good Agri. practices${response.body()}=====")
                        // displaySnackBar("Viewing Available cycles")

                        val gaps = response.body()

                        gaps?.let {
                            gapList.addAll(it)
                            cycleListAdapter.submitList(gapList)
                        }
                        cycleListAdapter.notifyDataSetChanged()
                        binding.allGAPRecViewAll.adapter = cycleListAdapter
                        binding.allGAPRecViewAll.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<java.util.ArrayList<GAP>>, t: Throwable) {
                    Timber.e("nothing here.${t.localizedMessage}")
                }
            })
    }
}