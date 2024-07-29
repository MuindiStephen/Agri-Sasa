package com.ekenya.rnd.merchant.ui.fragments.direct_payment.bottom_sheet_merchant_payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.common.form_validation.ui_extensions.formatAsHiddenCardNumber
import com.ekenya.rnd.merchant.R
import com.ekenya.rnd.merchant.databinding.ItemBankCardsBinding


class NfcCardsViewPagerAdapter : ListAdapter<NFCCard, NfcCardsViewPagerAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //init binding and return root view
        val binding = ItemBankCardsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = getItem(position)
        //bind card details
        holder.cardBankcardItemBinding.apply {

            //set image in the VISA position
            imageViewCardLogo.setImageResource(getBackCardLogo(card.imageCardLogo))
            //set card background Image
            imageViewCardBackground.setImageResource(getBackgroundImage(card.cardType))
            //set the eclectics image
            imageViewEclectics.setImageResource(R.drawable.ic_eclectics_merchant)
            //set card number details
            textViewBankDigits.text = card.cardNumberText.formatAsHiddenCardNumber()
            //set card expiry details
            textViewExpiry.text = card.cardExpiryDate
        }
    }

    private fun getBackCardLogo(imageCardLogo: String): Int {
        return R.drawable.ic_visalogo_merchant
    }

    class ViewHolder(val cardBankcardItemBinding: ItemBankCardsBinding) : RecyclerView.ViewHolder(cardBankcardItemBinding.root)

}

private fun getBackgroundImage(cardType: String): Int {
    return when(cardType){
        "Credit" -> {
            R.drawable.ic_cardbackground_merchant
        }
        "Debit" -> {
            R.drawable.ic_cardbackgroundtwo_merchant
        }
        else -> {
            R.drawable.ic_cardbackground_merchant
        }
    }
}


//create diff util class
private val DIFF_UTIL = object : DiffUtil.ItemCallback<NFCCard>() {
    override fun areItemsTheSame(oldItem: NFCCard, newItem: NFCCard): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NFCCard, newItem: NFCCard): Boolean {
        return oldItem == newItem
    }

}

data class NFCCard(
    var id: Int,
    val cardType: String,
    val imageCardLogo: String,
    val cardNumberText: String,
    val cardExpiryDate: String
)
