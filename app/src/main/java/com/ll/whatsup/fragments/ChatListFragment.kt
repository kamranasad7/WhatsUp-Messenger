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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.activities.ChatActivity
import com.ll.whatsup.activities.ContactsActivity
import com.ll.whatsup.adapter.ChatListAdapter
import com.ll.whatsup.model.Chat

class ChatListFragment() : Fragment() {
    lateinit var adapter: ChatListAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = view.findViewById(R.id.chatListView)

        adapter = ChatListAdapter(HashMap()){
            val gson = Gson()
            val contactJSON: String = gson.toJson(FirebaseDB.contactsHash[it.accountNum])

            val i = Intent(context, ChatActivity::class.java)
            i.putExtra("contact", contactJSON)
            i.putExtra("phone", it.accountNum)

            startActivity(i)
        }


        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.adapter=adapter
        recyclerView.setHasFixedSize(true)

        val fabBtn = view.findViewById<FloatingActionButton>(R.id.contactList_fab)
        fabBtn.setOnClickListener{
            val i = Intent(context, ContactsActivity::class.java)
            startActivity(i)
        }

        return view
    }

    fun loadChats(){
        adapter.setChatsList(FirebaseDB.currentAccount.chats)
        adapter.notifyItemRangeInserted(0, adapter.chats.size)

        FirebaseDB.chatsRef.addChildEventListener(object: ChildEventListener {
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                try {
                    val result = snapshot.getValue<HashMap<String, Chat>>()
                    val chat = ArrayList<Chat>()
                    if (result != null) {
                        chat.addAll(result.values)
                    }

                    adapter.chats[chat[0].accountNum] = chat[0]
                    adapter.notifyChange(chat[0].accountNum)
                }
                catch (e: Exception) { }
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                try {
                    val result = snapshot.getValue<Chat>()

                    if(adapter.chats[snapshot.key] == null){
                        adapter.chats[snapshot.key!!] = result!!
                        adapter.chatsList.add(0, result)
                        adapter.notifyItemInserted(0)
                        recyclerView.scrollToPosition(0)
                    }
                }

                catch (e: Exception) { }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) { }

        })
    }

}