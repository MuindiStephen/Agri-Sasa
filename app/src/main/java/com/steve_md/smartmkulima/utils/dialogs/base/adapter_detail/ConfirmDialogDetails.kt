package com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail.model.DialogDetailCommon


// populates the details in the dialog recyclerview
// NOTE! This is also re used in the success fragment

class DetailDialogAdapter() : ListAdapter<DialogDetailCommon, DetailDialogAdapter.ViewHolder>(
    DIFF_UTIL
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dialogDetail = getItem(position)
        holder.apply {
            textViewlabel.text = dialogDetail.label
            textViewContent.text = dialogDetail.content
        }
    }


    class ViewHolder(private val itemDialog: View) : RecyclerView.ViewHolder(itemDialog){
        val textViewlabel: TextView = itemDialog.findViewById<TextView>(R.id.textViewlabel)
        val textViewContent: TextView = itemDialog.findViewById<TextView>(R.id.textViewContent)
    }

}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<DialogDetailCommon>() {
    override fun areItemsTheSame(oldItem: DialogDetailCommon, newItem: DialogDetailCommon): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DialogDetailCommon, newItem: DialogDetailCommon): Boolean {
        return oldItem == newItem
    }

}