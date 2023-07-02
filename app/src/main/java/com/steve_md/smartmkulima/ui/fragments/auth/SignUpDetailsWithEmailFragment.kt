package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignUpDetailsWithEmailBinding
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class SignUpDetailsWithEmailFragment : Fragment() {

    // View binding
    private var _binding: FragmentSignUpDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    // View model
    private val signUpWithEmailViewModel : AuthenticationViewModel by viewModels()

    private lateinit var navController:NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpDetailsWithEmailBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.mainAuthsToolbar.title = null

        binding.signUpWithPhoneInsteadText.setOnClickListener {
            navigateToPhoneFragment()
        }


        binding.buttonSignUpWithEmail.setOnClickListener {
            if (isValidRegistrationDetails())
            {
              findNavController().navigate(
                    SignUpDetailsWithEmailFragmentDirections.actionSignUpDetailsWithEmailFragmentToEmailVerificationFragment
                    (binding.inputEmailAddress.text.toString())
                )
                registerUser()

            }

                else displaySnackBar("Unable to register. Empty Strings or invalid")
        }

        lifecycleScope.launchWhenResumed {
           signUpWithEmailViewModel.registerResult.collectLatest {
               when (it) {
                   is Resource.Success -> {
                       toast("Registered Successfully, Please Login")
                       navigateToEmailVerificationFragment()
                   }

                   is Resource.Error -> {
                       toast("Registered Successfully. An email verification code has been send to your email!")
                       navigateToEmailVerificationFragment()
                   }

                   is Resource.Loading -> {
                         toast("Registered please verify your email!")
                   }
                   null -> {}

               }
           }
        }


    }

    private fun navigateToEmailVerificationFragment() {
        findNavController().navigate(R.id.action_signUpDetailsWithEmailFragment_to_emailVerificationFragment)
    }

    private fun registerUser() {
        signUpWithEmailViewModel.registerUsingEmail(
            binding.inputUserName.text.toString(),
            binding.inputEmailAddress.text.toString(),
            binding.inputPassword.text.toString(),
            binding.inputConfirmPassword.text.toString()
        )
    }

    private fun isValidRegistrationDetails(): Boolean {
        return binding.inputUserName.text.isNullOrEmpty().not().also {
            if (!it) binding.enterUserName.error = "Invalid email"
        } && binding.inputEmailAddress.text.isNullOrEmpty().not().also {
            if (!it) binding.enterEmailAddress.error = "Invalid Name"
        } && binding.inputPassword.text.isNullOrEmpty().not().also {
            if (!it) binding.enterPassword.error = "Invalid Password"
        } && binding.inputConfirmPassword.text.isNullOrEmpty().not().also {
            if (!it) binding.confirmPassword.error = "Invalid Password"
        }
    }

    private fun navigateToPhoneFragment() {
        findNavController().navigate(R.id.action_signUpDetailsWithEmailFragment_to_signUpDetailsMainFragment)
    }


}