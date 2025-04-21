package com.appuccino.entertainment_spot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
Recyclerview adapter for the Entertainment list
 */

class EntertainmentAdapter(
    private val items: List<EntertainmentItem>,
    private val onItemClick: (EntertainmentItem) -> Unit
) : RecyclerView.Adapter<EntertainmentAdapter.EntertainmentViewHolder>() {

    inner class EntertainmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textView1)
        val imageView: ImageView = itemView.findViewById(R.id.imageView1)
        val ratingText: TextView = itemView.findViewById(R.id.star_rating_number)
        val container: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntertainmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_card, parent, false)
        return EntertainmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntertainmentViewHolder, position: Int) {
        val item = items[position]
        holder.titleText.text = item.title
        holder.imageView.setImageResource(item.imageResId)
        holder.ratingText.text = item.rating.toString()

        holder.container.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}