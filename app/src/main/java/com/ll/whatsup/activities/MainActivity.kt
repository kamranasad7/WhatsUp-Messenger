package com.ll.whatsup.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ll.whatsup.R
import com.ll.whatsup.adapter.ChatListAdapter
import com.ll.whatsup.adapter.FragmentAdapter
import com.ll.whatsup.model.Account

class MainActivity : AppCompatActivity() {

    private val acc = Account()
    private var tabLayout:TabLayout? = null
    private var viewpager:ViewPager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tablayout)
        viewpager = findViewById(R.id.viewpager)
        viewpager?.adapter = FragmentAdapter(this,supportFragmentManager, acc.allChats)
        tabLayout?.setupWithViewPager(viewpager)

    }

}
