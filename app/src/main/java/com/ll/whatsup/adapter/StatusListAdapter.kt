package com.ll.whatsup.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ll.whatsup.model.Status

class StatusListAdapter(var statusList: ArrayList<ArrayList<Status>>):
    RecyclerView.Adapter<StatusListAdapter.StatusListViewHolder>() {

    class StatusListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatusListAdapter.StatusListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: StatusListAdapter.StatusListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}