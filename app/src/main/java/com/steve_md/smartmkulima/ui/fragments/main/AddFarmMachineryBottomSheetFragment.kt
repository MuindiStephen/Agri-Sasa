package com.steve_md.smartmkulima.ui.fragments.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentAddFarmMachineryBottomSheetBinding
import com.steve_md.smartmkulima.model.FarmMachinery
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AddFarmMachineryBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddFarmMachineryBottomSheetBinding
    private val viewModel: MainViewModel by viewModels()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddFarmMachineryBottomSheetBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Image Selection
        binding.machineryImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        binding.submitButton.setOnClickListener {


            if (
                binding.machineryName.text.toString()
                    .isEmpty() ||
                binding.machineryDescription.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please fill all fields and select an image!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val machinery = FarmMachinery(
                    id = UUID.randomUUID().toString(),
                    name = binding.machineryName.text.toString(),
                    description = binding.machineryDescription.text.toString(),
                    price = binding.machineryPrice.text.toString().toDouble(),
                    productImage = imageUri?.toString() ?: "",
                    isSynced = false
                )
                saveMachineryToDatabase(machinery)
            }
        }

    }

    private fun saveMachineryToDatabase(machinery: FarmMachinery) {
        // Use Room Database or SharedPreferences
        viewModel.insertMachinery(machinery = machinery)
        Toast.makeText(requireContext(), "Farm Machinery Added!", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            binding.machineryImageView.setImageURI(imageUri)
        }
    }
}