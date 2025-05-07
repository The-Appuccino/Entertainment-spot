package com.appuccino.entertainment_spot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntertainmentListAdapter(
    private val entertainmentLists: MutableList<EntertainmentList>,
    private val onItemClick: (EntertainmentList) -> Unit
) : RecyclerView.Adapter<EntertainmentListAdapter.EntertainmentListViewHolder>() {

    inner class EntertainmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listName: TextView = itemView.findViewById(R.id.listName)

        fun bind(list: EntertainmentList) {
            listName.text = list.name
            itemView.setOnClickListener {
                onItemClick(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntertainmentListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_list, parent, false)
        return EntertainmentListViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntertainmentListViewHolder, position: Int) {
        val entertainmentList = entertainmentLists[position]
        holder.bind(entertainmentList)
    }

    override fun getItemCount(): Int = entertainmentLists.size

    fun addList(newList: EntertainmentList) {
        entertainmentLists.add(newList)
        notifyItemInserted(entertainmentLists.size - 1)
    }
}
