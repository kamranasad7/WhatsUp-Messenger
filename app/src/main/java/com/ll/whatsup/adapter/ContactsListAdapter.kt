package com.ll.whatsup.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.R
import com.ll.whatsup.model.Contact

class ContactsListAdapter(var contactList: ArrayList<Contact>, val listener:(Contact)->Unit): RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>(), Filterable {

    var list:ArrayList<Contact> = contactList

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         var contactName = itemView.findViewById<TextView>(R.id.ContactName)
        var contactBio = itemView.findViewById<TextView>(R.id.contactBio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_card,parent,false)
        return ContactViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = list[position]
        holder.contactName.text = contact.name
        holder.contactBio.text=contact.number
        holder.itemView.setOnClickListener { listener(contact) }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun getFilter(): Filter {

        return object:Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val  results:FilterResults = FilterResults()
                val tempList = ArrayList<Contact>()

                list.forEach {
                    if(p0?.let { it1 -> it.name.contains(it1) } == true){
                        tempList.add(it)
                    }
                }
                results.values=tempList
                return results
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                list.clear()
                list.addAll(p1?.values as ArrayList<Contact>)
                notifyDataSetChanged()
            }
        }
    }

}

