package com.ekenya.rnd.merchant.ui.fragments.direct_payment.bottom_sheet_merchant_payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.merchant.R
import com.ekenya.rnd.merchant.databinding.ItemMethodOfPaymentMerchantBinding
import com.ekenya.rnd.merchant.utils.PaymentOption
import com.steve_md.smartmkulima.utils.PaymentOptionState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * shows a list of payment options on the bottomsheet
 */
class AdapterRecyclerViewOptions(
    private val selectPaymentOption: (PaymentOptionState) -> Unit
) : ListAdapter<PaymentOption, AdapterRecyclerViewOptions.ViewHolder>(DIFF_UTIL) {

    private val selectedItem = MutableStateFlow(0)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMethodOfPaymentMerchantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val paymentOption = getItem(position)
        /* bind card details */

        holder.itemMethodOfPaymentMerchantBinding.apply {
            //set image resource
            imageViewPaymentLogo.setImageResource(paymentOption.image)
            //set payment name
            textViewPaymentName.text = paymentOption.name
            //set onclick listeners

            root.apply {
                setOnClickListener {
                    when (paymentOption.name) {

                        "Mobile \nMoney" -> {
                            selectPaymentOption.invoke(PaymentOptionState.MobileMoney)
                        }

                        "Bank \nAccount" -> {
                            selectPaymentOption.invoke(PaymentOptionState.BankAccount)
                        }

                        "Debit\\ \nCredit" -> {
                            selectPaymentOption.invoke(PaymentOptionState.DebitCredit)
                        }

                        "(NFC)" -> {
                            selectPaymentOption.invoke(PaymentOptionState.NFC)
                        }

                    }

                    //set current selected item
                    selectedItem.value = position
                }

                //collecting(observing) this on all the viewholders
                CoroutineScope(Dispatchers.Default).launch {
                    selectedItem.collect { selectedPosition ->
                        if (selectedPosition == position) {
                            constraintLayout.setBackgroundColor(resources.getColor(R.color.accent))
                            textViewPaymentName.setTextColor(resources.getColor(R.color.white))
                        } else {
                            constraintLayout.setBackgroundColor(resources.getColor(R.color.back_groundgrey))
                            textViewPaymentName.setTextColor(resources.getColor(R.color.text_color))
                        }
                    }
                }
            }
        }
    }

    class ViewHolder(val itemMethodOfPaymentMerchantBinding: ItemMethodOfPaymentMerchantBinding) :
        RecyclerView.ViewHolder(itemMethodOfPaymentMerchantBinding.root)

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<PaymentOption>() {
    override fun areItemsTheSame(oldItem: PaymentOption, newItem: PaymentOption): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PaymentOption, newItem: PaymentOption): Boolean {
        return oldItem == newItem
    }

}
