//package com.steve_md.smartmkulima.ui.fragments.auth
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupWithNavController
//import com.steve_md.smartmkulima.R
//import com.steve_md.smartmkulima.databinding.FragmentSignUpDetailsWithPhoneBinding
//import com.steve_md.smartmkulima.utils.Resource
//import com.steve_md.smartmkulima.utils.toast
//import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collectLatest
//
//
//@AndroidEntryPoint
//class SignUpDetailsWithPhoneFragment : Fragment() {
//
//    private lateinit var navController: NavController
//
//    private lateinit var binding: FragmentSignUpDetailsWithPhoneBinding
//
//    private val signUpWithPhoneViewModel : AuthenticationViewModel by viewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentSignUpDetailsWithPhoneBinding.inflate(layoutInflater, container, false)
//
//
//        navController = findNavController()
//
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.mainAuthsToolbar.title = null
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        (activity as AppCompatActivity).supportActionBar?.hide()
//
//        binding.signUpWithEmailInsteadText.setOnClickListener {
//            navigateToEmailFragment()
//        }
//
//        binding.buttonSignUpWithPhone.setOnClickListener {
//            if (isValidRegistrationDetails())
//            {
//                findNavController().navigate(
//                    SignUpDetailsWithPhoneFragmentDirections.actionSignUpDetailsMainFragmentToVerificationFragment(
//                        binding.inputPhoneNumber.text.toString()
//                    )
//                )
//                registerUser()
//
//            }
//
//            else toast("Unable to register. Invalid credentials!")
//        }
//
//        lifecycleScope.launchWhenResumed {
//            signUpWithPhoneViewModel.registerRes.collectLatest {
//                when (it) {
//                    is Resource.Success -> {
//                        toast("Registered Successfully, Please Login")
//                        navigateToPhoneVerificationFragment()
//                    }
//
//                    is Resource.Error -> {
//                        toast("Registered Successfully. An email verification code has been send to your email!")
//                        navigateToPhoneVerificationFragment()
//                    }
//
//                    is Resource.Loading -> {
//                        toast("Registered please verify your email!")
//                    }
//                    null -> {}
//
//                }
//            }
//        }
//
//
//    }
//
//    private fun navigateToPhoneVerificationFragment() {
//        findNavController().navigate(R.id.action_signUpDetailsMainFragment_to_verificationFragment)
//    }
//
//    private fun registerUser() {
//        signUpWithPhoneViewModel.registerUserUsingPhone(
//            binding.inputUserName.text.toString(),
//            binding.inputPhoneNumber.text.toString(),
//            binding.inputPassword.text.toString(),
//            binding.inputConfirmPassword.text.toString()
//        )
//    }
//
//    private fun isValidRegistrationDetails(): Boolean {
//        return binding.inputUserName.text.isNullOrEmpty().not().also {
//            if (!it) binding.enterUserName.error = "Invalid email"
//        } && binding.inputPhoneNumber.text.isNullOrEmpty().not().also {
//            if (!it) binding.enterPhoneNumber.error = "Invalid Name"
//        } && binding.inputPassword.text.isNullOrEmpty().not().also {
//            if (!it) binding.enterPassword.error = "Invalid Password"
//        } && binding.inputConfirmPassword.text.isNullOrEmpty().not().also {
//            if (!it) binding.confirmPassword.error = "Invalid Password"
//        }
//    }
//
//    private fun navigateToEmailFragment() {
//        findNavController().navigate(R.id.action_signInDetailsFragment_to_signInDetailsWithEmailFragment)
//    }
//
//
//
//}
//
//
//
