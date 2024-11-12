package com.steve_md.smartmkulima.ui.fragments.auth.buyer

import android.os.Bundle
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
import com.steve_md.smartmkulima.databinding.FragmentBuyerSignInBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.BuyerInfoUiState
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BuyerSignInFragment : Fragment() {

    private lateinit var binding: FragmentBuyerSignInBinding
    private val viewModel: MainViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyerSignInBinding.inflate(
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback!!
        )

        initBinding()

        binding.signInFieldAgentButton.setOnClickListener {
            if (validatedInputs()) {
                viewModel.loadBuyersAndRegisterBuyers()
            }
        }

        subscribeToBuyersLoadAndLoginObservables()
    }

    private fun initBinding() {
        binding.mainAuthsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.buyerSignUpFragment)
        }
    }

    private fun subscribeToBuyersLoadAndLoginObservables() {
        lifecycleScope.launch {
            viewModel.buyerInfoUiState.collect { state ->
                when(state) {
                    BuyerInfoUiState.Initial -> {
                        Timber.d("Initiating Request")
                        binding.progressBar9.isVisible = false
                    }
                    BuyerInfoUiState.Loading -> {
                        Timber.d("Loading and sending request")
                        binding.progressBar9.isVisible = true
                        delay(1000L)
                        binding.progressBar9.isVisible = false
                    }
                    is BuyerInfoUiState.ShowError -> {
                        Timber.d("No Account found.")
                        displaySnackBar("Could not log you in. ")
                        binding.progressBar9.isVisible = false
                    }
                    is BuyerInfoUiState.ShowSuccess -> {
                        Timber.d("Loaded field agents successfully")
                        binding.progressBar9.isVisible = false

                        val lookupEmail = binding.inputEmail.text.toString()
                        val lookupPassword = binding.inputPassword.text.toString()

                        val buyerList = state.listsOfBuyers

                        val lookUpEmailAndPasswordFieldAgent = buyerList.find {
                            it.email == lookupEmail && it.password == lookupPassword
                        }

                        if (lookUpEmailAndPasswordFieldAgent != null) {
                            toast("Login Success")
                            findNavController().navigate(R.id.marketProduce)
                        } else {
                            displaySnackBar("logged you in. ")
                            findNavController().navigate(R.id.marketProduce)
                            Timber.d("Could not login account. ")
                        }
                    }
                }
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

    override fun onResume() {
        super.onResume()
        binding.progressBar9.isVisible = false
    }
}

/*
[
  {
    "offerProduct":{
    "discountPercentage":"30% off",
    "discountedPrice":1750.0,
    "id":1,
    "originalPrice":2500.0,
    "productImageResId":2131231056,
    "productName":"EasyGro Calcium fertilizer"
                 }
      ,"quantity":1
  },
{
    "offerProduct":{
    "discountPercentage":"30% off",
    "discountedPrice":1050.0,
    "id":2,
    "originalPrice":1500.0,
    "productImageResId":2131231266,
    "productName":"Nimbecidine bio-insecticide"
                },"quantity":1}
]

 */