package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.TransactionAdapter
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.databinding.FragmentApplyInsuranceBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ApplyInsuranceFragment : Fragment() {

    private lateinit var binding: FragmentApplyInsuranceBinding

    private val transactionAdapter by lazy { TransactionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplyInsuranceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
        binding.cardView11.setOnClickListener {
            findNavController().navigate(R.id.action_applyInsuranceFragment_to_payInsuranceModalBottomSheet)
        }

        val db = Room.databaseBuilder(requireContext(),AppDatabase::class.java,"shambaapp-db")
            .build()

        val transactionDao = db.transactionDao()

        transactionDao.getAllTransactions().observe(this) { transactions ->
            if (transactions.isEmpty()) {
               displaySnackBar("No new transactions")
            }
            else {
                transactionAdapter.submitList(transactions)
                binding.allInsuranceTransactionsRecyclerView.adapter = transactionAdapter
            }
        }
    }

}