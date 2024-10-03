package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentYourAccountBinding
import com.steve_md.smartmkulima.ui.fragments.others.Settings
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class YourAccountFragment : Fragment() {

    private var _binding:FragmentYourAccountBinding? = null
    private val binding get() = _binding!!

    private var firebaseAuth: FirebaseAuth? = null

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
                // startActivity(Intent(requireActivity().applicationContext, Settings::class.java))
                // requireActivity().finish()
            }

            // Signout and pop the
            signOutUser.setOnClickListener {
                logoutDialog()
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

    private fun logoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit App")
            .setMessage("Do you want to exit?")
            .setPositiveButton("Yes") { dialog, which ->

                if (firebaseAuth?.uid != null) {
                    firebaseAuth!!.signOut()
                    Timber.d("Session Ended. for ${firebaseAuth!!.currentUser?.email}" +
                            "\n Logout Successful")
                } else {
                    Timber.d("NO session found." +
                            "You have been logged out successfully.")
                }

                val fragmentManager = requireActivity().supportFragmentManager
                if (fragmentManager.backStackEntryCount > 0) {
                    val first = fragmentManager.getBackStackEntryAt(0)
                    fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }

                requireActivity().finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

    companion object {
        const val TAG = "YourAccountProfileFragment"
    }
}