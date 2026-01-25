package com.appuccino.entertainment_spot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class EntertainmentFavoriteListFragment : Fragment(R.layout.fragment_entertainment_favorite_list) {

    private lateinit var recyclerView: RecyclerView

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private var favoritesListener: ListenerRegistration? = null

    companion object {
        private const val TAG = "EntertainmentFavoriteList"
        private const val TYPE_MOVIE = "movie"
        private const val TYPE_SERIES = "series"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.favoritesListRV)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        // Listen to ALL favorites (movies + series together)
        startListeningToAllFavorites()
    }

    private fun startListeningToAllFavorites() {
        favoritesListener?.remove()
        favoritesListener = null

        favoritesListener = FirestoreFavoriteListExchange.listenFavorites(
            onChanged = { favorites ->
                Log.d(TAG, "Favorites updated. count=${favorites.size}")

                // Optional: newest first if you stored createdAt
                val sorted = favorites.sortedByDescending { it.createdAt }

                recyclerView.adapter = FavoriteMixedAdapter(sorted) { favorite ->
                    // On click: route to correct full doc, then open detail
                    when (favorite.type) {
                        TYPE_MOVIE -> openDetailForMovie(favorite.tmdbId)
                        TYPE_SERIES -> openDetailForSeries(favorite.tmdbId)
                        else -> Log.w(TAG, "Unknown favorite type: ${favorite.type}")
                    }
                }
            },
            onError = { e ->
                Log.w(TAG, "listenFavorites error", e)
            }
        )

        if (favoritesListener == null) {
            Log.w(TAG, "Favorites listener not started (no signed-in user?)")
            recyclerView.adapter = FavoriteMixedAdapter(emptyList()) { }
        }
    }

    /**
     * Fetches the full Movie doc from Firestore, then navigates to detail fragment.
     * Assumes: movies/{tmdbId}
     */
    private fun openDetailForMovie(tmdbId: Int) {
        db.collection("movies")
            .document(tmdbId.toString())
            .get()
            .addOnSuccessListener { doc ->
                val movie = doc.toObject(Movie::class.java)
                if (movie == null) {
                    Log.w(TAG, "Movie doc missing or failed to parse for tmdbId=$tmdbId")
                    return@addOnSuccessListener
                }

                val fragment = EntertainmentDetailFragment.newInstance(movie)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "openDetailForMovie failed tmdbId=$tmdbId", e)
            }
    }

    /**
     * Fetches the full Series doc from Firestore, then navigates to detail fragment.
     * Assumes: series/{tmdbId}
     */
    private fun openDetailForSeries(tmdbId: Int) {
        db.collection("series")
            .document(tmdbId.toString())
            .get()
            .addOnSuccessListener { doc ->
                val series = doc.toObject(Series::class.java)
                if (series == null) {
                    Log.w(TAG, "Series doc missing or failed to parse for tmdbId=$tmdbId")
                    return@addOnSuccessListener
                }

                val fragment = EntertainmentDetailFragment.newInstance(series)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "openDetailForSeries failed tmdbId=$tmdbId", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesListener?.remove()
        favoritesListener = null
    }

    /**
     * Minimal adapter that can display a MIXED list (movies + series).
     * Uses the same card layout you already use: item_entertainment_card.xml
     * - title -> FavoriteItem.title
     * - rating -> FavoriteItem.imdbRating
     * - poster -> TMDb baseUrl + FavoriteItem.posterUrl
     */
    private class FavoriteMixedAdapter(
        private val items: List<FavoriteItem>,
        private val onClick: (FavoriteItem) -> Unit
    ) : RecyclerView.Adapter<FavoriteMixedAdapter.VH>() {

        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleText: TextView = itemView.findViewById(R.id.textView1)
            val imageView: ImageView = itemView.findViewById(R.id.imageView1)
            val ratingText: TextView = itemView.findViewById(R.id.star_rating_number)
            val container: View = itemView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_entertainment_card, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]

            holder.titleText.text = item.title
            holder.ratingText.text = item.imdbRating?.toString() ?: "N/A"

            val baseUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(holder.itemView.context)
                .load(baseUrl + item.posterUrl)
                .into(holder.imageView)

            holder.container.setOnClickListener { onClick(item) }
        }

        override fun getItemCount(): Int = items.size
    }
}
