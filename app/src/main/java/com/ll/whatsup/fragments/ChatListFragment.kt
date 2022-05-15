package com.ll.whatsup.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ll.whatsup.R
import com.ll.whatsup.activities.ContactsActivity
import com.ll.whatsup.adapter.ChatListAdapter
import com.ll.whatsup.model.Chat

class ChatListFragment(var chats:ArrayList<Chat>) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val  recyclerView = view.findViewById<RecyclerView>(R.id.chatListView)
        val adp = ChatListAdapter(chats)
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=adp

        val fabBtn = view.findViewById<FloatingActionButton>(R.id.contactList_fab)
        fabBtn.setOnClickListener{
            val i = Intent(context, ContactsActivity::class.java)
            startActivity(i)
        }

        return view
    }

}