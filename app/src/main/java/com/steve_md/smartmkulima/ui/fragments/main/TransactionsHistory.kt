package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.steve_md.smartmkulima.adapter.TransactionAdapter
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.TransactionDao
import com.steve_md.smartmkulima.databinding.FragmentTransactionsHistoryBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TransactionsHistory : Fragment() {

    private lateinit var binding:FragmentTransactionsHistoryBinding
    private lateinit var transactionDao: TransactionDao
    private val transactionAdapter by lazy { TransactionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java,"shambaapp-db")
            .fallbackToDestructiveMigration()
            .build()
        transactionDao = db.transactionDao()

        lifecycleScope.launch {

            val transactionList = withContext(Dispatchers.IO) {
                transactionDao.getAllTransactions()
            }
            transactionAdapter.submitList(transactionList)
            binding.transactionsHistory.adapter = transactionAdapter
            displaySnackBar("View transactions history")

            if (transactionList.isEmpty()) {
                displaySnackBar("No new transactions")
            }
        }
    }

}