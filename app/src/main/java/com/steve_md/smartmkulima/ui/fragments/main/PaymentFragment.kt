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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
class PaymentFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mApiClient: DarajaApiClient? = null

    private var mAmount: EditText? = null
    private var mPhone: EditText? = null
    private var mPay: Button? = null

    private val clearCartViewModel : MainViewModel by activityViewModels()

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


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
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

        val consumerKey = "NgGUJ2LGJlVvjdLu8P7yDGIs6v4RmMF1114mYRUVTVOjsCii"
        val consumerSecret = "L5Ur0sgEuGfBAB7u8ynlH0bzerD1VABt7ABASfBAozvATLKETNHGcpTieX1vzLyv"

        mApiClient = DarajaApiClient(
            consumerKey,
            consumerSecret,
            SANDBOX_BASE_URL
        )

        mApiClient!!.setIsDebug(true)
        mPay!!.setOnClickListener(this)

        getAccessToken()
    }

    private fun getAccessToken() {
        mApiClient!!.setGetAccessToken(true)
        mApiClient!!.mpesaService().getAccessToken().enqueue(object :
            Callback<AuthorizationResponse?> {
            override fun onResponse(
                call: Call<AuthorizationResponse?>,
                response: Response<AuthorizationResponse?>
            ) {
                if (response.isSuccessful) {
                    mApiClient!!.setAuthToken(response.body()?.accessToken)
                }
            }

            override fun onFailure(call: Call<AuthorizationResponse?>, t: Throwable) {
                Timber.tag(TAG).e(t.printStackTrace().toString())
            }
        })
    }

    override fun onClick(v: View?) {

        if (v === mPay) {
            val phoneNumber = mPhone!!.text.toString()
            val amount = mAmount!!.text.toString()
            performSTKPush(phoneNumber, amount)
        }
    }

    private fun performSTKPush(phoneNumber: String, amount: String) {
        val timestamp = RegEx.getTimestamp()

        val stkPush = StkPushRequest(
            businessShortCode = BUSINESS_SHORT_CODE,
            password = RegEx.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp!!)!!,
            timestamp = timestamp,
            transactionType = Constants.TransactionType.CustomerPayBillOnline,
            amount = amount,
            partyA = RegEx.sanitizePhoneNumber(phoneNumber),
            partyB = PARTYB,
            phoneNumber = RegEx.sanitizePhoneNumber(phoneNumber),
            callBackURL = CALLBACKURL,
            accountReference = "LIPA NA MPESA",
            transactionDesc = "LIPA NA MPESA C2B"
        )

        mApiClient!!.setGetAccessToken(false)

        mApiClient!!.mpesaService().sendPush(stkPush)
            .enqueue(object : Callback<StkPushSuccessResponse> {
                @SuppressLint("SimpleDateFormat")
                override fun onResponse(
                    call: Call<StkPushSuccessResponse>,
                    response: Response<StkPushSuccessResponse>
                ) {
                    try {
                        if (response.isSuccessful) {

                            toast("Response : ${response.body().toString()}")

                            val bundle = Bundle()
                            bundle.putString("PHONE_NUMBER",phoneNumber)
                            bundle.putString("AMOUNT",amount)

                            // clearTheCart
                            clearCartViewModel.clearCart()

                            findNavController().navigate(
                                R.id.action_paymentFragment_to_successfulPaymentFragment,
                                bundle
                            )

                            val timestamp = System.currentTimeMillis()
                            val formattedDate = DateFormat.formatDate(timestamp)

                            val yourmilliseconds = System.currentTimeMillis()
                            val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
                            val resultdate = Date(yourmilliseconds)


                            val transaction =
                                Transaction(id = 0, amount.toDouble(), sdf.format(resultdate))

                            val db = Room.databaseBuilder(
                                requireContext(), AppDatabase::class.java, "shambaapp-db"
                            ).build()

                            val transactionDao = db.transactionDao()

                            lifecycleScope.launch {
                                withContext(Dispatchers.Main) {
                                    transactionDao.saveTransaction(transaction)
                                }
                            }
                            displaySnackBar("Saved transaction successfully.")
                            Timber.e("Post submitted to the API")
                        } else {
                            Timber.e("Response %s")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<StkPushSuccessResponse>, t: Throwable) {
                    Timber.tag(TAG).e(httpException, t.printStackTrace().toString())
                }
            })
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