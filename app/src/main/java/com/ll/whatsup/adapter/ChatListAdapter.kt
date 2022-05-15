package com.ll.whatsup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.R
import com.ll.whatsup.model.Chat

class ChatListAdapter(var chats: ArrayList<Chat>, val listener:(Chat)->Unit) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatContactName = itemView.findViewById<TextView>(R.id.ContactName)
        val lastMessage = itemView.findViewById<TextView>(R.id.contactBio)
        val chatTime = itemView.findViewById<TextView>(R.id.chatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_card, parent, false)
        return ChatListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chat = chats[position]
        holder.chatContactName.text = chat.chatName
        holder.lastMessage.text = chat.messages.last().text
        holder.chatTime.text = chat.messages.last().time.toString()
        holder.itemView.setOnClickListener { listener (chat) }
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}