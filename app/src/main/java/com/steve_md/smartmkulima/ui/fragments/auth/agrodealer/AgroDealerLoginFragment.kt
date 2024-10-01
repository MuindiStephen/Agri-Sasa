package com.steve_md.smartmkulima.ui.fragments.auth.agrodealer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentAgroDealerLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AgroDealerLoginFragment : Fragment() {
    private lateinit var binding: FragmentAgroDealerLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAgroDealerLoginBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        initViewBinding()

        binding.signInWithEmailButton.setOnClickListener {
            if (validatedInputs()) {
                lifecycleScope.launch {
                    binding.progressBar4.isVisible = true
                    delay(1500L)
                    binding.progressBar4.isVisible = false
                    val bundle = Bundle()
                    bundle.putString("agrodealerID", binding.inputID.text.toString())
                    findNavController().navigate(
                        R.id.agroDealerHomeDashboardFragment,
                        bundle
                    )
                }

            }
        }
    }

    private fun initViewBinding() {
        binding.mainAuthsToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun validatedInputs(): Boolean {

        return binding.inputID.text.isNullOrEmpty().not().also {
            if (!it) binding.enterID.error = "**required, empty"
        } && binding.inputEmail.text.isNullOrEmpty().not().also {
            if (!it) binding.enterEmail.error = "**required empty"
        }
    }
}