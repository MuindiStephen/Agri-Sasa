package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignUpDetailsWithEmailBinding
import com.steve_md.smartmkulima.utils.EventObserver
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.snackBar
import com.steve_md.smartmkulima.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpDetailsWithEmailFragment : Fragment() {

    private var _binding: FragmentSignUpDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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


        subscribeToObservers()


        binding.buttonSignUpWithEmail.setOnClickListener {
            viewModel.registerUser(
                binding.inputUserName.text.toString(),
                binding.inputEmailAddress.text.toString(),
                binding.inputConfirmPassword.text.toString(),
                binding.inputPassword.text.toString()
            )
            hideKeyboard()
        }

        binding.alreadyAcc.setOnClickListener {
            navigateToLogin()
        }
        binding.signUpWithPhoneInsteadText.setOnClickListener {
            navigateToPhoneFragment()
        }
    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarSignUp.isVisible = false
                displaySnackBar(it)
            },
            onLoading = { binding.progressBarSignUp.isVisible = true }
        ) {
            binding.progressBarSignUp.isVisible = false
            displaySnackBar("Account created successfully")
            navigateToLogin()
        })
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_signUpDetailsWithEmailFragment_to_signInDetailsWithEmailFragment)
    }

    private fun navigateToEmailVerificationFragment() {
        findNavController().navigate(R.id.action_signUpDetailsWithEmailFragment_to_emailVerificationFragment)
    }


    private fun navigateToPhoneFragment() {
        displaySnackBar("Feature coming soon...")
    }
}