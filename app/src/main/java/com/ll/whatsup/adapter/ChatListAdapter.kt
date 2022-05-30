package com.ll.whatsup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.model.Chat
import com.ll.whatsup.model.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatListAdapter(var chats: HashMap<String, Chat>, val listener:(Chat)->Unit) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    var chatsList: ArrayList<Chat> = ArrayList()

    init {
        chatsList.addAll(chats.values)
    }

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatContactName: TextView = itemView.findViewById(R.id.ContactName)
        val lastMessage: TextView = itemView.findViewById(R.id.contactBio)
        val chatTime: TextView = itemView.findViewById(R.id.chatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_card, parent, false)
        return ChatListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chat = chatsList[position]
        holder.chatContactName.text = FirebaseDB.contactsHash[chat.accountNum]?.name ?: chat.accountNum

        val msgs = chat.getMessagesList()

        if(msgs.size > 0){
            val last = msgs.last()
            holder.lastMessage.text = last.text
            val dateFormat = SimpleDateFormat("hh.mm aa", Locale.US)
            holder.chatTime.text = dateFormat.format(last.time).toString()
        }


        holder.itemView.setOnClickListener { listener (chat) }
        holder.chatContactName.setOnClickListener{ listener(chat) }
        holder.chatTime.setOnClickListener{ listener(chat) }
        holder.lastMessage.setOnClickListener{ listener(chat) }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun setChatsList(chats: HashMap<String, Chat>){
        this.chats = chats

        chatsList.clear()
        chatsList.addAll(chats.values)
    }

    fun notifyChange(num: String) {
        chatsList.forEachIndexed { i, chat ->
            if(chat.accountNum == num){
                chatsList[i] = chats[num]!!
                notifyItemChanged(i)
                return
            }
        }
    }
}