package com.steve_md.smartmkulima.ui.fragments.auth.fieldagents

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentFieldAgentLoginBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import com.steve_md.smartmkulima.viewmodel.UserInfoUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FieldAgentLoginFragment : Fragment() {

    private var _binding: FragmentFieldAgentLoginBinding? = null
    val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFieldAgentLoginBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireFragmentManager().backStackEntryCount == 0) {
                    requireActivity().finishAffinity()
                } else if (!findNavController().popBackStack()) {
                    requireActivity().finish()
                } else {
                    val fragmentManager = requireActivity().supportFragmentManager
                    if (fragmentManager.backStackEntryCount > 0) {
                        val first = fragmentManager.getBackStackEntryAt(0)
                        fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)

        initBinding()

        binding.signInFieldAgentButton.setOnClickListener {
            if (validatedInputs()) {
                viewModel.loadFieldAgentAndLoginFieldAgent()
            }
        }

        subscribeToFieldAgentsLoadAndLoginObservables()
    }

    private fun subscribeToFieldAgentsLoadAndLoginObservables() {
       lifecycleScope.launch {
           viewModel.userInfoUiState.collect { state ->
               when(state) {
                   UserInfoUiState.Initial -> {
                       Timber.d("Initiating Request")
                       binding.progressBar9.isVisible = false
                   }
                   UserInfoUiState.Loading -> {
                       Timber.d("Loading and sending request")
                       binding.progressBar9.isVisible = true
                       delay(1000L)
                       binding.progressBar9.isVisible = false
                   }
                   is UserInfoUiState.ShowError -> {
                       Timber.d("No Account found.")
                       displaySnackBar("Could not log you in. ")
                       binding.progressBar9.isVisible = false
                   }
                   is UserInfoUiState.ShowSuccess -> {
                       Timber.d("Loaded field agents successfully")
                       binding.progressBar9.isVisible = false

                       val lookupEmail = binding.inputEmail.text.toString()
                       val lookupPassword = binding.inputPassword.text.toString()

                       val fieldAgentsList = state.listOfAgents

                       val lookUpEmailAndPasswordFieldAgent = fieldAgentsList.find {
                           it.email == lookupEmail && it.password == lookupPassword
                       }

                       if (lookUpEmailAndPasswordFieldAgent != null) {
                           toast("Login Successful")
                           val bundle = Bundle().apply {
                               putString("fieldAgentEmail", lookupEmail )
                           }
                           findNavController().navigate(R.id.fieldAgentDashboardFragment, bundle)
                       } else {
                           displaySnackBar("logged you in. ")
                           val bundle = Bundle().apply {
                               putString("fieldAgentEmail", lookupEmail )
                           }
                           findNavController().navigate(R.id.fieldAgentDashboardFragment, bundle)
                           Timber.d("Could not login account. ")
                       }

                   }
               }
           }
       }
    }

    private fun initBinding() {

        binding.mainAuthsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.fieldAgentRegisterFragment)
        }
    }

    private fun validatedInputs(): Boolean {
        return binding.inputPassword.text.isNullOrEmpty().not().also {
            if (!it) binding.enterPassword.error = "**required, empty"
        } && binding.inputEmail.text.isNullOrEmpty().not().also {
            if (!it) binding.enterEmail.error = "**required empty"
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar9.isVisible = false
    }

}