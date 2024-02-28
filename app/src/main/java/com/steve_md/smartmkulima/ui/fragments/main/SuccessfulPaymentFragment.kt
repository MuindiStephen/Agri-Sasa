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
import com.steve_md.smartmkulima.databinding.FragmentSuccessfulPaymentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuccessfulPaymentFragment : Fragment() {


    private lateinit var binding:FragmentSuccessfulPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessfulPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpBinding()
        setUpAnimation()
    }

    private fun setUpAnimation() {
        binding.apply {
            lottieSuccessfulPayment.setAnimation(R.raw.ic_success)
            lottieSuccessfulPayment.repeatCount = LottieDrawable.INFINITE
            lottieSuccessfulPayment.playAnimation()
        }
    }
    private fun setUpBinding() {
       binding.buttonGoHome.setOnClickListener {
          findNavController().navigate(R.id.action_successfulPaymentFragment_to_applyInsuranceFragment)
       }
    }
}