package com.ll.whatsup.activities

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import com.ll.whatsup.model.Contact

class ChatActivity() : AppCompatActivity() {

    var chat: Chat? = null
    var contact: Contact? = null
    lateinit var msgText: EditText
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val gson = Gson()
        chat = gson.fromJson(intent.getStringExtra("chat"), Chat::class.java)
        contact = gson.fromJson(intent.getStringExtra("contact"), Contact::class.java)


        if(contact != null){
            title = contact?.name
        }
        else if(chat != null){
            title = chat?.accountNum
        }


        msgText = findViewById(R.id.inputMsg)
        findViewById<Button>(R.id.sendBtn).setOnClickListener { sendMessage() }

        displayChat()
    }

    private fun sendMessage() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inf: MenuInflater = menuInflater
        inf.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.call_menu) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    Array(1) { android.Manifest.permission.CALL_PHONE },
                    110
                )
            } else {
                val batteryStatus: Intent? =
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                        applicationContext.registerReceiver(null, ifilter)
                    }
                val batteryPct: Float? = batteryStatus?.let { intent ->
                    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    level * 100 / scale.toFloat()
                }
                if (batteryPct != null) {
                    if (batteryPct < 20)
                        Toast.makeText(
                            applicationContext,
                            "Battery is too low to make call",
                            Toast.LENGTH_LONG
                        ).show()
                    else {
                        val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:${chat?.accountNum}"))
                        startActivity(i)
                    }
                } else {
                    val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:${chat?.accountNum}"))
                    startActivity(i)
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 110 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:${chat?.accountNum}"))
            startActivity(i)
        }
    }

    private fun displayChat() {
        lateinit var adp: ChatAdapter

        if(chat != null){
            adp = ChatAdapter(chat!!)
        }
        else if(contact != null) {
            adp = ChatAdapter(Chat(contact!!.number))
        }
        else {
            adp = ChatAdapter(Chat())
            Toast.makeText(this, "Chat ot contact not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        val view = findViewById<RecyclerView>(R.id.chatView)
        view.layoutManager = LinearLayoutManager(this)
        view.adapter = adp
    }
}