package com.ll.whatsup

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.model.Account
import com.ll.whatsup.model.Chat
import com.ll.whatsup.model.Contact
import com.ll.whatsup.model.Message

object FirebaseDB {
    val url = "https://whatsup-messenger-f8f2c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    val db = Firebase.database(url)
    lateinit var currentAccount: Account
    val contacts: ArrayList<Contact> = ArrayList()
    val contactsHash: HashMap<String, Contact> = HashMap()
    lateinit var chatsRef: DatabaseReference

    fun initialize() {
        chatsRef = db.getReference("accounts/${Firebase.auth.currentUser?.phoneNumber}")
    }

    fun getAccount(accountNo: String, onSuccessListener: OnSuccessListener<DataSnapshot>) {
        val accountRef = db.getReference("accounts/${accountNo}")
        accountRef.get().addOnSuccessListener(onSuccessListener)
    }

    fun getAccountReference(accountNo: String): DatabaseReference {
        return db.getReference("accounts/${accountNo}")
    }


    fun setContactsHash() {
        for(c in contacts){
            contactsHash[c.number] = c
        }
    }


}