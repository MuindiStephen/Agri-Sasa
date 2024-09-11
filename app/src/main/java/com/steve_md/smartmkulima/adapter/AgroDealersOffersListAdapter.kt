package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.ItemAgrodealersDealsRowBinding
import com.steve_md.smartmkulima.model.AgroDealerOffers

class AgroDealersOffersListAdapter(private val onItemClicked : (agrodealerOffers: AgroDealerOffers) -> Unit) : RecyclerView.Adapter<AgroDealersOffersListAdapter.MyViewHolder>() {

    private var offers: List<AgroDealerOffers> = ArrayList()

    inner class MyViewHolder(private val binding: ItemAgrodealersDealsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val buttonShopNow : Button = binding.buttonAddToCartShopNow

        fun bind(offer: AgroDealerOffers) {
            binding.imageViewProductFarmInputName.setImageResource(offer.productImageResId)
            binding.textViewProductName.text = offer.productName
            binding.textViewDiscountPercentage.text = offer.discountPercentage
            binding.textViewOriginalPrice.text = "Kes. "+offer.originalPrice.toString()
            binding.textViewProductPriceDiscounted.text = "Kes. "+offer.discountedPrice.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAgrodealersDealsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val offer = offers[position]
        holder.bind(offer)

        holder.itemView.setOnClickListener {
            onItemClicked(offer)
        }
        holder.buttonShopNow.setOnClickListener {
            onItemClicked(offer)
        }
    }

    override fun getItemCount(): Int {
        return offers.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(offersList: List<AgroDealerOffers>) {
        offers = offersList
        notifyDataSetChanged()
    }
}