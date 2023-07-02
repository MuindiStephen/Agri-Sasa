package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayInsuranceModalBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.modal_bottom_sheet_payment_option,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mpesa = view.findViewById<CardView>(R.id.mpesaCardView)
        val visa = view.findViewById<CardView>(R.id.visaCardView)

        mpesa.setOnClickListener {
            findNavController().navigate(R.id.action_payInsuranceModalBottomSheet_to_paymentFragment)
        }
        visa.setOnClickListener {
            displaySnackBar("Feature coming soon...")
        }
    }
}