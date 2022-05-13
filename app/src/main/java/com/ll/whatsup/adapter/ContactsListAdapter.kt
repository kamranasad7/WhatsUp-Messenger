package com.ll.whatsup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.R
import com.ll.whatsup.model.Contact

class ContactsListAdapter(var contactList: ArrayList<Contact>): RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         var contact_name = itemView.findViewById<TextView>(R.id.ContactName)
        var contat_bio = itemView.findViewById<TextView>(R.id.contactBio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.contact_card_list,parent,false)
        return ContactViewHolder(v)
    }


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var contact = contactList[position]
        holder.contact_name.text = contact.name
        holder.contat_bio.text=contact.number
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

}