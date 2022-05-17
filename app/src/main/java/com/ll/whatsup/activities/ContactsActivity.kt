package com.ll.whatsup.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ContactsListAdapter
import com.ll.whatsup.model.Contact

class ContactsActivity() : AppCompatActivity() {

    var resultSet:Cursor? = null
    var adp:ContactsListAdapter? = null

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inf: MenuInflater = menuInflater
        inf.inflate(R.menu.contact_list_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.search_menu)

        lateinit var searchView:androidx.appcompat.widget.SearchView
        if (searchItem != null) {
            searchView= searchItem.actionView as androidx.appcompat.widget.SearchView
        }
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adp?.filter?.filter(newText)
                return true
            }
        })

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            readContact()
        }
    }

    private fun readContact(){

        resultSet = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        columns,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        val contactList = ArrayList<Contact>()
        if (resultSet != null) {
            while(resultSet!!.moveToNext()){
                val c = Contact()
                c.name = resultSet!!.getString(resultSet!!.getColumnIndexOrThrow(columns[0]))
                c.number = resultSet!!.getString(resultSet!!.getColumnIndexOrThrow(columns[1]))
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
        displayContact(contactList)
    }

    private fun displayContact(list:ArrayList<Contact>){

        val contactView = findViewById<RecyclerView>(R.id.contactRecyclerView)
        contactView.layoutManager = LinearLayoutManager(this)

        val adp = ContactsListAdapter(list){
            val gson = Gson()
            val itJSON: String = gson.toJson(it)
            startActivity(Intent(this,ChatActivity::class.java).putExtra("ChatwithContact", itJSON))
        }
        contactView.adapter = adp

    }
}
