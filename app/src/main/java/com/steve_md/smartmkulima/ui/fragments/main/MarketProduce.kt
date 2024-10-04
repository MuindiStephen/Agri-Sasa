package com.steve_md.smartmkulima.ui.fragments.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.adapter.FarmProduceAdapter
import com.steve_md.smartmkulima.databinding.FragmentMarketProduceBinding
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.hideKeyboard
import com.steve_md.smartmkulima.utils.toast
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MarketProduce : Fragment() {

    private lateinit var binding: FragmentMarketProduceBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val farmProduceAdapter by lazy { FarmProduceAdapter() }
    private val farmProduceList = mutableListOf<FarmProduce>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketProduceBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        subScribeToFarmProduceObserver()
        setUpBinding()
    }

    private fun setUpBinding() {


        // Filters Search results as you type in the Edit Text
        binding.inputSearchFarmProduce.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterResults(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                
            }
        })


        binding.apply {
            imageView4.setOnClickListener {
                findNavController().navigateUp()
            }
        }



        binding.inputSearchFarmProduce.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hideKeyboard()

                val searchText = binding.searchFarmProducts.editText?.text.toString().trim()

                if (searchText.isEmpty()) {
                    toast("Enter some text in order to search")
                }
                searchingFarmProduce(searchText)
                true
            } else {
                false
            }

        }

        binding.searchFarmProducts.setEndIconOnClickListener {
            hideKeyboard()

            if (binding.searchFarmProducts.editText?.text.isNullOrEmpty()) {
                return@setEndIconOnClickListener
            }
            binding.searchFarmProducts.editText?.setText("")
            fetchAllFarmProduce()
        }
    }

    private fun filterResults(query: String) {

        if (query.isEmpty()) {
            toast("Enter some text in order to search")
        } else {
           searchingFarmProduce(query)
        }
    }

    private fun searchingFarmProduce(searchText: String) {
        val filteredList = farmProduceList.filter { it.productTitle.equals(searchText, ignoreCase = true) }
        farmProduceAdapter.submitList(filteredList.toMutableList())
    }

    private fun fetchAllFarmProduce() {
        subScribeToFarmProduceObserver()
    }

    private fun subScribeToFarmProduceObserver() {
        lifecycleScope.launch {
            mainViewModel.produce.collect { farmProduceState ->
                Timber.e("Farm Produce State: $farmProduceState")


                if (farmProduceState.isLoading) {
                   displaySnackBar("Loading Farm Produce...")
                    binding.textView172.isVisible = false
                }
                if (farmProduceState.error != null && !farmProduceState.isLoading) {
                    displaySnackBar("Check Your Internet Connection and Try again!")
                    Timber.e(farmProduceState.error.toString())
                    binding.textView172.isVisible = true
                }
                if (farmProduceState.farmProduce.isNotEmpty() && !farmProduceState.isLoading) {

                    binding.textView172.isVisible = false

                    farmProduceList.clear()
                    farmProduceList.addAll(farmProduceState.farmProduce)

                    farmProduceAdapter.submitList(farmProduceList)
                    binding.farmProduceRecyclerView.adapter = farmProduceAdapter

                    Timber.i("Fetched successfully: ${farmProduceState.farmProduce}")

                    displaySnackBar("Buy now, at discounted prices")
                }
            }
        }
    }

}