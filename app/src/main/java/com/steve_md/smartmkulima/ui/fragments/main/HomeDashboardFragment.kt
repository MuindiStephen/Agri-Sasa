package com.steve_md.smartmkulima.ui.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentHomeDashboardBinding
import com.steve_md.smartmkulima.model.User
import com.steve_md.smartmkulima.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class HomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHomeDashboardBinding

    private lateinit var databaseReference: DatabaseReference

    var firebaseAuth: FirebaseAuth? = null

    //private val getUserViewModel: MainViewModel by viewModels()

    // private lateinit var currentFragment: Fragment

   // private val args: HomeDashboardFragmentArgs by navArgs()
    //private var username = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDashboardBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.includeToolBar.menuIcon.setOnClickListener {
            AlertDialog.Builder(
                requireActivity()
            ).setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, which ->
                    performLogout()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        return binding.root
    }

    private fun performLogout() {
        firebaseAuth?.signOut()
        displaySnackBar("Logged out successfully")
        findNavController().navigate(R.id.action_homeDashboardFragment2_to_signInDetailsWithEmailFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val greetingDateTime = view.findViewById<TextView>(R.id.greetingsTextView)
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greetingDateTime.text = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }
       // username = args.username
       // binding.includeToolBar.userNameTextView.text = username.substring(0, username.indexOf('@'))

        /**
        val atIndex = username.indexOf('@')
        if (atIndex != -1) {
            binding.includeToolBar.userNameTextView.text = username.substring(0, atIndex)
        } else {
            Timber.i(username)
        }
        **/

        setUpBinding()


        // Initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId!!)
            .ref

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user  = snapshot.getValue(User::class.java)

                val username = user?.username

                if (username!= null) {
                    val usernameTextView = view.findViewById<TextView>(R.id.textViewUserNameProfile)
                    usernameTextView.text = username

                    Timber.d("Logged in user: $username")
                    displaySnackBar("Welcome, $username!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
               Timber.e("User name not updated: ${error.message}")
            }
        })
    }

    private fun setUpBinding() {
        binding.apply {
            cardView1.setOnClickListener {
                findNavController().navigate(R.id.action_homeDashboardFragment2_to_didYouKnow)
            }
            cardView9.setOnClickListener {
              // TODO()
            }
        }
    }
}