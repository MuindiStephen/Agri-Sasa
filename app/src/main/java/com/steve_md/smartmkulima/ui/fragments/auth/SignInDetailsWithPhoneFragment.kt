//package com.steve_md.smartmkulima.ui.fragments.auth
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupWithNavController
//import com.steve_md.smartmkulima.R
//import com.steve_md.smartmkulima.databinding.FragmentSignInDetailsWithPhoneBinding
//import com.steve_md.smartmkulima.utils.Resource
//import com.steve_md.smartmkulima.utils.toast
//import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collectLatest
//
//
//@AndroidEntryPoint
//class SignInDetailsWithPhoneFragment : Fragment() {
//
//    private lateinit var binding: FragmentSignInDetailsWithPhoneBinding
//
//    private lateinit var navController: NavController
//
//    private val loginWithPhoneViewModel:AuthenticationViewModel by viewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentSignInDetailsWithPhoneBinding.inflate(layoutInflater, container, false)
//
////        binding.signInWithPhoneButton.setOnClickListener {
////            findNavController().navigate(R.id.action_signInDetailsFragment_to_homeDashboardFragment2)
////        }
//        binding.signInWithEmailInsteadText.setOnClickListener {
//            findNavController().navigate(R.id.action_signInDetailsFragment_to_signInDetailsWithEmailFragment)
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
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        binding.mainAuthsToolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.mainAuthsToolbar.title = null
//
//
//
//        binding.signInWithEmailInsteadText.setOnClickListener {
//            navigateToSignInWithEmailFragment()
//        }
//
//        binding.forgotPasswordTextView.setOnClickListener {
//            findNavController().navigate(R.id.action_signInDetailsFragment_to_recoverPasswordFragment)
//        }
//
//        binding.signInWithPhoneButton.setOnClickListener {
//
//            binding.progressBar5.isVisible = true
//            if (isValidCredentials()) {
//                loginUser()
//            }
//            else toast("Enter valid credentials")
//        }
//
//        lifecycleScope.launchWhenResumed {
//            loginWithPhoneViewModel.loginRes.collectLatest {
//                when (it) {
//                    Resource.Loading -> {
//                        toast("Loading")
//                        binding.progressBar5.isVisible = true
//                    }
//                    is Resource.Error -> {
//                        toast("Couldn't log in")
//                        binding.progressBar5.isVisible = false
//                    }
//                    is Resource.Success -> {
//                        val userId = it.value.accessToken
//
//                        // check whether user data is null or available in the backend db api
//                        it.value.accessToken.let {
//                            binding.progressBar5.isVisible = false
//                            // if available then login the user successfully.
//                            toast("Successfully Logged In")
//                            navigateToHomeDashboardFragment()
//
//                            savePrefsToken("token")
//
//                        }
//
//                    }
//                    null -> {}
//                }
//            }
//        }
//
//    }
//
//    private fun savePrefsToken(token:String) {
//        requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
//            .edit().putString("login", token)
//            .apply()
//    }
//
//    private fun navigateToHomeDashboardFragment() {
//        findNavController().navigate(R.id.action_signInDetailsFragment_to_homeDashboardFragment2)
//    }
//
//    private fun loginUser() {
//        Log.i("VAVAVA","VIEWMODEL IS nUll -> ${loginWithPhoneViewModel == null}")
//        loginWithPhoneViewModel.loginUserUsingPhone(
//            binding.inputPhoneNumber.text.toString(),
//            binding.inputPassword.text.toString()
//        )
//    }
//
//    private fun isValidCredentials(): Boolean {
//        return binding.inputPhoneNumber.text.isNullOrEmpty().not().also {
//            if (!it) binding.enterPhoneNumber.error = "Invalid Email"
//        } && binding.inputPassword.text.isNullOrEmpty().not().also {
//            if (!it) binding.enterPassword.error = "Invalid Password"
//        }
//    }
//
//    private fun navigateToSignInWithEmailFragment() {
//        findNavController().navigate(R.id.action_signInDetailsFragment_to_signInDetailsWithEmailFragment)
//    }
//
//}