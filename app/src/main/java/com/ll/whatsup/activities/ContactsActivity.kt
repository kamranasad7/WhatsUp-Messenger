package com.ll.whatsup.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ContactsListAdapter
import com.ll.whatsup.model.Contact


class ContactsActivity : AppCompatActivity() {

    var columns = listOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,Array(1){android.Manifest.permission.READ_CONTACTS},111)
        }
        else{
            readContact()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            readContact()
        }
    }

    private fun readContact(){

        val resultSet = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        columns,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        val contactList = ArrayList<Contact>()
        if (resultSet != null) {
            while(resultSet.moveToNext()){
                val c = Contact()
                c.name = resultSet.getString(resultSet.getColumnIndexOrThrow(columns[0]))
                c.number = resultSet.getString(resultSet.getColumnIndexOrThrow(columns[1]))
                var duplicate = false
                contactList.forEach{contact->
                    if(PhoneNumberUtils.compare(contact.number, c.number)){
                        duplicate=true
                    }
                }
                if(!duplicate){
                    contactList.add(c)
                }
            }
        }
        viewContact(contactList)
    }

    private fun viewContact(list:ArrayList<Contact>){

        val contactView = findViewById<RecyclerView>(R.id.contactRecyclerView)
        contactView.layoutManager = LinearLayoutManager(this)

        val adp = ContactsListAdapter(list)
        contactView.adapter = adp

    }
}