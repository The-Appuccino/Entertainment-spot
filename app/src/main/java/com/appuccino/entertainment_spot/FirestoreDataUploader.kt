package com.appuccino.entertainment_spot

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//import com.appuccino.entertainment_spot.BuildConfig


object FirestoreDataUploader {

    private const val TAG = "FirestoreUploader"
    private const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
    private const val WATCHMODE_API_KEY = BuildConfig.WATCHMODE_API_KEY
    
    private val tmdb = RetrofitClient.tmdbService
    private val watchmode = RetrofitClient.watchmodeService

    suspend fun uploadPopularMovies() = withContext(Dispatchers.IO) {
        val firestore = FirebaseFirestore.getInstance()
        try {
            val popularMovies = tmdb.getPopularMovies(TMDB_API_KEY).results
            Log.d(TAG, "Popular Movie result: $popularMovies")

            for (movie in popularMovies) {
                Log.d(TAG, "movie: $movie")
                val movieId = movie.tmdbId

                val details = tmdb.getMovieDetails(movieId, TMDB_API_KEY)
                val credits = tmdb.getMovieCredits(movieId, TMDB_API_KEY)
                val videos = tmdb.getMovieVideos(movieId, TMDB_API_KEY)
                val releaseDates = tmdb.getMovieReleaseDates(movieId, TMDB_API_KEY)

                val trailerKey: String? = videos.results.firstOrNull { video: com.appuccino.entertainment_spot.VideoResult ->
                    video.site == "YouTube" && video.type == "Trailer"
                }?.key
                val trailerUrl = trailerKey?.let { key: String -> "https://www.youtube.com/watch?v=$key" }

                val audienceRating = releaseDates.results
                    .firstOrNull { result: ReleaseDateResult -> result.region == "US" }
                    ?.releaseDates
                    ?.firstOrNull { date: ReleaseDate -> date.certification.isNotBlank() }
                    ?.certification ?: "NR"

                //Watchmode
//                val rawSearchResponse = watchmode.searchByTmdbId(movieId.toString(), WATCHMODE_API_KEY)
//                Log.d(TAG, "Raw watchmode response: $rawSearchResponse")
                val watchSearch = watchmode.searchByTmdbId(
                    field = "tmdb_movie_id",
                    tmdbId = movieId,
                    apiKey = WATCHMODE_API_KEY
                )
                val wmResult = watchSearch.titleResults.firstOrNull()
                val imdbId = wmResult?.imdbId
                val watchmodeId = wmResult?.watchmodeId
                Log.d(TAG, "Watchmode ID: $watchmodeId")

                val imdbRating = watchmodeId?.let { id: Int ->
                    val details = watchmode.getWatchmodeTitleDetails(id, WATCHMODE_API_KEY)
                    Log.d(TAG, "Watchmode Details Response: $details")
                    details.imdbRating
                }
                Log.d(TAG, "Final imdbRating: ${imdbRating}")

                val streamingPlatforms = watchmodeId?.let { id: Int ->
                    watchmode.getWatchmodeSources(id, WATCHMODE_API_KEY)
                        .map { source: com.appuccino.entertainment_spot.WatchmodeSource -> source.name }
                        .distinct()
                } ?: emptyList()

                val genreNames = details.genres
                val topCast = credits.cast.take(8)

                val enrichedMovie = Movie(
                    tmdbId = details.tmdbId,
                    imdbId = imdbId,
                    title = details.title,
                    overview = details.overview,
                    releaseDate = details.releaseDate,
                    posterUrl = details.posterUrl,
                    runtime = details.runtime,
                    cast = topCast,
                    genres = genreNames,
                    audienceRating = audienceRating,
                    trailerUrl = trailerUrl,
                    imdbRating = imdbRating,
                    streamingPlatforms = streamingPlatforms
                )
                //Log.d(TAG, "Enrichred Movie List: ${enrichedMovie}")

                firestore.collection("movies")
                    .document(details.tmdbId.toString())
                    .set(enrichedMovie)
                    .addOnSuccessListener { _: Void? ->
                        Log.d(TAG, "Uploaded movie: ${details.title}")
                    }
                    .addOnFailureListener { error: Exception ->
                        Log.e(TAG, "Failed to upload ${details.title}: ${error.message}")
                    }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching/uploading movies: ${e.message}", e)
        }
    }

    suspend fun uploadPopularSeries() = withContext(Dispatchers.IO) {
        val firestore = FirebaseFirestore.getInstance()
        try {
            val popularSeries = tmdb.getPopularSeries(TMDB_API_KEY).results
            Log.d(TAG, "Popular Series result: $popularSeries")

            for (series in popularSeries) {
                //Log.d(TAG, "series: $series")
                val seriesId = series.tmdbId

                val details: SeriesDetailResponse = tmdb.getSeriesDetails(seriesId, TMDB_API_KEY)
                val credits = tmdb.getSeriesCredits(seriesId, TMDB_API_KEY)
                val videos = tmdb.getSeriesVideos(seriesId, TMDB_API_KEY)

                val trailerKey: String? = videos.results.firstOrNull { video: com.appuccino.entertainment_spot.VideoResult ->
                    video.site == "YouTube" && video.type == "Trailer"
                }?.key
                val trailerUrl = trailerKey?.let { key: String -> "https://www.youtube.com/watch?v=$key" }

                val watchSearch = watchmode.searchByTmdbId(
                    field = "tmdb_tv_id",
                    tmdbId = seriesId,
                    apiKey = WATCHMODE_API_KEY
                )

                val wmResult = watchSearch.titleResults.firstOrNull()
                val imdbId = wmResult?.imdbId
                val watchmodeId = wmResult?.watchmodeId

                val imdbRating = watchmodeId?.let { id: Int ->
                    watchmode.getWatchmodeTitleDetails(id, WATCHMODE_API_KEY).imdbRating
                }

                val streamingPlatforms = watchmodeId?.let { id: Int ->
                    watchmode.getWatchmodeSources(id, WATCHMODE_API_KEY)
                        .map { source: com.appuccino.entertainment_spot.WatchmodeSource -> source.name }
                        .distinct()
                } ?: emptyList()

                val genreNames = details.genres
                val topCast = credits.cast.take(8)

                val enrichedSeries = Series(
                    tmdbId = details.tmdbId,
                    imdbId = imdbId,
                    name = details.name,
                    overview = details.overview,
                    firstAirDate = details.firstAirDate,
                    posterUrl = details.posterUrl,
                    genres = details.genres,
                    numberOfSeasons = details.numberOfSeasons,
                    audienceRating = "NR",
                    trailerUrl = trailerUrl,
                    imdbRating = imdbRating,
                    streamingPlatforms = streamingPlatforms,
                    cast = topCast
                )


                firestore.collection("series")
                    .document(details.tmdbId.toString())
                    .set(enrichedSeries)
                    .addOnSuccessListener { _: Void? ->
                        Log.d(TAG, "Uploaded series: ${details.name}")
                    }
                    .addOnFailureListener { error: Exception ->
                        Log.e(TAG, "Failed to upload ${details.name}: ${error.message}")
                    }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching/uploading series: ${e.message}", e)
        }
    }
}