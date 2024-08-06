package com.ekenya.rnd.common.dialogs.dialog_confirm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail.DetailDialogAdapter
import com.ekenya.rnd.common.dialogs.base.BaseDialogCommon
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail.model.DialogDetailCommon


// Structure your dialog here

interface ConfirmDialogCallBacks {
    fun confirm();
    fun cancel();
}

class ConfirmDialogCommon(val context: Context) : BaseDialogCommon() {


    //  dialog view
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.layout_confirm_dialog, null)
    }

    //dialog builder
    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    // dialog title
    private val dialogTitle by lazy {
        dialogView.findViewById<TextView>(R.id.textViewDialogTitle)
    }

    // dialog sub title
    private val dialogSubtitle by lazy {
        dialogView.findViewById<TextView>(R.id.textViewDialogSubtitle)
    }

    // dialog recyclerview contents
    private val dialogContentRecyclerView by lazy {
        dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialogContents)
    }

    //  confirm button
    private val confirmButton by lazy {
        dialogView.findViewById<Button>(R.id.button_confirm)
    }

    //  cancel button
    private val cancelButton by lazy {
        dialogView.findViewById<Button>(R.id.button_cancel)
    }

    // set up the recycler adapter
    fun setUpRecyclerAdapter(detailCommons: List<DialogDetailCommon>) {
        val dialogAdapter = DetailDialogAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        dialogContentRecyclerView.apply {
            layoutManager = linearLayoutManager
            dialogAdapter.submitList(detailCommons)
            adapter = dialogAdapter
        }
    }

    // set dialog title
    fun setDialogTitle(title: String) {
        dialogTitle.text = title
    }

    // set dialog sub title
    fun setDialogSubtitle(subtitle: String) {
        dialogSubtitle.text = subtitle
    }

    //  cancel button ClickListener with listener
    fun cancelButtonClickListener(callBacks: ConfirmDialogCallBacks) =
        with(cancelButton) {
            setClickListenerToDialogIcon {
                callBacks.cancel()
                dialog?.dismiss()
            }
        }

    //  confirm button ClickListener with listener
    fun confirmButtonClickListener(callBacks: ConfirmDialogCallBacks) =
        with(confirmButton) {
            setClickListenerToDialogIcon {
                callBacks.confirm()
                dialog?.dismiss()
            }

        }

    fun setCallbacks(dialogCallBacks: ConfirmDialogCallBacks) {
        confirmButtonClickListener(dialogCallBacks)
        cancelButtonClickListener(dialogCallBacks)
    }

    //  view click listener as extension function
    private fun View.setClickListenerToDialogIcon(func: (() -> Unit)?) =
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }

}