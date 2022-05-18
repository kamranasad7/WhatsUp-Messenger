package com.ll.whatsup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.model.Chat
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(var chat : Chat) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SENDER = 0
    private val RECEIVER = 1

    class SenderMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var msg: TextView = itemView.findViewById(R.id.text_message_body)
        var msgTime: TextView = itemView.findViewById(R.id.text_message_time)
    }

    class ReceiverMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var msg = itemView.findViewById<TextView>(R.id.text_message_body_rcv)
        var msgTime = itemView.findViewById<TextView>(R.id.text_message_time_rcv)
    }

    override fun getItemViewType(position: Int): Int {
        return if(chat.messages[position].senderNum == FirebaseDB.currentAccount.number)  SENDER else RECEIVER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            SENDER -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_send_card,parent,false)
                SenderMessageViewHolder(v)
            }

            RECEIVER -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_recieve_card,parent,false)
                ReceiverMessageViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_recieve_card,parent,false)
                ReceiverMessageViewHolder(v)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            SENDER -> {
                val senderHolder = holder as SenderMessageViewHolder
                val message = chat.messages[position]
                holder.msg.text = message.text
                val dateFormat = SimpleDateFormat("hh.mm aa", Locale.US)
                holder.msgTime.text = dateFormat.format(message.time).toString()
            }
            RECEIVER -> {
                val receiverHolder = holder as ReceiverMessageViewHolder
                val message = chat.messages[position]
                holder.msg.text = message.text
                val dateFormat = SimpleDateFormat("hh.mm aa", Locale.US)
                holder.msgTime.text = dateFormat.format(message.time).toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return chat.messages.size
    }
}