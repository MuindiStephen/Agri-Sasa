package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.FarmEquipmentAdapter
import com.steve_md.smartmkulima.data.remote.FarmEquipmentsApiClient
import com.steve_md.smartmkulima.databinding.FragmentHireFarmEquipmentsBinding
import com.steve_md.smartmkulima.model.FarmEquipment
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber


@AndroidEntryPoint
class HireFarmEquipmentsFragment : Fragment() {

    private lateinit var binding: FragmentHireFarmEquipmentsBinding
    private lateinit var farmEquipmentsRecylerView: RecyclerView
    private lateinit var farmEquipmentsAdapter: FarmEquipmentAdapter
    private var farmEquipmentsList = ArrayList<FarmEquipment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHireFarmEquipmentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        farmEquipmentsRecylerView = view.findViewById(R.id.farmEquipmentsRecyclerView)

        setUpBinding()

        /**
         * Passing @param FarmEquipment as an argument
         */
        farmEquipmentsAdapter =
            FarmEquipmentAdapter(FarmEquipmentAdapter.OnClickListener { farmEquipment ->
                Timber.i("Farm Equipments: ${farmEquipment.name}")

                val action =
                    HireFarmEquipmentsFragmentDirections.actionHireFarmEquipmentsFragmentToFarmEquipmentDetailsFragment(
                        farmEquipmentItem = farmEquipment
                    )
                findNavController().navigate(action)
            })
        /**
         * Get a List of all available Farm Equipments
         */
        FarmEquipmentsApiClient.api.getAllEquipments()
            .enqueue(object : retrofit2.Callback<ArrayList<FarmEquipment>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<FarmEquipment>>,
                    response: Response<ArrayList<FarmEquipment>>
                ) {
                    if (response.isSuccessful) {

                        Timber.i("Available Farm Equipments: ${response.body()}")
                        displaySnackBar("Farm Equipments for hire are available")

                        val farmEquipments = response.body()

                        farmEquipments?.let {
                            farmEquipmentsList.addAll(it)
                            farmEquipmentsAdapter.submitList(farmEquipmentsList)
                        }

                        farmEquipmentsAdapter.notifyDataSetChanged()
                        farmEquipmentsRecylerView.adapter = farmEquipmentsAdapter
                        farmEquipmentsRecylerView.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<ArrayList<FarmEquipment>>, t: Throwable) {
                    toast("No available Farm Equipments for now! \nConsider checking your network connection")
                    binding.textViewError.visibility = View.VISIBLE
                    binding.farmEquipmentsRecyclerView.visibility = View.INVISIBLE
                }
            })
    }
    private fun setUpBinding() {
        binding.imageViewBackFromFarmEquipments.setOnClickListener {
            findNavController().navigateUp()
        }

        // performing filtering of farm equipments here
        binding.card1.setOnClickListener {
            filterFarmEquipments("F")
        }
        binding.card2.setOnClickListener { filterFarmEquipments("W") }
        binding.card3.setOnClickListener { filterFarmEquipments("J") }
        binding.card4.setOnClickListener { filterFarmEquipments("P") }
    }

    private fun filterFarmEquipments(query: String) {
        val filteredList = farmEquipmentsList.filter { it.name.contains(query, ignoreCase = true) }
        farmEquipmentsAdapter.submitList(filteredList.toMutableList())
    }
}