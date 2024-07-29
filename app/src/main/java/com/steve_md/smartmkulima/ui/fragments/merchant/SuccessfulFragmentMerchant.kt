package com.steve_md.smartmkulima.ui.fragments.merchant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail.DetailDialogAdapter
import com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail.model.DialogDetailCommon
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentSucessfulMerchantBinding


class SuccessfulFragmentMerchant : Fragment() {

    private lateinit var binding: FragmentSucessfulMerchantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSucessfulMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    // set up UI
    private fun setUpUI() {

        binding.recyclerViewDetails.apply {
            // create adapter
            val dialogAdapter = DetailDialogAdapter()

            // create a layout manager
            val linearLayoutManager = LinearLayoutManager(context)

            //set the layout manager
            layoutManager = linearLayoutManager

            // submit a list
            dialogAdapter.submitList(getDummyDetails)

            //set the adapter
            adapter = dialogAdapter
        }

        // navigate to home page
        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_successfulFragmentMerchant_to_fragmentHomeMerchant)
        }

//        binding.text

    }

    //dummy details
    private val getDummyDetails = mutableListOf<DialogDetailCommon>().apply {
        add(DialogDetailCommon("AMOUNT:","Kes 2000.00"))
        add(DialogDetailCommon("NAME:","Alex Mwangi"))
        add(DialogDetailCommon("MERCHANT NUMBER:","123456"))
        add(DialogDetailCommon("SPLIT BILL:","NO"))
        add(DialogDetailCommon("CHARGES","Kes 00.00"))
        add(DialogDetailCommon("TIME & DATE:","12-04-2021 | 4:00PM"))
        add(DialogDetailCommon("REF ID:","1234567890"))
    }

}
