package com.steve_md.smartmkulima.ui.fragments.auth.buyer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentBuyerSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyerSignUpFragment : Fragment() {

    private lateinit var binding: FragmentBuyerSignUpBinding
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

    }
}