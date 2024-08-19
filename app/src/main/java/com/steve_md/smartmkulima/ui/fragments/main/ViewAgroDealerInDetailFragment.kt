package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentViewAgroDealerInDetailBinding

/**
 * Viewing Agro-Dealer in Detail
 */
class ViewAgroDealerInDetailFragment : Fragment() {

    private lateinit var binding: FragmentViewAgroDealerInDetailBinding
    private val args: ViewAgroDealerInDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewAgroDealerInDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        val agrodealer = args.agrodealer

        binding.apply {
            textViewAgroDealerNameDetail.text = agrodealer.name
            textViewAgroDealerServices.text = agrodealer.servicesOffered
            textViewAgroDealerCategories.text = agrodealer.categories
            textViewIfLeasingOptionOfferred.text = agrodealer.leasingOptionsAvailable
            textViewLeasingDetails.text = agrodealer.leasingDetails
        }
    }
}