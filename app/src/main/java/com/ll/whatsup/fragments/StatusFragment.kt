package com.ll.whatsup.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ll.whatsup.R
import com.ll.whatsup.activities.WelcomeActivity

class StatusFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_status, container, false)
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton2).setOnClickListener{
            val i = Intent(context, WelcomeActivity::class.java)
            startActivity(i)
        }

        return view
    }

}