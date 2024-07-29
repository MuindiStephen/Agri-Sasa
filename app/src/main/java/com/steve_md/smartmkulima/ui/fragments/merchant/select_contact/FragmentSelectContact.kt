package com.steve_md.smartmkulima.ui.fragments.merchant.select_contact

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.merchant.domain.model.ContactModel
import com.steve_md.smartmkulima.databinding.FragmentSelectContactMerchantBinding
import com.steve_md.smartmkulima.ui.fragments.select_contact.AdapterSelectContact
import com.steve_md.smartmkulima.utils.toast

class FragmentSelectContact : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var binding: FragmentSelectContactMerchantBinding

    //init select contact adapter
    private val mAdapter: AdapterSelectContact by lazy {
        AdapterSelectContact(
            onContactSelected = { onContactSelected(it) },
            contains = { contains(it) }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectContactMerchantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        checkContactsPermissions()
        initBindings()
    }

    private fun initBindings() {
        binding.apply {

            buttonContinue.setOnClickListener {
                val selectedContacts = contactHashSet.toMutableList()
                if (selectedContacts.isNotEmpty()) {

                    findNavController().navigateUp()
                } else {
                    toast("No Contact was selected")
                }
            }

            //Navigate back
            binding.apply {
                toolbar.setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
            }

        }
    }

    //check if has permission read contacts
    private fun checkContactsPermissions() {
        // read contacts if has permission
        if (hasPermission(Manifest.permission.READ_CONTACTS)){
            // initialize the contacts recycler adapter
            initContactsAdapter()
        }else{
            // request  permissions if none
            requestContactsPermission()
        }
    }


    //set up contacts recyclerview
    private fun initContactsAdapter() {
        binding.recyclerViewContacts.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        //start loading contacts
        LoaderManager.getInstance(requireActivity())
            .initLoader(CONTACTS_LOADER_ID, null, this)
    }


    //Add or remove contact to a hashset when received
    private fun onContactSelected(contact: ContactModel): Boolean {

        // remove if set contains the contact
        if (contactHashSet.contains(contact)) {
            contactHashSet.remove(contact)
        } else {
            // add if it does not
            contactHashSet.add(contact)
        }
        return contactHashSet.contains(contact)
    }

    private fun contains(contact: ContactModel) = contactHashSet.contains(contact)

    //requests for permissions
    private fun requestContactsPermission() {
        if (shouldShowRequestPermissionRationale(
                Manifest.permission.READ_CONTACTS
            )
        ) {
            // show dialog
            AlertDialog.Builder(requireContext())
                .setTitle("Permission needed")
                .setMessage("This permission is needed for you to add contacts to split your bill")
                .setPositiveButton("ok"
                ) { _, _ ->
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        0
                    )
                }
                .setNegativeButton("cancel"
                ) { dialog, _ -> dialog.dismiss() }
                .create().show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                0
            )
        }
    }

    //check if a particular permission is granted
    private fun hasPermission(permission: String) =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    //callback after user interacts with permission dialog
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == 0){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initContactsAdapter()
            } else {
                Toast.makeText(requireContext(), "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor?> {
        return contactsLoader()
    }

    override fun onLoadFinished(loader: Loader<Cursor?>, data: Cursor?) {
        mAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        mAdapter.swapCursor(null)
    }

    private fun contactsLoader(): Loader<Cursor?> {

        val contactsUri: Uri =
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI// The content URI of the phone contacts

        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val selection: String? = null //Selection criteria
        val selectionArgs = arrayOf<String>() //Selection criteria
        val sortOrder: String = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC" //The sort order for the returned rows
        return CursorLoader(
            requireContext(),
            contactsUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    }
    companion object {
        val contactHashSet = HashSet<ContactModel>()
        private const val CONTACTS_LOADER_ID = 1
    }
}