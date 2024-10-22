package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.databinding.FragmentProfileDetailsAccountBinding
import com.steve_md.smartmkulima.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.InputStream


@AndroidEntryPoint
class ProfileDetailsAccountFragment : Fragment() {

    private lateinit var binding: FragmentProfileDetailsAccountBinding
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileDetailsAccountBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n", "BinaryOperationInTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
         val currentUser = firebaseAuth!!.currentUser


        if (currentUser != null) {
            binding.textView180.text = currentUser.email.toString()
            Timber.tag(TAG).e("User profile retrieved: ${currentUser.email}")
        }
        else {
            Timber.tag(TAG).e("An error,occurred while retrieving your profile. \n" +
                    "User Anonymous = ${firebaseAuth!!.currentUser?.isAnonymous}" )
            binding.textView180.text = "Anonymous user" ?: "testuser@gmail.com"
        }

        binding.apply {

            materialToolbarBackFromProfileDetails.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            imageViewPickPhoto.setOnClickListener {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.setType("image/*")
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            try {
                val imageUri = data?.data
                val imageStream: InputStream = requireActivity().contentResolver.openInputStream(imageUri!!)!!
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                binding.imageViewPickPhoto.setImageBitmap(selectedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                toast("Something went wrong")
            }
        } else {
            toast("You haven't picked Image")
        }
    }

    companion object {
        const val RESULT_LOAD_IMG = 1
        const val TAG = "ProfileDetailsAccountFragment"
    }
}