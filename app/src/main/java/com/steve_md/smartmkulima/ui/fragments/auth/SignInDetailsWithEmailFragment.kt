package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignInDetailsWithEmailBinding
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest


class SignInDetailsWithEmailFragment : Fragment() {

    // According to the docs this is a nice way get binding
    private var _binding: FragmentSignInDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    // view model
    private val loginWithEmailViewModel : AuthenticationViewModel by viewModels()

   override  fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInDetailsWithEmailBinding.inflate(layoutInflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInWithPhoneInsteadText.setOnClickListener {
            navigateToSignInWithPhoneFragment()
        }

        binding.signInWithEmailButton.setOnClickListener {
            if (isValidCredentials()) loginUser()
            else toast("Enter valid credentials")
        }

        lifecycleScope.launchWhenResumed {
            loginWithEmailViewModel.loginResult.collectLatest {
                when (it) {
                    Resource.Loading -> {
                        toast("Loading")
                    }
                    is Resource.Error -> {
                        toast("Couldn't log in")
                    }
                    is Resource.Success -> {
                        val userId = it.value.email

                        // check whether user data is null or available in the backend db api
                        it.value.email.let {

                            // if available then login the user successfully.
                            if (it !=null) {
                                toast("Successfully Logged In")
                                navigateToHomeDashboardFragment()
                            } else {
                                toast("Account Doesn't exist")
                            }
                        }

                    }
                    null -> {}
                }
            }
        }

    }

    private fun navigateToHomeDashboardFragment() {
        findNavController().navigate(R.id.action_signInDetailsWithEmailFragment_to_homeDashboardFragment2)
    }

    private fun loginUser() {
        Log.i("VAVAVA","VIEWMODEL IS nUll -> ${loginWithEmailViewModel == null}")
        loginWithEmailViewModel.loginUserUsingEmail(
            binding.inputLoginEmail.text.toString(),
            binding.inputLoginPassword.text.toString()
        )
    }

    private fun isValidCredentials(): Boolean {
        return binding.inputLoginEmail.text.isNullOrEmpty().not().also {
            if (!it) binding.enterLoginEmailAddress.error = "Invalid Email"
        } && binding.inputLoginPassword.text.isNullOrEmpty().not().also {
            if (!it) binding.enterLoginPassword.error = "Invalid Password"
        }
    }

    private fun navigateToSignInWithPhoneFragment() {
        findNavController().navigate(R.id.action_signInDetailsWithEmailFragment_to_signInDetailsFragment)
    }



}