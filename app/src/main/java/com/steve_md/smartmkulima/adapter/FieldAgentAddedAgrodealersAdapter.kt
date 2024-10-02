package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.FarmFieldRowItemBinding
import com.steve_md.smartmkulima.databinding.ItemAddedAgrodealersFieldagentRowBinding
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentAddAgroDealerData

/**
 * FieldAgentAddedAgroDealersAdapter
 */
class FieldAgentAddedAgroDealersAdapter : ListAdapter<FieldAgentAddAgroDealerData, FieldAgentAddedAgroDealersAdapter.MyViewHolder>(MyDiffUtil) {

    object MyDiffUtil : DiffUtil.ItemCallback<FieldAgentAddAgroDealerData>() {
        override fun areItemsTheSame(oldItem: FieldAgentAddAgroDealerData, newItem: FieldAgentAddAgroDealerData): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: FieldAgentAddAgroDealerData, newItem: FieldAgentAddAgroDealerData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class MyViewHolder(private val binding: ItemAddedAgrodealersFieldagentRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(fieldAgentAddAgroDealerData: FieldAgentAddAgroDealerData?) {
            binding.textView163.text = fieldAgentAddAgroDealerData?.name.toString()
            binding.textView165.text = "Phone: "+fieldAgentAddAgroDealerData?.phone.toString()
            binding.textView166.text = "Email: "+fieldAgentAddAgroDealerData?.email.toString()
            binding.textView167.text = "Physical Address: "+fieldAgentAddAgroDealerData?.physicalLocationAddress.toString()
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAddedAgrodealersFieldagentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fieldAgentAddAgroDealerData = getItem(position)
        holder.bind(fieldAgentAddAgroDealerData)
    }
}