package com.steve_md.smartmkulima.ui.fragments.select_contact

import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.merchant.domain.model.ContactModel
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.utils.BaseCursorAdapter
import com.steve_md.smartmkulima.databinding.ItemSelectcontactMerchantBinding
import com.steve_md.smartmkulima.utils.getInitials

/**
 * Displays a list of contacts fro which to choose from
 */
class AdapterSelectContact(
    private val onContactSelected: (ContactModel) -> Boolean,
    private val contains:(ContactModel) -> Boolean,
) : BaseCursorAdapter<AdapterSelectContact.ViewHolder>(null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectcontactMerchantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, cursor: Cursor?) {

        holder.itemSelectContactMerchantBinding.apply {

            val contact = getContact(cursor)

            //bind contact details to recyclerview
            contact.apply {
                textViewNameInitials.text = initials
                textViewPhone.text = phone;
                textViewName.text = name
            }

            //assign drawable based on the existence of the object in the set
            if (!contains(contact)) { //perform check using higher order function defined in UI
                //object exists
                imageViewSelectedIndicate.setImageResource(R.drawable.ic_notselected_merchant)
            } else {
                //object doesn't exist
                imageViewSelectedIndicate.setImageResource(R.drawable.ic_selected_merchant)
            }

            //itemview clicked
            root.setOnClickListener {
                //maintain a set of selected contacts in UI using a higher order function
                if (!onContactSelected.invoke(contact)) {
                    //object exists
                    imageViewSelectedIndicate.setImageResource(R.drawable.ic_notselected_merchant)
                } else {
                    //object doesn't exist
                    imageViewSelectedIndicate.setImageResource(R.drawable.ic_selected_merchant)
                }
            }
        }
    }

    private fun getContact(cursor: Cursor?): ContactModel {

        //load name from cursor
        val mColumnIndexName = cursor!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val contactName = cursor.getString(mColumnIndexName)

        //get initials from name
        val initials = contactName.getInitials()

        //load phone number
        val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val phone = cursor.getString(phoneIndex);

        //build contact model object
        return ContactModel(contactName, phone, initials)
    }

    class ViewHolder(val itemSelectContactMerchantBinding: ItemSelectcontactMerchantBinding) :
        RecyclerView.ViewHolder(itemSelectContactMerchantBinding.root)

}

