package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentEmailVerificationBinding
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest


class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding

    private val emailOtpViewModel: AuthenticationViewModel by viewModels()

    private lateinit var navController:NavController


    private val args:EmailVerificationFragmentArgs by navArgs()
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentEmailVerificationBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()


        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.mainAuthsToolbar.title = null


        binding.sendCodeToEmailInsteadText.setOnClickListener {
            findNavController().navigate(R.id.action_emailVerificationFragment_to_verificationFragment)
        }

        binding.imageView2.setOnClickListener {
            findNavController().navigate(R.id.action_emailVerificationFragment_to_signInDetailsFragment)
        }

        email = args.email
        binding.emailSentVerificationCode.text = email


        // Email verification
//        val code:String = binding.pinView.text.toString()
//
//        if (code.equals(otp)) {
//            suspend {
//                otp.verifyUserWithEmail(code)
//            }
//            toast("Authenticated successfully")
//            findNavController().navigate(R.id.action_emailVerificationFragment_to_signInDetailsWithEmailFragment)
//        } else {
//            toast("Failed to authenticate")
//        }



        binding.buttonVerifyEmailOTP.setOnClickListener {
           val code:String  =  binding.pinView.text.toString()
            binding.progressBar2.isVisible = false
          emailOtpViewModel.verifyEmailOtp(code)

        }

        observeViewModelOtp()

    }

    private fun observeViewModelOtp() {
        lifecycleScope.launchWhenResumed {
            emailOtpViewModel.otpResult.collectLatest {
                when (it) {
                    Resource.Loading -> {
                        toast("Loading")
                        binding.progressBar2.isVisible = true
                    }
                    is Resource.Error -> {
                        toast("Authenticated Successfully")
                        binding.progressBar2.isVisible = false

                        navigateToLoginPage()

                    }
                    is Resource.Success -> {
                        toast("Authenticated Successfully")
                        binding.progressBar2.isVisible = false
                        navigateToLoginPage()
                    }
                    null -> {}
                }

            }
        }
    }

    private fun navigateToLoginPage() {
        findNavController().navigate(R.id.action_emailVerificationFragment_to_signInDetailsWithEmailFragment)
    }


}


