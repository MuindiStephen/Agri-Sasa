package com.steve_md.smartmkulima.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSignUpDetailsWithEmailBinding
import com.steve_md.smartmkulima.model.User
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SignUpDetailsWithEmailFragment : Fragment() {

    // View binding
    private var _binding: FragmentSignUpDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    // View model
    private val signUpWithEmailViewModel: AuthenticationViewModel by viewModels()


    // firebase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var navController: NavController

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


        // Initialize firebase
        databaseReference =  FirebaseDatabase.getInstance().getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()


        binding.buttonSignUpWithEmail.setOnClickListener {
            val username: String = binding.inputUserName.text.toString()
            val email: String = binding.inputEmailAddress.text.toString()
            val password: String = binding.inputPassword.text.toString()
            val confirmPassword: String = binding.inputConfirmPassword.text.toString()

            if (username.isEmpty()) {
                binding.inputUserName.error = "Empty Username"
            }
            else if (email.isEmpty()) {
                binding.inputEmailAddress.error = ("Empty Email or invalid")
            }
            else if (password.isEmpty() || password.length<8) {
                binding.inputPassword.error = ("Password too weak or password empty")
            }
            else if (confirmPassword.isEmpty() || confirmPassword.length<8) {
                binding.inputConfirmPassword.error = ("Password too weak or password empty")
            }
            else {
                binding.progressBarSignUp.visibility = View.VISIBLE

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task->

                    if (task.isSuccessful) {
                        val userId : String  = firebaseAuth.currentUser!!.uid
                        val user = User(username, email, password, confirmPassword)
                        databaseReference.child(userId).setValue(user)

                        Timber.i("User account created: $user")
                        displaySnackBar("Account created successfully")

                        firebaseAuth.signOut()

                        binding.progressBarSignUp.visibility = View.INVISIBLE

                        navigateToEmailVerificationFragment()
                    } else {
                        binding.progressBarSignUp.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.alreadyAcc.setOnClickListener {
            navigateToLogin()
        }
        binding.signUpWithPhoneInsteadText.setOnClickListener {
            navigateToPhoneFragment()
        }
        /**
        binding.buttonSignUpWithEmail.setOnClickListener {
            if (isValidRegistrationDetails()) {
                val direction =
                    SignUpDetailsWithEmailFragmentDirections.actionSignUpDetailsWithEmailFragmentToEmailVerificationFragment(
                        binding.inputEmailAddress.text.toString()
                    )
                findNavController().navigate(
                    direction
                )
                registerUser()

            } else displaySnackBar("Unable to register. Empty Strings or invalid")
        }
        */

        /**
        lifecycleScope.launchWhenResumed {
            signUpWithEmailViewModel.registerResult.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        displaySnackBar("Account Registration Successful.")
                        navigateToEmailVerificationFragment()
                    }

                    is Resource.Error -> {
                        displaySnackBar("Could not register Account!")
                    }
                    is Resource.Loading -> {
                        toast("Loading...")
                    }
                    null -> {}

                }
            }
        }
        */


    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_signUpDetailsWithEmailFragment_to_signInDetailsWithEmailFragment)
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
        displaySnackBar("Feature coming soon...")
    }
}