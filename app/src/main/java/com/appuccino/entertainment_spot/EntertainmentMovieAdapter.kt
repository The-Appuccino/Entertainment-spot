package com.appuccino.entertainment_spot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EntertainmentMovieAdapter(
    private val movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<EntertainmentMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textView1)
        val imageView: ImageView = itemView.findViewById(R.id.imageView1)
        val ratingText: TextView = itemView.findViewById(R.id.star_rating_number)
        val container: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_card, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.titleText.text = movie.title
        holder.ratingText.text = movie.imdbRating?.toString() ?: "N/A"

        val baseUrl = "https://image.tmdb.org/t/p/w500"
        Glide.with(holder.itemView.context)
            .load(baseUrl + movie.posterUrl)
            .into(holder.imageView)

        holder.container.setOnClickListener { onItemClick(movie) }
    }

    override fun getItemCount(): Int = movies.size
}