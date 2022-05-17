package com.ll.whatsup

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.model.Account

class FirebaseDB {

    val db = Firebase.database("https://whatsup-messenger-f8f2c-default-rtdb.asia-southeast1.firebasedatabase.app/")

    companion object LoginAccount {
        lateinit var acc: Account
    }

    fun getAccount(accountNo: String): Account? {

        val accVal = db.getReference("accounts/${accountNo}")
        accVal.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    acc = snapshot.getValue<Account>()!!
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        val acc: DataSnapshot = await(accVal.get())
        return acc.getValue<Account>()
    }

    fun addMsgtoChat() {

    }


}