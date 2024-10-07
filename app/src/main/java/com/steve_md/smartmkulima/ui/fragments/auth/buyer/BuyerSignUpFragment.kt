package com.steve_md.smartmkulima.ui.fragments.auth.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentBuyerSignUpBinding
import com.steve_md.smartmkulima.utils.ResourceNetwork
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BuyerSignUpFragment : Fragment() {

    private lateinit var binding: FragmentBuyerSignUpBinding
    private val viewModel: MainViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBuyerSignUpBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
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

        setUpBinding()

        subScribeBuyerRegisterStateObservables()

        binding.signUpFieldAgentButton.setOnClickListener {
            if (validatedInputs()) {
                viewModel.registerBuyer(
                    email = binding.inputEmail.text.toString(),
                    password = binding.inputPassword.text.toString()
                )
            }
        }

    }

    private fun validatedInputs(): Boolean {
        return binding.inputPassword.text.isNullOrEmpty().not().also {
            if (!it) binding.enterPassword.error = "**required, empty"
        } && binding.inputEmail.text.isNullOrEmpty().not().also {
            if (!it) binding.enterEmail.error = "**required empty"
        }
    }

    private fun subScribeBuyerRegisterStateObservables() {

        lifecycleScope.launch {
            viewModel.buyerRegisterState.collect { state ->
                when (state) {
                    is ResourceNetwork.Failure -> {
                        Timber.d("Request failed")
                        displaySnackBar("Account Registration Request failed. Please check your internet connection and try again.")
                    }

                    ResourceNetwork.Loading -> {
                        Timber.d("Loading request")
                    }

                    is ResourceNetwork.Success -> {
                        Timber.d("Request success")
                        displaySnackBar("Your account is created successfully, proceed to login.")
                        findNavController().navigate(R.id.buyerSignInFragment)
                    }

                    null -> {}
                }
            }
        }
    }


    private fun setUpBinding() {
        binding.tvHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.buyerSignInFragment)
        }
        binding.mainAuthsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}