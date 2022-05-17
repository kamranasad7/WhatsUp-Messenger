package com.ll.whatsup.activities

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

        setTitle(chat.chatName)

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
        displayChat()
    }

    private fun sendMessage() {
        testValRef.setValue(msgText.text.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inf:MenuInflater = menuInflater
        inf.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.call_menu){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,Array(1){android.Manifest.permission.CALL_PHONE},110)
            }
            else{
                val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    applicationContext.registerReceiver(null, ifilter)
                }
                val batteryPct: Float? = batteryStatus?.let { intent ->
                    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    level * 100 / scale.toFloat()
                }
                if (batteryPct != null) {
                    if(batteryPct < 20)
                        Toast.makeText(applicationContext,"Battery is too low to make call", Toast.LENGTH_LONG).show()
                    else{
                        val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:03004031900"))
                        startActivity(i)
                    }
                }
                else{
                    val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:03004031900"))
                    startActivity(i)
                }
            }

        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 110 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:03004031900"))
            startActivity(i)
        }
    }

    private fun displayChat(){
        val adp= ChatAdapter(chat)
        val view = findViewById<RecyclerView>(R.id.chatView)
        view.layoutManager = LinearLayoutManager(this)
        view.adapter=adp
    }
}