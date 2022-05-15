package com.ll.whatsup.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ChatAdapter
import com.ll.whatsup.model.Chat

class ChatActivity() : AppCompatActivity() {

    lateinit var chat:Chat
    lateinit var testTXT: TextView
    lateinit var msgText: EditText

    val db = Firebase.database("https://whatsup-messenger-f8f2c-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val testValRef: DatabaseReference = db.getReference("test-message")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val gson=Gson()
        chat = gson.fromJson<Chat>(intent.getStringExtra("ChatwithContact"), Chat::class.java)

        testTXT = findViewById(R.id.testTXT)
        msgText = findViewById(R.id.inputMsg)
        findViewById<Button>(R.id.sendBtn).setOnClickListener { sendMessage() }

        testValRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                testTXT.text = snapshot.getValue<String>()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }

        })
        viewChat()
    }

    private fun sendMessage() {
        testValRef.setValue(msgText.text.toString())
    }

    private fun viewChat(){
        val adp= ChatAdapter(chat)
        val view = findViewById<RecyclerView>(R.id.chatView)
        view.layoutManager = LinearLayoutManager(this)
        view.adapter=adp
    }
}