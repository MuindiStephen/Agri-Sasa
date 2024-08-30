package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.FarmFieldsAdapter
import com.steve_md.smartmkulima.databinding.FragmentAddNewFarmFieldBinding
import com.steve_md.smartmkulima.databinding.FragmentAddedNewFarmFieldsBinding
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 * Added Farm field
 */
@AndroidEntryPoint
class AddedNewFarmFieldsFragment : Fragment() {
    private lateinit var binding: FragmentAddedNewFarmFieldsBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var farmFieldsAdapter: FarmFieldsAdapter
    private var farmFieldsList =  mutableListOf<NewFarmField>()
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddedNewFarmFieldsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpBinding()

        setUpFarmFieldsRecyclerView()
        fetchAllFarmFields()
    }

    private fun setUpFarmFieldsRecyclerView() {
        binding.createdFarmFieldsRecView.layoutManager = LinearLayoutManager(requireContext())

        farmFieldsAdapter = FarmFieldsAdapter(FarmFieldsAdapter.OnClickListener { newFarmField ->
            Timber.i("==Clicked on farm field==: ${newFarmField.farmName}")

            // navigate to farm monitoring
            findNavController().navigate(
                R.id.monitorFarmConditionFragment,
                bundleOf(Pair("farmfield",newFarmField))
            )
        })

        binding.createdFarmFieldsRecView.adapter = farmFieldsAdapter
    }

    private fun fetchAllFarmFields() {
        viewModel.allFarmFields.observe(viewLifecycleOwner) { farmField->
            if (!farmField.isNullOrEmpty()) {
                farmFieldsList.clear()
                farmFieldsList.addAll(farmField)
                farmFieldsAdapter.submitList(farmFieldsList)
                binding.textViewNoFarmsAvailable.isVisible = false

                binding.createdFarmFieldsRecView.adapter = farmFieldsAdapter

                // set the counted number of items in the rec view
                binding.textViewFarmFieldInitialLr.text = farmFieldsList.size.toString()
            } else {
                binding.textViewNoFarmsAvailable.isVisible = true
            }
        }
    }

    private fun setUpBinding() {
        binding.apply {
            buttonAddNewFarmField.setOnClickListener {
                findNavController().navigate(R.id.addNewFarmFieldFragment)
            }
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireFragmentManager().backStackEntryCount == 0) {
                    requireActivity().finishAffinity()
                } else {
                    val fragmentManager = requireActivity().supportFragmentManager
                    if (fragmentManager.backStackEntryCount > 0) {
                        val first = fragmentManager.getBackStackEntryAt(0)
                        fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    findNavController().navigateUp()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (onBackPressedCallback != null) {
            onBackPressedCallback!!.isEnabled = false
            onBackPressedCallback!!.remove()
        } else{
            onBackPressedCallback?.isEnabled ?: true
            onBackPressedCallback?.remove()
        }
    }

}

