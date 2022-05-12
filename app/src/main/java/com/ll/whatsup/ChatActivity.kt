package com.ll.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    var testTXT: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        findViewById<Button>(R.id.sendBtn).setOnClickListener { sendMessage() }
        testTXT = findViewById(R.id.testTXT)
    }

    fun sendMessage() {
        //val db = Firebase.database("https://whatsup-messenger-f8f2c-default-rtdb.asia-southeast1.firebasedatabase.app/")
        //val testValRef = db.getReference("test-message")
        //testValRef.setValue(testTXT?.text)
    }
}