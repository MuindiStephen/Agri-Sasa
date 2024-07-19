package com.steve_md.smartmkulima.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.databinding.HomeGapRowBinding
import com.steve_md.smartmkulima.model.GAP

/**
 * Bind fetched GAPS to the Ui (Recycler View)
 * - In order to display a list of available GAPs to the RecyclerView Ui
 */
class GapAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<GAP, GapAdapter.MyViewHolder>(TaskDiffUtil) {

    object TaskDiffUtil : DiffUtil.ItemCallback<GAP>() {
        override fun areItemsTheSame(oldItem: GAP, newItem: GAP): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GAP, newItem: GAP): Boolean {
            return oldItem.nameGAP == newItem.nameGAP
        }
    }

    inner class MyViewHolder(private var binding: HomeGapRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(gap: GAP?) {
            binding.apply {
                textViewGAP.text = gap?.nameGAP
                Glide.with(imageGAP).load(gap?.imageGAP).into(imageGAP)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            HomeGapRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gap = getItem(position)
        holder.bind(gap)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(gap = gap)
        }
    }

    class OnClickListener(val clickListener: (gap: GAP) -> Unit) {
        fun onClick(gap: GAP) = clickListener(gap)
    }
}
