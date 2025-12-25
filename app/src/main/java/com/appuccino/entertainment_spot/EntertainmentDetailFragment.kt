package com.appuccino.entertainment_spot

import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale
import java.text.SimpleDateFormat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


/**
 *Detail screen for either a Movie or Series. Receives the object via JSON args (ARG_MOVIE / ARG_SERIES),
 * populates UI (title, genre, summary, runtime/seasons, rating, release date, platforms),
 * shows cast in a horizontal RecyclerView, and cues the YouTube trailer.
 **/


class EntertainmentDetailFragment : Fragment(R.layout.fragment_entertainment_detail) {

    // Holds a live Firestore listener so we can stop it when the Fragment view goes away.
    private var favoriteListener: ListenerRegistration? = null

    // Firestore-backed favorite state for the currently displayed item.
    private var isBookmarked: Boolean = false


    // Helper function to format the date
    private fun formatDate(isoDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.US)
            val date = inputFormat.parse(isoDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            isoDate // fallback to original if parsing fails
        }
    }




    private fun bindBookmarkBehavior(
        view: View,
        type: String,
        tmdbId: Int,
        title: String,
        posterUrl: String,
        imdbRating: Double?
    ) {
        val bookmarkButton = view.findViewById<ImageButton>(R.id.bookmarkButton)

        // 1) Start listening to Firestore to know if this item is currently favorited
        // Remove any previous listener first (important when fragment is reused)
        favoriteListener?.remove()
        favoriteListener = FirestoreFavoriteListExchange.listenIsFavorited(
            type = type,
            tmdbId = tmdbId,
            onChanged = { favorited ->
                // Firestore is the source of truth
                isBookmarked = favorited

                // Update icon based on Firestore state
                bookmarkButton.setImageResource(
                    if (favorited) R.drawable.ic_baseline_bookmark_added_24
                    else R.drawable.ic_baseline_bookmark_border_24
                )
            },
            onError = { e ->
                Log.w("DetailFragment", "listenIsFavorited error", e)
            }
        )

        // 2) Toggle favorite on click
        bookmarkButton.setOnClickListener {
            if (isBookmarked) {
                // Remove favorite doc
                FirestoreFavoriteListExchange.removeFavorite(type, tmdbId) { success, error ->
                    if (!success) {
                        Log.w("DetailFragment", "removeFavorite failed", error)
                    }
                    // No need to manually flip icon here — the listener will update it.
                }
            } else {
                // Add favorite doc (thin model: only what list needs)
                val favoriteItem = FavoriteItem(
                    type = type,
                    tmdbId = tmdbId,
                    title = title,
                    posterUrl = posterUrl,
                    imdbRating = imdbRating,
                    createdAt = 0L // Exchange will fill it if 0
                )

                FirestoreFavoriteListExchange.addFavorite(favoriteItem) { success, error ->
                    if (!success) {
                        Log.w("DetailFragment", "addFavorite failed", error)
                    }
                    // Listener will update icon.
                }
            }
        }
    }


    companion object {
        // Fragment arg keys
        private const val ARG_MOVIE = "movie"
        private const val ARG_SERIES = "series"

        // Favorite type constants (keep consistent across app + Firestore)
        private const val TYPE_MOVIE = "movie"
        private const val TYPE_SERIES = "series"

        // Logging tag
        private const val TAG = "EntertainmentDetail"

        fun newInstance(movie: Movie): EntertainmentDetailFragment {
            val args = Bundle().apply {
                putString(ARG_MOVIE, Json.encodeToString(movie))
            }
            return EntertainmentDetailFragment().apply { arguments = args }
        }

        fun newInstance(series: Series): EntertainmentDetailFragment {
            val args = Bundle().apply {
                putString(ARG_SERIES, Json.encodeToString(series))
            }
            return EntertainmentDetailFragment().apply { arguments = args }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set up RecyclerView for cast
        val castRecyclerView = view.findViewById<RecyclerView>(R.id.castRecyclerView)

        val youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtubePlayerView)
        lifecycle.addObserver(youTubePlayerView)

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val ratingTextView = view.findViewById<TextView>(R.id.ratingTextView)
        val posterImageView = view.findViewById<ImageView>(R.id.posterImageView)
        val backgroundImageView = view.findViewById<ImageView>(R.id.backgroundImageView)
        val durationTextView = view.findViewById<TextView>(R.id.durationTextView)
        val releaseDateTextView = view.findViewById<TextView>(R.id.releaseDateTextView)
        val summaryTextView = view.findViewById<TextView>(R.id.summaryTextView)
        val genreTextView = view.findViewById<TextView>(R.id.genreTextView)
        val platformNames = view.findViewById<TextView>(R.id.platformName)


        //Allows user to go back to the previous screen using the back arrow button icon
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        arguments?.getString(ARG_MOVIE)?.let { json ->
            val movie = Json.decodeFromString<Movie>(json)

            // Bookmark integration (Firestore-backed)
            bindBookmarkBehavior(
                view = view,
                type = TYPE_MOVIE,
                tmdbId = movie.tmdbId,
                title = movie.title,
                posterUrl = movie.posterUrl,
                imdbRating = movie.imdbRating
            )


            titleTextView.text = movie.title
            //ratingTextView.text = movie.imdbRating?.toString() ?: "N/A"
            genreTextView.text = movie.genres.joinToString(", ") { it.name}
            summaryTextView.text = movie.overview
            durationTextView.text = "${movie.runtime} min"
            ratingTextView.text = movie.audienceRating
            releaseDateTextView.text = formatDate(movie.releaseDate)
            platformNames.text = movie.streamingPlatforms
                .take(6) // Get first 6 items safely, even if list has fewer
                .joinToString(", ")

            val baseUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(this).load(baseUrl + movie.posterUrl).into(posterImageView)
            Glide.with(this).load(baseUrl + movie.posterUrl).into(backgroundImageView)

            castRecyclerView.adapter = EntertainmentCastAdapter(movie.cast)
            castRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            // Load the trailer
            val trailerKey = movie.trailerUrl?.substringAfter("v=") // if using full URL
            trailerKey?.let {
                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(it, 0f) // cue instead of auto-play
                    }
                })
            }
        }

        arguments?.getString(ARG_SERIES)?.let { json ->
            val series = Json.decodeFromString<Series>(json)

            // Bookmark integration (Firestore-backed)
            bindBookmarkBehavior(
                view = view,
                type = TYPE_SERIES,
                tmdbId = series.tmdbId,
                title = series.name,          // normalize to "title" in FavoriteItem
                posterUrl = series.posterUrl,
                imdbRating = series.imdbRating
            )


            titleTextView.text = series.name
            //ratingTextView.text = series.imdbRating?.toString() ?: "N/A"
            genreTextView.text = series.genres.joinToString(", ") { it.name}
            summaryTextView.text = series.overview
            durationTextView.text = "${series.numberOfSeasons} seasons"
            ratingTextView.text = series.audienceRating
            releaseDateTextView.text = formatDate(series.firstAirDate)
            platformNames.text = series.streamingPlatforms
                .take(6) // Get first 6 items safely, even if list has fewer
                .joinToString(", ")

            val baseUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(this).load(baseUrl + series.posterUrl).into(posterImageView)
            Glide.with(this).load(baseUrl + series.posterUrl).into(backgroundImageView)

            castRecyclerView.adapter = EntertainmentCastAdapter(series.cast)
            castRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            // Load the trailer
            val trailerKey = series.trailerUrl?.substringAfter("v=") // if using full URL
            trailerKey?.let {
                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(it, 0f) // cue instead of auto-play
                    }
                })
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Stop listening when the view is destroyed to avoid leaks and duplicate listeners.
        favoriteListener?.remove()
        favoriteListener = null
    }
}