package com.steve_md.smartmkulima.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.FarmEquipmentRowBinding
import com.steve_md.smartmkulima.model.FarmEquipment

/**
 * A recycler view to display a list of all available farm equipments for hire
 */
class FarmEquipmentAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<FarmEquipment, FarmEquipmentAdapter.MyViewHolder>(MyDiffUtil) {

    object MyDiffUtil : DiffUtil.ItemCallback<FarmEquipment>() {
        override fun areItemsTheSame(
            oldItem: FarmEquipment,
            newItem: FarmEquipment
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FarmEquipment,
            newItem: FarmEquipment
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    inner class MyViewHolder(private val binding: FarmEquipmentRowBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        fun bind(equipment: FarmEquipment?) {
            Glide.with(binding.equipmentImage).load(equipment?.imageUrl).into(binding.equipmentImage)
            binding.farmEquipmentName.text = equipment?.name
            binding.textViewYear.text = equipment?.year
            binding.priceEquipment.text = equipment?.priceHire
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FarmEquipmentRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val equipment = getItem(position)
        holder.bind(equipment)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(farmEquipment = equipment)
        }
    }
    // Handling OnclickListener on recyclerview holder
    class OnClickListener(val clickListener: (farmEquipment:FarmEquipment) -> Unit) {
        fun onClick(farmEquipment: FarmEquipment) = clickListener(farmEquipment)
    }
}
