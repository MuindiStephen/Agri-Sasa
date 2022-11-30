package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.os.CountDownTimer
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
import com.steve_md.smartmkulima.utils.displaySnackBar
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


        email = args.email
        binding.emailSentVerificationCode.text = email


//        binding.pinView.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code:String  =  binding.pinView.text.toString()
//                binding.progressBar2.isVisible = false
//                emailOtpViewModel.verifyEmailOtp(code)
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//
//        })


        // time count down for 30 seconds,
        // with 1 second as countDown interval
        object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTimer.setText("0." + millisUntilFinished / 1000)
                binding.callMeTimer.setText("1."+millisUntilFinished / 500)
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                binding.resendCodeTimer.setText("done!")
                binding.callMeTimer.setText("Time exceeded")
            }
        }.start()


        //  Button email verify OTP
        binding.buttonVerifyEmailOTP.setOnClickListener {
           val code:String  =  binding.pinView.text.toString()
            binding.progressBar2.isVisible = true
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
                        toast("Verified")
                        binding.progressBar2.isVisible = false

                        navigateToLoginPage()

                    }
                    is Resource.Success -> {
                        displaySnackBar("Code is incorrect,verification not successful !")
                        binding.progressBar2.isVisible = false
                        //navigateToLoginPage()
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


