package com.steve_md.smartmkulima.utils.code_scanner

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.ActivityCustomScannerBinding


class AnyOrientationCaptureActivity : AppCompatActivity(), TorchListener {

    private var torchOn: Boolean = false
    private lateinit var capture: CaptureManager
    private lateinit var binding : ActivityCustomScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            ActivityCustomScannerBinding.inflate(layoutInflater).also {
                binding = it
            }.root
        )
        capture = CaptureManager(this, binding.zxingBarcodeScanner)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.decode()
        binding.zxingBarcodeScanner.setTorchListener(this)


        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            imageViewClose.setOnClickListener {
                onBackPressed()
            }

            imageViewFlash.setOnClickListener {
                switchTorchOn()
            }
        }
    }

    private fun switchTorchOn() {
        if (torchOn) {
            binding.zxingBarcodeScanner.setTorchOff()
        } else {
            binding.zxingBarcodeScanner.setTorchOn()
        }
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onTorchOn() {
        torchOn = true
        binding.imageViewFlash.setImageResource(R.drawable.ic_flash_off)
    }

    override fun onTorchOff() {
        torchOn = false
        binding.imageViewFlash.setImageResource(R.drawable.ic_flash_on)
    }
}