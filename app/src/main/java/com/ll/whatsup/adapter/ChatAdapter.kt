package com.ll.whatsup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.R
import com.ll.whatsup.model.Chat

class ChatAdapter(var chat : Chat) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var msg = itemView.findViewById<TextView>(R.id.text_message_body)
        var msg_time = itemView.findViewById<TextView>(R.id.text_message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_card,parent,false)
        return ChatViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        val txt = chat.messages[position]
        holder.msg.text = txt.text
        holder.msg_time.text = txt.time.toString()
    }

    override fun getItemCount(): Int {
        return chat.messages.size
    }

}