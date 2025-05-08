package com.appuccino.entertainment_spot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EntertainmentSeriesAdapter(
    private val seriesList: List<Series>,
    private val onItemClick: (Series) -> Unit
) : RecyclerView.Adapter<EntertainmentSeriesAdapter.SeriesViewHolder>() {

    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textView1)
        val imageView: ImageView = itemView.findViewById(R.id.imageView1)
        val ratingText: TextView = itemView.findViewById(R.id.star_rating_number)
        val container: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_card, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val series = seriesList[position]
        holder.titleText.text = series.name
        holder.ratingText.text = series.imdbRating?.toString() ?: "N/A"

        val baseUrl = "https://image.tmdb.org/t/p/w500"
        Glide.with(holder.itemView.context)
            .load(baseUrl + series.posterUrl)
            .into(holder.imageView)

        holder.container.setOnClickListener { onItemClick(series) }
    }

    override fun getItemCount(): Int = seriesList.size
}