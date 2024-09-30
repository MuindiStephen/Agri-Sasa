package com.steve_md.smartmkulima.ui.fragments.main.fieldagents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.databinding.FragmentAddANewAgroDealerBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Launched as sheet from bottom
 * to add a new agro-dealer
 */
@AndroidEntryPoint
class AddANewAgroDealerBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddANewAgroDealerBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddANewAgroDealerBottomsheetBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}