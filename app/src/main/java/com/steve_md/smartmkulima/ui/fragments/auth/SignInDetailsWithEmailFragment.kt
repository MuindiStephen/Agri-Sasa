package com.steve_md.smartmkulima.ui.fragments.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignInDetailsWithEmailBinding
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest


class SignInDetailsWithEmailFragment : Fragment() {

    // According to the docs this is a nice way get binding
    private var _binding: FragmentSignInDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

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

        (activity as AppCompatActivity).supportActionBar?.hide()

        navController = findNavController()


        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.mainAuthsToolbar.title = null


        binding.signInWithPhoneInsteadText.setOnClickListener {
            navigateToSignInWithPhoneFragment()
        }

        binding.forgotPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_signInDetailsWithEmailFragment_to_recoverPasswordWithEmailFragment)
        }

        binding.signInWithEmailButton.setOnClickListener {
            //findNavController().navigate(directions)
            binding.progressBar.isVisible = true
            if (isValidCredentials()) {
                val directions = binding.inputLoginEmail.text?.let { it1 ->
                    SignInDetailsWithEmailFragmentDirections.actionSignInDetailsWithEmailFragmentToHomeDashboardFragment2(
                        it1.substring(0, binding.inputLoginEmail.text!!.indexOf('@')), binding.inputLoginPassword.text.toString()
                    )
                }
                findNavController().navigate(directions!!)
                loginUser()
                this.hideKeyboard()
            }
            else displaySnackBar("Empty Strings or Invalid credentials")
        }

        lifecycleScope.launchWhenResumed {
            loginWithEmailViewModel.loginResult.collectLatest {
                when (it) {
                    Resource.Loading -> {
                        toast("Loading")
                        binding.progressBar.isVisible = false
                    }
                    is Resource.Error -> {
                        toast("Couldn't log in! Invalid details or Account does not exist.")
                        binding.progressBar.isVisible = false
                    }
                    is Resource.Success -> {
                        val userId = it.value.email

                        // check whether user data is null or available in the backend db api
                        it.value.email.let {
                            binding.progressBar.isVisible = false
                            // if available then login the user successfully.
                            displaySnackBar("You Logged in successfully")
                             navigateToHomeDashboardFragment()

                           //  savePrefsToken("token")

                        }
//                        displaySnackBar("Invalid details or account does not exist.")

                    }
                    null -> {}
                }
            }
        }

    }

    private fun savePrefsToken(token:String) {
        requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
            .edit().putString("login", token)
            .apply()
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