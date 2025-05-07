package com.appuccino.entertainment_spot

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface EntertainmentItem : Parcelable {
    val title: String
    val imageResId: Int
    val imdbRating: Float
}


// Movie
@Parcelize
data class Movie(
    override val title: String,
    override val imageResId: Int,
    override val imdbRating: Float,
//    val tmdbId: Int,
//    val imdbId: String?,
//    val title: String,
//    val overview: String,
//    val releaseDate: String,
//    val posterUrl: String,
//    val runtime: Int,
//    val genres: List<String>,
//    val audienceRating: String,
//    val trailerUrl: String?,
//    val imdbRating: Double?,
//    val streamingPlatforms: List<String>
) : EntertainmentItem

// Series
@Parcelize
data class Series(
    override val title: String,
    override val imageResId: Int,
    override val imdbRating: Float,
//    val tmdbId: Int,
//    val imdbId: String?,
//    val name: String,
//    val overview: String,
//    val firstAirDate: String,
//    val posterUrl: String,
//    val genres: List<String>,
//    val numberOfSeasons: Int,
//    val audienceRating: String,
//    val trailerUrl: String?,
//    val imdbRating: Double?,
//    val streamingPlatforms: List<String>
) : EntertainmentItem
