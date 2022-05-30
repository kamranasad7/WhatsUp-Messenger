package com.ll.whatsup.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.R
import com.ll.whatsup.model.Account
import java.util.*
import kotlin.collections.HashMap

class CreateProfileActivity : AppCompatActivity() {
    lateinit var confirmBtn: Button
    lateinit var profileName: EditText
    lateinit var profileBio: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        confirmBtn = findViewById(R.id.create_profile_confirm)
        profileName = findViewById(R.id.create_profile_name)
        profileBio = findViewById(R.id.create_profile_bio)

        profileName.doAfterTextChanged { confirmBtn.isEnabled = verifyInfo() }
        profileBio.doAfterTextChanged { confirmBtn.isEnabled = verifyInfo() }

        confirmBtn.setOnClickListener {

            //save account in db
            val user = Firebase.auth.currentUser
            val newAccount = Account(profileName.text.toString(), user?.phoneNumber.toString(),
                profileBio.text.toString(), "")

            val db = Firebase.database("https://whatsup-messenger-f8f2c-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val accounts = db.getReference("accounts")
            //val newAccountRef = accounts.child(newAccount.number)
            //newAccountRef.setValue(newAccount)

            val map = HashMap<String, Any>()
            map["${newAccount.number}/number"] = newAccount.number
            map["${newAccount.number}/profileName"] = newAccount.profileName
            map["${newAccount.number}/profilePicture"] = newAccount.profilePicture
            map["${newAccount.number}/bio"] = newAccount.bio
            accounts.updateChildren(map)

            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    fun verifyInfo(): Boolean {
        return profileName.text.toString().isNotBlank() && profileBio.text.toString().isNotBlank()
    }
}