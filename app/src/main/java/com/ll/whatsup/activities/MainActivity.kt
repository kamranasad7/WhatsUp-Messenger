package com.ll.whatsup.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ChatListAdapter
import com.ll.whatsup.adapter.FragmentAdapter
import com.ll.whatsup.model.Account

class MainActivity : AppCompatActivity() {

    private var currentAccount: Account? = null
    private var tabLayout: TabLayout? = null
    private var viewpager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        val db = Firebase.database("https://whatsup-messenger-f8f2c-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val accounts = db.getReference("accounts")

        val user = Firebase.auth.currentUser
        if (user == null) {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            finish()
        }

        //CHECK IF USER WHO IS LOGGED IN HAS CREATED A ACCOUNT(PROFILE)... IF NOT THEN REDIRECT
        else {
            val currentAccountRef = accounts.child(user.phoneNumber.toString())
            currentAccountRef.get().addOnSuccessListener {
                currentAccount = it.getValue<Account>()
                if (currentAccount == null) {
                    startActivity(Intent(applicationContext, CreateProfileActivity::class.java))
                    finish()
                }
            }
        }


        //CHECK IF USER IS LOGGED IN... IF NOT THEN REDIRECT TO SIGNUP
        Firebase.auth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                finishAffinity()
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tablayout)
        viewpager = findViewById(R.id.viewpager)
        viewpager?.adapter = FragmentAdapter(this, supportFragmentManager)
        tabLayout?.setupWithViewPager(viewpager)
    }
}