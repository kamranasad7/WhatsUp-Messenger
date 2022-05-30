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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ChatAdapter
import com.ll.whatsup.model.Chat
import com.ll.whatsup.model.Contact
import com.ll.whatsup.model.Message
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    lateinit var chatRef: DatabaseReference
    lateinit var msgsRef: DatabaseReference
    lateinit var recvChatRef: DatabaseReference
    lateinit var view: RecyclerView

    var chat: Chat? = null
    var contact: Contact? = null
    var chatStarted = false

    lateinit var msgText: EditText
    lateinit var adp: ChatAdapter
    private lateinit var msgsLoadDialog: Dialog
    private lateinit var dialog: Dialog
    private lateinit var dialog2: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        //GET VALUES FROM INTENT
        val gson = Gson()
        val number = intent.getStringExtra("phone")
        chatRef = FirebaseDB.getChatReference(number!!)
        msgsRef = chatRef.child("messages")
        recvChatRef = FirebaseDB.getReceiverChatReference(number, FirebaseDB.currentAccount.number)
        chat = FirebaseDB.currentAccount.chats[number]
        contact = gson.fromJson(intent.getStringExtra("contact"), Contact::class.java)

        //CLEAR MESSAGES
        chat?.messages?.clear()


        // SET TITLE
        if(contact != null){
            title = contact?.name
        }
        else if(chat != null){
            title = chat?.accountNum
        }


        msgText = findViewById(R.id.inputMsg)
        findViewById<Button>(R.id.sendBtn).setOnClickListener { sendMessage() }

        displayChat()


        //CHECK IF CHAT OBJECT EXISTS IN DB ON RECEIVER SIDE
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.findViewById<TextView>(R.id.loading_dialog_text).text = "Loading..."
        dialog.setCancelable(false)
        dialog.show()
        recvChatRef.get().addOnSuccessListener {
            try{
                val recvChat = it.getValue<Chat>()
                if(recvChat != null){
                    chatStarted = true
                }
                dialog.dismiss()
            }
            catch(e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }


        msgsRef.orderByKey().addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                try{
                    val result = snapshot.getValue<Message>()

                    if(chat?.messages?.get(snapshot.key) == null){
                        chat?.messages?.put(snapshot.key!!, result!!)
                        adp.msgList.add(result!!)
                        adp.notifyItemInserted(adp.msgList.size - 1)
                        view.scrollToPosition(adp.msgList.size - 1)
                    }
                }
                catch (e: Exception) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) { }

        })
    }

    private fun sendMessage() {

        if(msgText.text.isNullOrBlank()) { return }

        val message = Message(msgText.text.toString(), Calendar.getInstance().time, FirebaseDB.currentAccount.number, adp.chat.accountNum)

        if(!chatStarted) {
            dialog2 = Dialog(this)
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog2.setContentView(R.layout.dialog_loading)
            dialog2.findViewById<TextView>(R.id.loading_dialog_text).text = "Starting chat"
            dialog2.setCancelable(false)
            dialog2.show()

            recvChatRef.setValue(Chat(FirebaseDB.currentAccount.number)).addOnSuccessListener {
                dialog2.dismiss()
                send(message)
            }
        }

        else send(message)
    }


    private fun send(message: Message) {
        if(chat == null){
            chat = adp.chat
            chatRef.setValue(adp.chat).addOnSuccessListener {
                val msgRef = msgsRef.push()
                msgRef.setValue(message)
                val recvMsg = recvChatRef.child("messages").push()
                recvMsg.setValue(message)
                dialog.dismiss()
            }

            dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_loading)
            dialog.findViewById<TextView>(R.id.loading_dialog_text).text = "Starting chat"
            dialog.setCancelable(false)
            dialog.show()
        }
        else{
            val msgRef = msgsRef.push()
            msgRef.setValue(message)
            val recvMsg = recvChatRef.child("messages").push()
            recvMsg.setValue(message)
        }

        msgText.text.clear()
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

        if(chat != null){
            adp = ChatAdapter(chat!!)
        }
        else if(contact != null) {
            adp = ChatAdapter(Chat(contact!!.number))
        }
        else {
            adp = ChatAdapter(Chat())
            Toast.makeText(this, "Chat or contact not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        view = findViewById(R.id.chatView)
        view.layoutManager = LinearLayoutManager(this)
        view.adapter = adp
    }

    override fun onDestroy() {
        super.onDestroy()

        //msgsRef.removeEventListener()
    }
}