package com.ll.whatsup.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ll.whatsup.FirebaseDB
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ContactsListAdapter
import com.ll.whatsup.model.Contact

class ContactsActivity : AppCompatActivity() {
    
    var adp: ContactsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Contacts Permission denied", Toast.LENGTH_LONG).show()
        }

        displayContact(FirebaseDB.contacts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inf: MenuInflater = menuInflater
        inf.inflate(R.menu.contact_list_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.search_menu)

        lateinit var searchView: SearchView
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
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


    private fun displayContact(list:ArrayList<Contact>){

        val contactView = findViewById<RecyclerView>(R.id.contactRecyclerView)
        contactView.layoutManager = LinearLayoutManager(this)

        adp = ContactsListAdapter(list){
            val gson = Gson()
            val contactJSON: String = gson.toJson(it)

            val i = Intent(this,ChatActivity::class.java)
            i.putExtra("phone", it.number)
            i.putExtra("contact", contactJSON)
            startActivity(i)
        }

        contactView.adapter = adp

    }
}
