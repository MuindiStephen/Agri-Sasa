package com.steve_md.smartmkulima.ui.fragments.directpayment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.utils.dialogs.dialog_progress.ProgressDialogCommon
import com.ekenya.rnd.common.form_validation.ui_extensions.removeNonDigits
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentDirectPaymentMerchantBinding
import com.steve_md.smartmkulima.ui.fragments.directpayment.bottom_sheet_merchant_payment.BottomSheetMerchantPayment
import com.steve_md.smartmkulima.ui.fragments.merchant.select_contact.FragmentSelectContact
import com.steve_md.smartmkulima.utils.form_validation.ui_extensions.formatAsCurrency
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentDirectPayment : Fragment(){

    private lateinit var binding: FragmentDirectPaymentMerchantBinding

    private var searched = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDirectPaymentMerchantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    override fun onResume() {
        super.onResume()
        binding.groupMerchantDetails.isVisible = searched
    }

    private fun setUpUi() {
        setUpBindings()
        if (FragmentSelectContact.contactHashSet.isNotEmpty()) {
            showMerchantPaymentBottomSheet()
        }
    }

    private fun setUpBindings() {
        binding.button.setOnClickListener {
            when (binding.groupMerchantDetails.isVisible) {
                // when that layout is visible, it means a search has already been done
                true -> {
                    //decide whether to just pay or split the bill
                    payOrSplitBill()
                }

                // simulate a search
                false -> {
                    simulateSearch()
                }
            }
        }

        //Navigate back
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            editTextTextAmount.formatAsCurrency()
        }

    }

    private fun simulateSearch() {
        lifecycleScope.launch {
           simulateSearching()
            binding.groupMerchantDetails.isVisible = true
            searched = true
            binding.button.text = "CONTINUE"
        }
    }

    private fun payOrSplitBill() {

        if (binding.editTextTextAmount.text.isNullOrEmpty()){
            binding.editTextTextAmount.error = "Please enter amount"
            return
        }else{
            amount = binding.editTextTextAmount.text.toString().removeNonDigits().toInt()
        }

        when (binding.switchSplitBill.isChecked) {
            true -> {
               // findNavController().navigate(R.id.action_directPayment_to_splitBill)
            }
            false -> {
                showMerchantPaymentBottomSheet()
            }
        }
    }

    private suspend fun simulateSearching(){
           showHideProgress("Searching merchant")
            delay(2000)
          showHideProgress(null)
    }

    private fun showMerchantPaymentBottomSheet() {
        val bottomSheetDialogFragment: BottomSheetDialogFragment =
            BottomSheetMerchantPayment (toSuccess = {navigateToSuccess()}, toSelectContact = {navigateToSelectContact()})
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

    private fun navigateToSuccess() {
        findNavController().navigate(R.id.action_fragmentDirectPayment_to_successfulFragmentMerchant)
    }

    private fun navigateToSelectContact() {
      //  findNavController().navigate(R.id.action_directPayment_to_SelectContact)
    }

    companion object {
        var amount = 0
    }

    // HashMap<String, String> hashMap = new HashMap<String, String>();
    private var progressAlertDialog: AlertDialog? = null
    private fun showHideProgress(message: String?) {
        if (progressAlertDialog == null) {
            val progressDialog = ProgressDialogCommon(requireContext())
            progressDialog.setloadingMessage(message)
            progressAlertDialog = progressDialog.create()
        }
        if (!progressAlertDialog!!.isShowing) {
            progressAlertDialog!!.show()
        } else {
            progressAlertDialog!!.dismiss()
        }
    }



}