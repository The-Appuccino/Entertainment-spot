package com.appuccino.entertainment_spot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * EntertainmentSearchAdapter
 *
 * What this file is responsible for
 * - Binds List<SearchRowItem> into your RecyclerView using item_entertainment_card.
 * - Handles image loading (poster) and text binding (title, rating, etc.).
 * - Exposes click events so the Fragment can navigate to the correct detail screen.
 *
 * Why an adapter exists here
 * - RecyclerView needs a dedicated binding layer.
 * - It’s reusable: you can use the same adapter anywhere you display mixed search results.
 *
 * Typical flow
 * Fragment receives new results -> adapter.submitList(results) / notifyDataSetChanged()
 * RecyclerView renders updated cards.
 */

class EntertainmentSearchAdapter(
    private var items: List<SearchRowItem>,
    private val onClick: (SearchRowItem) -> Unit
) : RecyclerView.Adapter<EntertainmentSearchAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textView1)
        val imageView: ImageView = itemView.findViewById(R.id.imageView1)
        val ratingText: TextView = itemView.findViewById(R.id.star_rating_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_card, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.titleText.text = item.displayTitle
        holder.ratingText.text = item.imdbRating?.toString() ?: "N/A"

        val baseUrl = "https://image.tmdb.org/t/p/w500"
        Glide.with(holder.itemView.context)
            .load(baseUrl + item.posterUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<SearchRowItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}