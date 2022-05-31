package com.ll.whatsup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.R
import com.ll.whatsup.model.Contact

class ContactsListAdapter(var allContacts: ArrayList<Contact>, val listener:(Contact)->Unit): RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>(), Filterable {

    var contacts: ArrayList<Contact> = ArrayList()

    init {
        contacts.addAll(allContacts)
    }

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         var contactName = itemView.findViewById<TextView>(R.id.ContactName)
        var contactBio = itemView.findViewById<TextView>(R.id.contactBio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_card,parent,false)
        return ContactViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.name
        holder.contactBio.text=contact.number
        holder.itemView.setOnClickListener { listener(contact) }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun getFilter(): Filter {

        return object: Filter(){
            override fun performFiltering(text: CharSequence?): FilterResults {
                val  results = FilterResults()
                val tempList = ArrayList<Contact>()

                if(text.isNullOrBlank()){
                    tempList.addAll(allContacts)
                }
                else{
                    allContacts.forEach {
                        if(it.name.lowercase().contains(text.toString().lowercase().trim())){
                            tempList.add(it)
                        }
                    }
                }

                results.values = tempList
                return results
            }

            override fun publishResults(text: CharSequence?, result: FilterResults?) {
                contacts.clear()
                contacts.addAll(result?.values as ArrayList<Contact>)
                notifyDataSetChanged()
            }
        }
    }

}

