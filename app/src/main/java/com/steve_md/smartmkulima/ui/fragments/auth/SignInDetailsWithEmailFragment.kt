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
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignInDetailsWithEmailBinding
import com.steve_md.smartmkulima.utils.EventObserver
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.snackBar
import com.steve_md.smartmkulima.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInDetailsWithEmailFragment : Fragment() {

    // According to the docs this is a nice way get binding
    private var _binding: FragmentSignInDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInDetailsWithEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        navController = findNavController()


        binding.signInWithEmailButton.setOnClickListener {
            authViewModel.loginUser(
                binding.inputLoginEmail.text.toString(),
                binding.inputLoginPassword.text.toString()
            )
            hideKeyboard()
        }

        subscribeToObservables()

        binding.signInWithPhoneInsteadText.setOnClickListener {
            navigateToSignInWithPhoneFragment()
        }

        binding.dontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signInDetailsWithEmailFragment_to_signUpDetailsWithEmailFragment)
        }

        binding.forgotPasswordText.setOnClickListener {
            // findNavController().navigate(R.id.action_signInDetailsWithEmailFragment_to_recoverPasswordWithEmailFragment)
            displaySnackBar("Feature coming soon")
        }
    }

    private fun subscribeToObservables() {
        authViewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                displaySnackBar(it)
                binding.progressBar.isVisible = false
            },
            onLoading = {
                binding.progressBar.isVisible = true
            }
        ) {
            binding.progressBar.isVisible = false
            displaySnackBar("Log in successful")
            navigateHome()
        })
    }

    private fun navigateHome() {
        findNavController().navigate(
            R.id.action_signInDetailsWithEmailFragment_to_homeDashboardFragment2
        )
    }

    private fun navigateToSignInWithPhoneFragment() {
        //findNavController().navigate(R.id.action_signInDetailsWithEmailFragment_to_signInDetailsFragment2)
        displaySnackBar("Feature is coming soon!")
    }
}