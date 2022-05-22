package com.ll.whatsup.activities

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.ll.whatsup.BackgroundService
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.adapter.FragmentAdapter
import com.ll.whatsup.model.Account
import com.ll.whatsup.model.Contact


class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewpager: ViewPager? = null
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,Array(1){android.Manifest.permission.READ_CONTACTS},111)
        }
        else readContact()

        //For background service
        val i = Intent(this, BackgroundService::class.java)
        startService(i)


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
            FirebaseDB.initialize()

            currentAccountRef.get().addOnSuccessListener {
                val acc = it.getValue<Account>()
                if (acc == null) {
                    startActivity(Intent(applicationContext, CreateProfileActivity::class.java))
                    finish()
                }
                else {
                    FirebaseDB.currentAccount = acc
                    fragmentAdapter.chatList.loadChats()
                    dialog.dismiss()
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


        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tablayout)
        viewpager = findViewById(R.id.viewpager)

        fragmentAdapter = FragmentAdapter(this, supportFragmentManager)
        viewpager?.adapter = fragmentAdapter
        tabLayout?.setupWithViewPager(viewpager)

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            readContact()
        }
    }

    private fun readContact(){

        val columns = listOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        ).toTypedArray()

        val resultSet = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            columns,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        if (resultSet != null) {
            while(resultSet.moveToNext()){
                val c = Contact()
                c.name = resultSet.getString(resultSet.getColumnIndexOrThrow(columns[0]))
                c.number = resultSet.getString(resultSet.getColumnIndexOrThrow(columns[1]))
                c.number = c.number.replace(Regex("[()\\s-]+"), "")

                var duplicate = false
                FirebaseDB.contacts.forEach{contact->
                    if(PhoneNumberUtils.compare(contact.number, c.number)){
                        duplicate=true
                    }
                }
                if(!duplicate){
                    FirebaseDB.contacts.add(c)
                }
            }
        }

        FirebaseDB.setContactsHash()
        resultSet?.close()
    }
}