package com.ll.whatsup.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ll.whatsup.fragments.ChatListFragment
import com.ll.whatsup.fragments.StatusFragment
import com.ll.whatsup.model.Chat

class FragmentAdapter(var context: Context, fm:FragmentManager, var chats:ArrayList<Chat>): FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                ChatListFragment(chats)
            }
            1->{
                StatusFragment()
            }
            else -> {getItem(position)}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0->{
                "CHATS"
            }
            1->{
                "STATUS"
            }
            else -> {getPageTitle(position)}
        }
    }
}