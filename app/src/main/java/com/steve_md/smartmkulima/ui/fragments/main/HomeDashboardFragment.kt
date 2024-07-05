package com.steve_md.smartmkulima.ui.fragments.main

import android.annotation.SuppressLint
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.GapAdapter
import com.steve_md.smartmkulima.data.remote.GapApiClient
import com.steve_md.smartmkulima.databinding.FragmentHomeDashboardBinding
import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.utils.DateFormat.getLastLoginDayAndDate
import com.steve_md.smartmkulima.utils.displaySnackBar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class HomeDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHomeDashboardBinding
    private lateinit var databaseReference: DatabaseReference

    private lateinit var adapter: GapAdapter
    private var gapList = ArrayList<GAP>()

    private var firebaseAuth: FirebaseAuth? = null


    private lateinit var userProfileTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDashboardBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        val userId =  firebaseAuth!!.uid

        if (userId == null) {
            Timber.e("Firebase User ID is null")
            return binding.root
        }
        val currentUserLogged = firebaseAuth!!.currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId!!)

         databaseReference.child("email")
             .addListenerForSingleValueEvent(object : ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                     val username = snapshot.getValue(String::class.java)

                     if (currentUserLogged!=null){
                         if (username != null) {
                             val pascalCaseUsername = username.substring(0, 1)
                                 .uppercase(Locale.getDefault()) + username.substring(1)
                                 .lowercase(Locale.getDefault())
                             binding.includeToolBar.userNameTextView.text = username
                         }
                         Timber.tag("$this@HomeDashboardFragment").d("$username is loggedIn" )
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {
                     Timber.d("Failed to retrieve username {}",error.message)
                 }
             })

        binding.includeToolBar.notificationIcon.setOnClickListener {
            findNavController().navigate(R.id.action_homeDashboardFragment2_to_notificationsFragment)
        }

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

        userProfileTxt = view.findViewById<TextView>(R.id.userNameTextView)

        val greetingDateTime = view.findViewById<TextView>(R.id.greetingsTextView)
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        greetingDateTime.text = when (currentTime) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            else -> "Good Evening"
        }
        setUpBinding()

        // fetch Good Agricultural practices
        getGoodAgriculturalPractices()
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        // Set the layout manager
       // binding.homeGapRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter
        adapter = GapAdapter(GapAdapter.OnClickListener { gap ->
            Timber.i("=====Checking=======>: ${gap.nameGAP} Good Agricultural practice!")

//            findNavController().navigate(
//                HomeDashboardFragmentDirections.actionHomeDashboardFragment2ToAllGAPHomeFragment(gap)
//            )

            val bundle = Bundle().apply {
                putParcelable("gap", gap)
            }
            findNavController().navigate(
                R.id.action_homeDashboardFragment2_to_allGAPHomeFragment, bundle
            )

        })

        // Set the adapter to the RecyclerView
        binding.homeGapRecyclerView.adapter = adapter
    }

    private fun getGoodAgriculturalPractices() {
        GapApiClient.api.getAllGAPs()
            .enqueue(object : retrofit2.Callback<ArrayList<GAP>> {
                @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
                override fun onResponse(
                    call: Call<ArrayList<GAP>>,
                    response: Response<ArrayList<GAP>>
                ) {
                    if (response.isSuccessful) {

                        Timber.i("==== Viewing Good Agri. practices${response.body()}=====")
                        // displaySnackBar("Viewing Available cycles")

                        val gaps = response.body()

                        gaps?.let {
                            gapList.addAll(it)
                            adapter.submitList(gapList)
                        }
                        adapter.notifyDataSetChanged()
                        binding.homeGapRecyclerView.adapter = adapter
                        binding.homeGapRecyclerView.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<ArrayList<GAP>>, t: Throwable) {
                    Timber.e("nothing here.${t.localizedMessage}")
                }
            })
    }


    @SuppressLint("SetTextI18n")
    private fun setUpBinding() {
        binding.apply {
            cardView.setOnClickListener {
                findNavController().navigate(R.id.action_homeDashboardFragment2_to_didYouKnow)
            }
            cardView9.setOnClickListener {
                // TODO()
            }

            textViewLastLoggedInTimeDate.text = "Last login: " +getLastLoginDayAndDate()
        }
    }
}