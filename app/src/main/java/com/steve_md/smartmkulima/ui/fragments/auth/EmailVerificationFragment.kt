//package com.steve_md.smartmkulima.ui.fragments.auth
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupWithNavController
//import com.steve_md.smartmkulima.R
//import com.steve_md.smartmkulima.databinding.FragmentEmailVerificationBinding
//import com.steve_md.smartmkulima.utils.displaySnackBar
//import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//
//@AndroidEntryPoint
//class EmailVerificationFragment : Fragment() {
//
//    private lateinit var binding: FragmentEmailVerificationBinding
//    private val emailOtpViewModel: AuthenticationViewModel by viewModels()
//
//    private lateinit var navController: NavController
//
//
//   // private val args: EmailVerificationFragmentArgs by navArgs()
//   // private var email = ""
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentEmailVerificationBinding.inflate(layoutInflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        (activity as AppCompatActivity).supportActionBar?.hide()
//        navController = findNavController()
//
//
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.mainAuthsToolbar.title = null
//
//
//        binding.sendCodeToEmailInsteadText.setOnClickListener {
//           // findNavController().navigate(R.id.action_emailVerificationFragment_to_verificationFragment)
//            displaySnackBar("Feature coming soon")
//        }
//
//       // email = args.email
//       // binding.emailSentVerificationCode.text = email
//
//
//        binding.buttonVerifyEmailOTP.setOnClickListener {
//            verifyEmailCode()
//        }
//        object : CountDownTimer(30000, 1000) {
//
//            // Callback function, fired on regular interval
//            @SuppressLint("SetTextI18n")
//            override fun onTick(millisUntilFinished: Long) {
//                binding.resendCodeTimer.text = "0." + millisUntilFinished / 1000
//                binding.callMeTimer.text = "1." + millisUntilFinished / 500
//            }
//
//            // Callback function, fired
//            // when the time is up
//            @SuppressLint("SetTextI18n")
//            override fun onFinish() {
//                binding.resendCodeTimer.text = "done!"
//                binding.callMeTimer.text = "Time exceeded"
//            }
//        }.start()
//
//
//        // TODO (Not working)
//        //  Button email verify OTP
//        /**
//        binding.buttonVerifyEmailOTP.setOnClickListener {
//        val code:String  =  binding.pinView.text.toString()
//        binding.progressBar2.isVisible = true
//        emailOtpViewModel.verifyEmailOtp(code)
//        }
//         */
//
//        //observeViewModelOtp()
//
//    }
//
//    private fun verifyEmailCode() {
//        val inputEmailCode = binding.pinView.text.toString()
//
//        if (inputEmailCode == "002002") {
//            displaySnackBar("Email Verification successful,Proceed to Login")
//            findNavController().navigate(R.id.action_emailVerificationFragment_to_signInDetailsWithEmailFragment)
//        } else {
//            displaySnackBar("Invalid code, please try again")
//        }
//    }
//}
//
//
//    /** TODO (not working for now)
//
//    private fun observeViewModelOtp() {
//        lifecycleScope.launchWhenResumed {
//            emailOtpViewModel.otpResult.collectLatest {
//                when (it) {
//                    Resource.Loading -> {
//                        toast("Loading")
//                        binding.progressBar2.isVisible = true
//                    }
//                    is Resource.Error -> {
//                        toast("Verified")
//                        binding.progressBar2.isVisible = false
//
//                        navigateToLoginPage()
//
//                    }
//                    is Resource.Success -> {
//                        displaySnackBar("Code is incorrect,verification not successful !")
//                        binding.progressBar2.isVisible = false
//                        //navigateToLoginPage()
//                    }
//                    null -> {}
//                }
//
//            }
//        }
//    }
//
//    private fun navigateToLoginPage() {
//        findNavController().navigate(R.id.action_emailVerificationFragment_to_signInDetailsWithEmailFragment)
//    }
//
//
//}
//     */
//
//
