package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.data.remote.DarajaApiClient
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.databinding.FragmentPaymentBinding
import com.steve_md.smartmkulima.model.Transaction
import com.steve_md.smartmkulima.payment.mpesa.dto.AuthorizationResponse
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushRequest
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushSuccessResponse
import com.steve_md.smartmkulima.utils.*
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.utils.Constants.BUSINESS_SHORT_CODE
import com.steve_md.smartmkulima.utils.Constants.CALLBACKURL
import com.steve_md.smartmkulima.utils.Constants.PARTYB
import com.steve_md.smartmkulima.utils.Constants.PASSKEY
import com.steve_md.smartmkulima.utils.Constants.SANDBOX_BASE_URL
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import com.steve_md.smartmkulima.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Initiates Mpesa STK push for payment based on inputs
 * @param phone
 * @param amount
 */
@AndroidEntryPoint
class PaymentFragment : Fragment(), View.OnClickListener {

    private var mAmount: EditText? = null
    private var mPhone: EditText? = null
    private var mPay: Button? = null

    private val clearCartViewModel : MainViewModel by activityViewModels()
    private val paymentViewModel: PaymentViewModel by activityViewModels()

    private lateinit var binding: FragmentPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
            showBackPressedDialog()
        }


        // Retrieve the total price from the arguments
        val totalPrice = arguments?.getInt("TOTAL_PRICE") ?: 0

        binding.inputAmountToPay.apply {
            setText(totalPrice.toString())
            isEnabled = false // Disable the input to make it un-editable
        }

        mAmount = view.findViewById(R.id.inputAmountToPay)
        mPhone = view.findViewById(R.id.inputPhoneNumber)
        mPay = view.findViewById(R.id.pay)


        paymentViewModel.initiateDarajaApiClient()

        mPay!!.setOnClickListener(this)

        paymentViewModel.getAccessToken()

        observeViewModelForMpesaListeners()
    }

    override fun onClick(v: View?) {

        if (v === mPay) {
            val phoneNumber = mPhone!!.text.toString()
            val amount = mAmount!!.text.toString()

            paymentViewModel.performSTKPush(phoneNumber, amount)
        }
    }

    private fun observeViewModelForMpesaListeners() {
        paymentViewModel.stkPushResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                handleSTKPushSuccess(response)
            }.onFailure { error ->
                displaySnackBar("Your Payment Request Failed. ${error.localizedMessage}")
                Timber.e("MPESA-REQUEST-FAILED: \t" +
                        "CAUSE OF FAILURE" + "${error.message}")
            }
        }
    }

    private fun handleSTKPushSuccess(response: StkPushSuccessResponse) {

        Timber.tag(TAG)
            .e("MPESA-RESPONSE==$response")

        displaySnackBar("Payment Request Successful.")

        // Save to Room DB
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                paymentViewModel.savePaymentTransactionToDB(amount = mAmount!!.text.toString())
            }
        }
        displaySnackBar("Saved transaction successfully.")
        Timber.e("Post submitted to the API")

        // Clear the cart and navigate to success fragment
        clearCartViewModel.clearCart()

        val bundle = Bundle().apply {
            putString("PHONE_NUMBER", mPhone?.text.toString())
            putString("AMOUNT", mAmount?.text.toString())
        }

        findNavController().navigate(
            R.id.action_paymentFragment_to_successfulPaymentFragment, bundle
        )
    }



    private fun showBackPressedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit Payment")
            .setMessage("Do you want to go back? Your payment may not be completed.")
            .setPositiveButton("Yes") { dialog, which ->
                findNavController().popBackStack()
            }
            .setNegativeButton("No", null)
            .show()
    }


    companion object {
        val httpException: HttpException? = null
        const val TAG = "PaymentFragment"
    }
}
