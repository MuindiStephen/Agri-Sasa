package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.databinding.FarmFieldRowItemBinding
import com.steve_md.smartmkulima.model.NewFarmField

/**
 * FarmFieldsAdapter
 */
class FarmFieldsAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<NewFarmField, FarmFieldsAdapter.MyViewHolder>(MyDiffUtil) {

    object MyDiffUtil : DiffUtil.ItemCallback<NewFarmField>() {
        override fun areItemsTheSame(oldItem: NewFarmField, newItem: NewFarmField): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: NewFarmField, newItem: NewFarmField): Boolean {
            return oldItem.id == newItem.id
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class MyViewHolder(private val binding: FarmFieldRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(farmField: NewFarmField?) {
            binding.textView109.text = farmField?.farmName.toString()
            binding.textView110.text = farmField?.farmDescription.toString()
            binding.textViewFarmFieldInitialLr.text = getInitialLetter(adapterPosition)
        }
    }

    fun getInitialLetter(position: Int): String {
        val farm = getItem(position)
        return farm.farmName.firstOrNull()?.toUpperCase().toString() ?: ""
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FarmFieldRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val farmField = getItem(position)
        holder.bind(farmField)


        /**
         *
         * Click to invoke this mtd
         */
        holder.itemView.setOnClickListener {
            onClickListener.onClick(newFarmField = farmField)
        }
    }

    class OnClickListener(val clickListener: (newFarmField: NewFarmField) -> Unit) {
        fun onClick(newFarmField: NewFarmField) = clickListener(newFarmField)
    }
}