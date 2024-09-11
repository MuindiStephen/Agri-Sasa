package com.steve_md.smartmkulima.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSuccessfulPaymentBinding
import com.steve_md.smartmkulima.utils.DateFormat.getWhenStarts
import com.steve_md.smartmkulima.utils.services.Config
import com.steve_md.smartmkulima.utils.services.NumberUtil
import com.steve_md.smartmkulima.utils.services.PrintServiceActivity
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

var map: HashMap<String, String> = HashMap()


@AndroidEntryPoint
class SuccessfulPaymentFragment : Fragment() {
    private lateinit var binding: FragmentSuccessfulPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessfulPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpBinding()

        configViews()

        setUpAnimation()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showBackPressedDialog()
        }
    }

    private fun showBackPressedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Do you want to go back? Your payment has been completed successfully.")
            .setPositiveButton("Yes") { dialog, which ->
                findNavController().navigate(R.id.homeDashboardFragment2)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun configViews() {

        binding.apply {
            textView142.text = "(+254) "+arguments?.getString("PHONE_NUMBER")
            textView150.text = "Kes. "+arguments?.getString("AMOUNT")
            textView146.text = getWhenStarts()
        }
    }

    private fun setUpAnimation() {
        binding.apply {
            lottieSuccessfulPayment.setAnimation(R.raw.ic_success)
            lottieSuccessfulPayment.repeatCount = LottieDrawable.INFINITE
            lottieSuccessfulPayment.playAnimation()
        }
    }
    private fun setUpBinding() {
       binding.buttonGoHome.setOnClickListener {
          findNavController().navigate(
              R.id.homeDashboardFragment2
          )
       }
        binding.buttonPrintReceipt.setOnClickListener {
            map["reference"] = "txnReference"
            map["qr_auth_code"] = "qr_auth_code"

            // Create an empty JSONObject if you don't have any data to pass
            val jsonObject = JSONObject()

            PrintServiceActivity.startPrinting(context, map, getDepositReceiptText(jsonObject))
            startActivity(Intent(requireContext(), PrintServiceActivity::class.java))
            requireActivity().finish()
        }

    }


    @Throws(JSONException::class)
    private fun getDepositReceiptText(jsonObject: JSONObject): Array<String> {
        val wasTxnSuccessful = jsonObject.optBoolean("wasTxnSuccessful", false)
        return if (wasTxnSuccessful) {

            val totalAmount =
                (jsonObject.getString("amount").replace(",", "")
                    .toDouble() + jsonObject.getString("charge").replace(",", "")
                    .toDouble() + jsonObject.getString("tax").replace(",", "")
                    .toDouble()).toString()
            arrayOf<String>(
                PrintServiceActivity.centeredText(jsonObject.getString("txnType"), 48),
                PrintServiceActivity.centeredText(jsonObject.getString("txnResultMessage"), 44),
                PrintServiceActivity.SEPARATOR_LINE,
                ("Txn Amount:    " + Config.CURRENCY_CODE).toString() + " " + NumberUtil.formatNumber(
                    jsonObject.getString("amount")
                ),
                ("Txn Fee:       " + Config.CURRENCY_CODE).toString() + " " + NumberUtil.formatNumber(
                    jsonObject.getString("charge")
                ),
                ("Excise Duty:   " + Config.CURRENCY_CODE).toString() + " " + NumberUtil.formatNumber(
                    jsonObject.getString("tax")
                ),
                ("Total Amount:  " + Config.CURRENCY_CODE).toString() + " " + NumberUtil.formatNumber(
                    totalAmount
                ),
                PrintServiceActivity.SEPARATOR_LINE,
                "\n",
                "Account No.:   " + jsonObject.getString("accountNo"),
                "Account Name: " + jsonObject.getString("customerName"),
                "Narration:    " + jsonObject.getString("narration")
            )
        } else {
            arrayOf<String>()
        }
    }
}