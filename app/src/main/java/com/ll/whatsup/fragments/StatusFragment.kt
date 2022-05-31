package com.ll.whatsup.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.ll.whatsup.R

class StatusFragment : Fragment() {

    lateinit var banner : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_status, container, false)

        //LOAD ADMOB BANNER
        banner = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        banner.loadAd(adRequest)

        return view
    }

}