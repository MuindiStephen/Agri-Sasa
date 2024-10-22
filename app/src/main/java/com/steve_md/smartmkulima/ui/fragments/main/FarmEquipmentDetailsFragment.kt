package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentFarmEquipmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FarmEquipmentDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentFarmEquipmentDetailsBinding
    private val args: FarmEquipmentDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmEquipmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        val equipment = args.farmEquipmentItem

        binding.apply {
            Glide.with(requireContext())
                .load(equipment.imageUrl)
                .into(imageView18)
            equipmentImageText.text = "Equipment Name"+equipment.name
            equipmentYear.text = "Year of manufacture:"+equipment.year
            costEquipmentHire.text = "Equipment Price"+equipment.priceHire
        }

        setUpBinding()
    }

    private fun setUpBinding() {
        binding.buttonRequestHire.setOnClickListener {
            findNavController().navigate(R.id.action_farmEquipmentDetailsFragment_to_paymentEquipmentFragment)
        }
    }
}