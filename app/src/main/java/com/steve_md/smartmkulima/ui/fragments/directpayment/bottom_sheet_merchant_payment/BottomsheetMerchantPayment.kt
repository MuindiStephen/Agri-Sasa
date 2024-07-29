package com.steve_md.smartmkulima.ui.fragments.directpayment.bottom_sheet_merchant_payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.steve_md.smartmkulima.ui.fragments.directpayment.FragmentDirectPayment
import com.steve_md.smartmkulima.utils.PaymentOptionState
import com.steve_md.smartmkulima.utils.dialogs.base.adapter_detail.model.DialogDetailCommon
import com.ekenya.rnd.merchant.ui.fragments.direct_payment.bottom_sheet_merchant_payment.AdapterRecyclerViewOptions
import com.ekenya.rnd.merchant.ui.fragments.direct_payment.bottom_sheet_merchant_payment.NFCCard
import com.ekenya.rnd.merchant.ui.fragments.direct_payment.bottom_sheet_merchant_payment.NfcCardsViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.BottomsheetPaymentMerchantBinding
import com.steve_md.smartmkulima.utils.MerchantListUtils
import com.steve_md.smartmkulima.utils.code_scanner.AnyOrientationCaptureActivity
import com.steve_md.smartmkulima.utils.setUpSpinner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class BottomSheetMerchantPayment(
    private val toSuccess: () -> Unit,
    private val toSelectContact: () -> Unit

) :
    BottomSheetDialogFragment(),
    ConfirmDialogCallBacks
{

    private lateinit var binding : BottomsheetPaymentMerchantBinding
    private var lastPaymentState : PaymentOptionState = PaymentOptionState.MobileMoney

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetPaymentMerchantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Reduce the ui skipping frames
        lifecycleScope.launch {
            initUI()
        }
    }

    // do UI initializations
    private fun initUI() {
        setupPaymentRecyclerview()
        setUpMyCardsRecyclerView()
        setUpBindings()
        setUpCardViewPager()
    }

    // set up bindings
    private fun setUpBindings() {

        binding.apply {

            // populate autocomplete view having accounts
            accountSpinner.setUpSpinner(
                R.array.accountProvider,
                onItemClick = { _, _, _, _ ->

                })


            // populate autocomplete view having payment options [M-pesa , Airtel]
            val mAdapter = AdapterSpinnerPaymentsProviders(
                requireContext(),
                MerchantListUtils.spinnerPayments
            )
            spinnerServiceProvider.setAdapter(mAdapter)

            // get payment provider from the spinner
            spinnerServiceProvider.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        // val option = parent?.getItemAtPosition(position)

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        //Do nothing
                    }

                }

            binding.imageViewScanAddcard.setOnClickListener {
                // method to launch scanner activity
                val options = ScanOptions()
                options.setOrientationLocked(false)
                options.captureActivity = AnyOrientationCaptureActivity::class.java
                barcodeLauncher.launch(options)
            }

            buttonContinue.setOnClickListener {
                handleButtonClick(buttonContinue.text.toString().lowercase(Locale.getDefault()))
            }

            textViewAddNewCard.setOnClickListener {
                setPaymentOption(PaymentOptionState.AddNewCard)
            }

            switchPaidFor.setOnCheckedChangeListener { _, payForMe ->

                if (payForMe) {
                    setPaymentOption(PaymentOptionState.PayForMe)
                    binding.payForMe.isVisible = false
                } else {
                    setPaymentOption(lastPaymentState)
                    binding.payForMe.isVisible = true
                    buttonContinue.text = "continue"
                }
            }

        }

    }


    // Set up recyclerview containing payment options
    private fun setupPaymentRecyclerview() {
        binding.apply {
            paymentList.apply {

                // create payment adapter
                val paymentAdapter = AdapterRecyclerViewOptions { setPaymentOption(it) }

                // set adapter
                adapter = paymentAdapter

                // create layout manager
                val linearLayoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                // set layout manager
                layoutManager = linearLayoutManager

                // submit the list of payment options
                paymentAdapter.submitList(MerchantListUtils.paymentOptions)

                // setup dots indicator
               // dashboardIndicatorLayout.setIndicatorCount(MerchantListUtils.paymentOptions.size)

                // set dots starting position
                // binding.dashboardIndicatorLayout.selectCurrentPosition(0)

            }
        }
    }

    // Hide all groups
    private fun hideAllGroups() {
        binding.apply {

            // hide debit card group
            groupAddCard.isVisible = false

            // hide mobile money group
            groupMobileMoney.isVisible = false

            //hide my cards group
            groupMyCards.isVisible = false

            // hide card number group
            groupNfcCardNumber.isVisible = false

            // hide select account group
            groupSelectAccount.isVisible = false


        }
    }

    // show the selected group based on what was selected
    private fun setPaymentOption(paymentOptionMerchant: PaymentOptionState) {

        if (!binding.switchPaidFor.isChecked){
            lastPaymentState = paymentOptionMerchant
        }

        // hide all groups
        hideAllGroups()
        binding.buttonContinue.isVisible = true

        // when the selected one is ...
        when (paymentOptionMerchant) {

            // mobile money, show group
            PaymentOptionState.MobileMoney -> {
                binding.groupMobileMoney.isVisible = true
              //  binding.dashboardIndicatorLayout.selectCurrentPosition(0)
            }

            // bank account, show group
            PaymentOptionState.BankAccount -> {
                binding.groupSelectAccount.isVisible = true
               // binding.dashboardIndicatorLayout.selectCurrentPosition(1)
            }

            // debit card, show group
            PaymentOptionState.DebitCredit -> {

                binding.groupMyCards.isVisible = true
              //  binding.dashboardIndicatorLayout.selectCurrentPosition(2)

            }

            // NFC option, show group
            PaymentOptionState.NFC -> {
                binding.groupNfcCardNumber.isVisible = true
              //  binding.dashboardIndicatorLayout.selectCurrentPosition(3)
                binding.buttonContinue.isVisible = false

            }

            PaymentOptionState.PayForMe -> {
                binding.groupMobileMoney.isVisible = true
                binding.buttonContinue.text = "Look up that number"
            }

            PaymentOptionState.AddNewCard -> {
                binding.groupAddCard.isVisible = true
               // binding.dashboardIndicatorLayout.selectCurrentPosition(2)
                binding.buttonContinue.text = "Add Card"
            }

        }
    }

    // populate a list of cards which I currently own
    private fun setUpMyCardsRecyclerView() {

        val cardsAdapter = AdapterRecycleviewMyCards()

        val linearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }

        binding.recyclerViewMyCards.apply {
            adapter = cardsAdapter
            layoutManager = linearLayoutManager
            isNestedScrollingEnabled = true
        }

        cardsAdapter.submitList(MerchantListUtils.myCards)
    }

    // handle button clicks based on the text on the button
    private fun handleButtonClick(text: String) {
        when(text){

            "Add Card".lowercase(Locale.getDefault()) -> {
                simulateAddCard()
            }

            "Look up that number".lowercase(Locale.getDefault()) -> {
                simulateNumberLookup()
            }

            "continue" -> {
                showDialog()
            }
        }
    }

    //simulate adding a card
    private fun simulateAddCard() {
        lifecycleScope.launch {
           // showHideProgress("Adding Card")
            delay(2000L)
            // showHideProgress(null)
            binding.buttonContinue.text = "CONTINUE"
            setPaymentOption(PaymentOptionState.DebitCredit)
        }
    }

    private fun setUpCardViewPager() {

        //Create a Pager Transformer for the Viewpager
        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(24))
            addTransformer { page, position ->
                val transform = 1 - kotlin.math.abs(position)
                page.scaleY = (0.85f + transform * 0.15f)
            }
        }

        //Sets adapter to the viewpager
        val cardAdapter = NfcCardsViewPagerAdapter()
        binding.viewpagerCard.apply {
            adapter = cardAdapter
            setPageTransformer(compositePageTransformer)
            clipToPadding = false
            offscreenPageLimit = 20
            setPadding(48, 0, 48, 0)
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            isNestedScrollingEnabled = true
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        binding.viewpagerCard.adapter = cardAdapter

        cardAdapter.submitList(mutableListOf<NFCCard>().apply {
            add(NFCCard(0,"Debit","Visa","1234 •••• •••• 2344 ","12/25"))
            add(NFCCard(0,"Credit","Debit","1234 •••• •••• 2344 ","12/25"))
            add(NFCCard(0,"Debit","Visa","1234 •••• •••• 2344 ","12/25"))
        })

        //register on page change callback
        binding.viewpagerCard.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
              //  binding.dashboardIndicatorLayout.selectCurrentPosition(position)
//                selected = cards[position]
            }

        })


    }

    //simulate loading for a number
    private fun simulateNumberLookup() {
        lifecycleScope.launch {
           // showHideProgress("Searching member")
            delay(2000L)
            // showHideProgress(null)
            showDialog("Member found","Please confirm you are making a friend to pay for you")
            binding.buttonContinue.text = "CONTINUE"
        }
    }

    // show dialog
    private fun showDialog(title : String = "Confirm Merchant Payment", subtitle : String = "Please confirm you are making a merchant payment to Garden city 123456") {

        val callBacks = object : ConfirmDialogCallBacks{
            override fun confirm() {
                TODO("Not yet implemented")
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }

        }
        // showConfirmationDialog(title,subtitle,getDetails(),callBacks)

    }

    //get the details to display in the success screen based on the last payment state
    private fun getDetails() : List<DialogDetailCommon> {
        return when(lastPaymentState){
            PaymentOptionState.BankAccount -> paywithAccountDetails()
            PaymentOptionState.DebitCredit -> paywithCardDetails()
            PaymentOptionState.MobileMoney -> payMobileMoneyDetails()
            PaymentOptionState.NFC -> paywithNFCDetails()
            PaymentOptionState.PayForMe -> payForMeDetails()
            PaymentOptionState.AddNewCard -> emptyList()
        }
    }

    /**
     * Dummy details
     */
    private fun payMobileMoneyDetails() = mutableListOf<DialogDetailCommon>().apply {
        add(DialogDetailCommon("Amount", "Ksh ${FragmentDirectPayment.amount}"))
        add(DialogDetailCommon("Split bill", "NO"))
        add(DialogDetailCommon("Pay with", "MPESA +254712345678"))
        add(DialogDetailCommon("Charges", "Ksh 10.00"))
    }

    private fun paywithAccountDetails() = mutableListOf<DialogDetailCommon>().apply {
        add(DialogDetailCommon("Amount", "Ksh ${FragmentDirectPayment.amount}"))
        add(DialogDetailCommon("Split bill", "NO"))
        add(DialogDetailCommon("Pay with", "Current Account- A/C #1234******6789"))
        add(DialogDetailCommon("Charges", "Ksh 10.00"))
    }

    private fun paywithCardDetails() = mutableListOf<DialogDetailCommon>().apply {
        add(DialogDetailCommon("Amount", "Ksh ${FragmentDirectPayment.amount}"))
        add(DialogDetailCommon("Split bill", "NO"))
        add(DialogDetailCommon("Pay with", "Gold credit 5535 •••• •••• 2279"))
        add(DialogDetailCommon("Charges", "Ksh 10.00"))
    }

    private fun paywithNFCDetails() = mutableListOf<DialogDetailCommon>().apply {
        add(DialogDetailCommon("Amount", "Ksh ${FragmentDirectPayment.amount}"))
        add(DialogDetailCommon("Split bill", "NO"))
        add(DialogDetailCommon("Pay with", "NFC"))
        add(DialogDetailCommon("Charges", "Ksh 10.00"))
    }

    private fun payForMeDetails() = mutableListOf<DialogDetailCommon>().apply {
        add(DialogDetailCommon("Name", "Alex Mwangi"))
        add(DialogDetailCommon("Amount", "Ksh ${FragmentDirectPayment.amount}"))
        add(DialogDetailCommon("Split bill", "NO"))
        add(DialogDetailCommon("Charges", "Ksh 10.00"))
    }

    override fun confirm() {
        toSuccess.invoke()
        this@BottomSheetMerchantPayment.dismiss()
    }

    override fun cancel() {
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(
                    requireContext(),
                    "Cancelled",
                    Toast.LENGTH_LONG
                ).show()
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Timber.tag("MainActivity").d("Cancelled scan due to missing camera permission")
                Toast.makeText(
                    requireContext(),
                    "Cancelled due to missing camera permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.d("MainActivity", "Scanned")
            Toast.makeText(
                requireContext(),
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object{
        val contact = ""
    }

}
