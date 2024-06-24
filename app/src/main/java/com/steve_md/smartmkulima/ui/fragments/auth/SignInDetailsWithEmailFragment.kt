package com.steve_md.smartmkulima.ui.fragments.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SignInDetailsWithEmailFragment : Fragment() {

    // According to the docs this is a nice way get binding
    private var _binding: FragmentSignInDetailsWithEmailBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun authenticate() {

        /**
         * Check whether the Authentication is available
         * @param biometricManager is present, is enrolled
         */
        val biometricManager = BiometricManager.from(requireContext())
        when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Timber.tag("Biometric").e("Authenticate using biometrics")
                val prompt = createBiometricPrompt()
                prompt.authenticate(createPromptInfo())
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Timber.tag(requireActivity().toString())
                    .e("onCreate: Biometric features are currently unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // The user didn't enroll in biometrics that your app accepts, prompt them to enroll in it
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val enrollIntent =
                        Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                    startActivityForResult(enrollIntent, REQUEST_CODE)
                }
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Timber.d("Biometric features hardware is missing")
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {

            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {

            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {

            }
        }
    }

    private fun setUpBinding() {
        binding.fingerprintToPress.setOnClickListener {
            authenticate()
        }
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login")
            .setSubtitle("Log in to your Agri-Sasa account")
            .setDescription("Please authenticate using biometrics")
            .setNegativeButtonText("CANCEL")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setConfirmationRequired(false)
            .build()


    /**
     *
     * Display biometric prompt
     * @param biometricPrompt
     */
    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())

        val callBack = object : BiometricPrompt.AuthenticationCallback() {
            @SuppressLint("TimberArgCount")
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.tag(this@SignInDetailsWithEmailFragment.toString())
                    .d("$errorCode :: $errString")

                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    loginWithEmailPasswordOtherwise()
                } else if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                    snackBar("Device does not support Fingerprint biometrics")
                    loginWithEmailPasswordOtherwise()
                } else if (errorCode == BiometricPrompt.ERROR_CANCELED) {
                    loginWithEmailPasswordOtherwise()
                } else if (errorCode == BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED) {
                    snackBar("Security violation")
                    loginWithEmailPasswordOtherwise()
                } else if (errorCode == BiometricPrompt.ERROR_TIMEOUT) {
                    snackBar("Fingerprint failed. try again or login using email instead")
                } else {
                    Timber.tag(this.toString())
                        .e("%s%s", "%s || ", "onAuthenticationError: %s", errorCode, errString)
                }

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.tag(this.toString()).d("Authentication was successful")
                toast("Authenticated with biometrics successfully")
                // showEncryptedMessage(result.cryptoObject)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.tag(this.toString()).d("Authentication failed for an unknown reason")
                toast("Unknown authentication error")
            }

        }

        return BiometricPrompt(this.requireActivity(), executor, callBack)
    }

    private fun showEncryptedMessage(cryptoObject: BiometricPrompt.CryptoObject?) {

    }

    private fun loginWithEmailPasswordOtherwise() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInDetailsWithEmailBinding.inflate(layoutInflater, container, false)
        setUpBinding()

        biometricPrompt = createBiometricPrompt()

        authenticate()

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

    companion object {
        const val REQUEST_CODE = 1333
    }
}