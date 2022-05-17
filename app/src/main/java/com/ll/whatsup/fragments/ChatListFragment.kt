package com.ll.whatsup.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.activities.ChatActivity
import com.ll.whatsup.activities.ContactsActivity
import com.ll.whatsup.adapter.ChatListAdapter
import com.ll.whatsup.model.Chat

class ChatListFragment() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val db = FirebaseDB()
        //val temp = db.getAccount("+923164222121")
        //if(temp!=null){
        //    FirebaseDB.acc=temp
        //}
        //val chats:ArrayList<Chat> = FirebaseDB.acc.chats
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val  recyclerView = view.findViewById<RecyclerView>(R.id.chatListView)
        val adp = ChatListAdapter(ArrayList()){
            val gson = Gson()
            val itJSON: String = gson.toJson(it)
            startActivity(Intent(context, ChatActivity::class.java).putExtra("ChatwithContact", itJSON))
        }

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