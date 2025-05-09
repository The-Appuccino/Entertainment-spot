package com.appuccino.entertainment_spot

import android.os.Bundle
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

class EntertainmentDetailFragment : Fragment(R.layout.fragment_entertainment_detail) {
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

    companion object {
        private const val ARG_MOVIE = "movie"
        private const val ARG_SERIES = "series"

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

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        arguments?.getString(ARG_MOVIE)?.let { json ->
            val movie = Json.decodeFromString<Movie>(json)
            titleTextView.text = movie.title
            //ratingTextView.text = movie.imdbRating?.toString() ?: "N/A"
            summaryTextView.text = movie.overview
            durationTextView.text = "${movie.runtime} min"
            ratingTextView.text = movie.audienceRating
            releaseDateTextView.text = formatDate(movie.releaseDate)

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
            titleTextView.text = series.name
            ratingTextView.text = series.imdbRating?.toString() ?: "N/A"
            summaryTextView.text = series.overview
            durationTextView.text = "${series.numberOfSeasons} seasons"
            ratingTextView.text = series.audienceRating
            releaseDateTextView.text = formatDate(series.firstAirDate)

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
}






























//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//
//class EntertainmentDetailFragment: Fragment(R.layout.fragment_entertainment_detail) {
//
//    companion object {
//        private const val ARG_TITLE = "title"
//        private const val ARG_IMAGE = "image"
//        private const val ARG_RATING = "rating"
//
//        fun newInstance(item: EntertainmentItem): EntertainmentDetailFragment {
//            val args = Bundle().apply {
//                putString(ARG_TITLE, item.title)
//                putInt(ARG_IMAGE, item.imageResId)
//                putFloat(ARG_RATING, item.imdbRating)
//            }
//            return EntertainmentDetailFragment().apply {
//                arguments = args
//            }
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val title = arguments?.getString(ARG_TITLE)
//        val imageRes = arguments?.getInt(ARG_IMAGE)
//        val rating = arguments?.getFloat(ARG_RATING)
//
//        view.findViewById<TextView>(R.id.titleTextView).text = title
//        view.findViewById<ImageView>(R.id.posterImageView).setImageResource(imageRes ?: 0)
//        view.findViewById<ImageView>(R.id.backgroundImageView).setImageResource(imageRes ?: 0)
//        view.findViewById<TextView>(R.id.ratingTextView).text = rating.toString()
//    }
//}