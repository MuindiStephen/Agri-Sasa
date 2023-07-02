package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentPaymentEquipmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentEquipmentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentEquipmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpBinding()
    }
    private fun setUpBinding() {
        binding.apply {
            imageView3.setOnClickListener { findNavController().navigateUp() }
            buttonProceedToPayHiredEquipment.setOnClickListener {
                findNavController().navigate(R.id.action_paymentEquipmentFragment_to_paymentFragment)
            }

            lottieAnimationView.setAnimation(R.raw.approved_verified)
            lottieAnimationView.repeatCount = LottieDrawable.INFINITE
            lottieAnimationView.playAnimation()
        }
    }
}