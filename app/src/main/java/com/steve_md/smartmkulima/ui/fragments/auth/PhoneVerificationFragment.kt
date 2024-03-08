//package com.steve_md.smartmkulima.ui.fragments.auth
//
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupWithNavController
//import com.steve_md.smartmkulima.R
//import com.steve_md.smartmkulima.databinding.FragmentPhoneVerificationBinding
//import com.steve_md.smartmkulima.utils.Resource
//import com.steve_md.smartmkulima.utils.toast
//import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collectLatest
//
//
//@AndroidEntryPoint
//class PhoneVerificationFragment : Fragment() {
//
//    private lateinit var binding: FragmentPhoneVerificationBinding
//    private lateinit var navController: NavController
//    private val phoneOTPViewModel: AuthenticationViewModel by viewModels()
//
//    private val args:PhoneVerificationFragmentArgs by navArgs()
//    private var phone = ""
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentPhoneVerificationBinding.inflate(layoutInflater, container, false)
//
//        binding.emailVerificationCodeText.setOnClickListener {
//            findNavController().navigate(R.id.action_verificationFragment_to_emailVerificationFragment)
//        }
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        (activity as AppCompatActivity).supportActionBar?.hide()
//
//        navController = findNavController()
//
//
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.mainAuthsToolbar.title = null
//
//
//
//        phone = args.phone
//        binding.phoneSendVerificationCode.text = phone
//
//
//        object : CountDownTimer(30000, 1000) {
//
//            // Callback function, fired on regular interval
//            override fun onTick(millisUntilFinished: Long) {
//                binding.textView15.setText("0." + millisUntilFinished / 1000)
//                binding.textView18.setText("1."+millisUntilFinished / 500)
//            }
//
//            // Callback function, fired
//            // when the time is up
//            override fun onFinish() {
//                binding.textView15.setText("done!")
//                binding.textView18.setText("Time exceeded")
//            }
//        }.start()
//
//        binding.buttonVerifyPhoneOTP.setOnClickListener {
//            val code:String  =  binding.pinView.text.toString()
//
//            phoneOTPViewModel.confirmPhoneOTP(code)
//
//        }
//
//        observeViewModelOtp()
//
//    }
//
//    private fun observeViewModelOtp() {
//        lifecycleScope.launchWhenResumed {
//            phoneOTPViewModel.otpRes.collectLatest {
//                when (it) {
//                    Resource.Loading -> {
//                        toast("Loading")
//                    }
//                    is Resource.Error -> {
//                        //toast("")
//
//                        navigateToLoginPage()
//
//                    }
//                    is Resource.Success -> {
//                        toast("Authenticated Successfully")
//                        navigateToLoginPage()
//                    }
//                    null -> {}
//                }
//
//            }
//        }
//    }
//
//    private fun navigateToLoginPage() {
//        findNavController().navigate(R.id.action_verificationFragment_to_signInDetailsFragment)
//    }
//
//
//}