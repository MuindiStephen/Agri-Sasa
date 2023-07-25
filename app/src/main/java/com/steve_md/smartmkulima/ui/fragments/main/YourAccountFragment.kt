package com.steve_md.smartmkulima.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentYourAccountBinding
import com.steve_md.smartmkulima.ui.fragments.others.Settings
import timber.log.Timber


class YourAccountFragment : Fragment() {

    private var _binding:FragmentYourAccountBinding? = null
    private val binding get() = _binding!!

    var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYourAccountBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        initBinding()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth!!.currentUser

        if (currentUser != null) {
            binding.tvUserName.text = currentUser.email.toString()
        }
        else {
            Timber.tag(TAG).e("An error,occurred while retrieving your profile")
        }
    }

    private fun initBinding() {

        binding.apply {

            settings.setOnClickListener {
               startActivity(Intent(requireActivity().applicationContext, Settings::class.java))
                requireActivity().finish()
            }
            signOutUser.setOnClickListener {
                firebaseAuth!!.signOut()
                findNavController().navigate(R.id.action_yourAccountFragment_to_loginMainFragment)
            }
            share.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT,"AgriSasa App | Hello, I found this useful AgriTech app that offers latest Agricultural technology")
                startActivity(Intent.createChooser(intent,"Share via :)"))
            }
            history.setOnClickListener {
                 findNavController().navigate(R.id.action_yourAccountFragment_to_transactionsHistory2)
            }
            getHelpAndInquiries.setOnClickListener {
              findNavController().navigate(R.id.action_yourAccountFragment_to_helpFragment)
            }
            imageViewBackFromAccount.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    companion object {
        const val TAG = "YourAccountProfileFragment"
    }
}