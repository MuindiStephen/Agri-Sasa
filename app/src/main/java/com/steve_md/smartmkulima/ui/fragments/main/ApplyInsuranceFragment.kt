package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.TransactionAdapter
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.TransactionDao
import com.steve_md.smartmkulima.databinding.FragmentApplyInsuranceBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class ApplyInsuranceFragment : Fragment() {

    private lateinit var binding: FragmentApplyInsuranceBinding
    private lateinit var transactionDao: TransactionDao

    private val transactionAdapter by lazy { TransactionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplyInsuranceBinding.inflate(inflater, container, false)

        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpBinding()

        val db = Room.databaseBuilder(requireContext(),AppDatabase::class.java,"shambaapp-db")
            .fallbackToDestructiveMigration()
            .build()

        transactionDao = db.transactionDao()

        lifecycleScope.launch {

            val transactionList = withContext(Dispatchers.IO) {
                transactionDao.getAllTransactions()
            }
            transactionAdapter.submitList(transactionList)
            binding.allInsuranceTransactionsRecyclerView.adapter = transactionAdapter
            displaySnackBar("View transactions history")

            if (transactionList.isEmpty()) {
                displaySnackBar("No new transactions")
            }
        }
        /** NB: Not working correctly
        transactionDao.getAllTransactions().observe(this) { transactions ->
            if (transactions.isEmpty()) {
               displaySnackBar("No new transactions")
            }
            else {
               activity?.runOnUiThread {
                   transactionAdapter.submitList(transactions)
                   transactionAdapter.notifyDataSetChanged()
                   binding.allInsuranceTransactionsRecyclerView.adapter = transactionAdapter
                   displaySnackBar("Transactions History updated")
               }
            }
        }
        */
    }

    private fun setUpBinding() {
        binding.apply {
            cardView11.setOnClickListener {
                findNavController().navigate(R.id.action_applyInsuranceFragment_to_payInsuranceModalBottomSheet)
            }
            imageView3.setOnClickListener {
                findNavController().navigateUp()
            }
            tvViewAllTransactions.setOnClickListener {
                findNavController().navigate(R.id.action_applyInsuranceFragment_to_transactionsHistory2)
            }
        }
    }
}