package com.appuccino.entertainment_spot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//interface EntertainmentItem {
//    val title: String
//    val imageResId: Int
//    val imdbRating: Float
//}

//Popularmovie
//data class TmdbResponse

@Serializable
data class TmdbMovieBrief(
    @SerialName("id") val tmdbId: Int,
    @SerialName("original_title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("release_date") val releaseDate: String
)

@Serializable
data class TmdbSeriesBrief(
    @SerialName("id") val tmdbId: Int,
    @SerialName("name") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("first_air_date") val releaseDate: String,
    @SerialName("number_of_seasons") val numberOfSeasons: Int = 0,
)


@Serializable
data class CastMember(
    val name: String = "",
    val character: String = "",
    @SerialName("profile_path") val profilePath: String? = ""
)

//// Movie
//@Serializable
//data class Movie(
//    @SerialName("id")
//    val tmdbId: Int,
//
//    val imdbId: String? = null, // From Watchmode
//
//    @SerialName("original_title")
//    val title: String,
//
//    @SerialName("overview")
//    val overview: String,
//
//    @SerialName("release_date")
//    val releaseDate: String,
//
//    @SerialName("poster_path")
//    val posterUrl: String,
//
//    // TMDb runtime is in `/movie/{movie_id}` endpoint
//    @SerialName("runtime")
//    val runtime: Int,
//
//    val cast: List<CastMember> = emptyList(),
//
//    // You might need to parse genres manually if it's an array of objects
//    val genres: List<Genre>, //extract only name
//
//    // From Watchmode
//    val audienceRating: String = "NR",
//    val trailerUrl: String? = null, // Can come from TMDb videos endpoint
//    val imdbRating: Double? = null, // From Watchmode
//    val streamingPlatforms: List<String> = emptyList() // From Watchmode
//)

@Serializable
data class Movie(
    val tmdbId: Int = 0,
    val imdbId: String? = null,
    val title: String = "",
    val overview: String = "",
    val posterUrl: String = "",
    val releaseDate: String = "",
    val imdbRating: Double? = 0.0,
    val trailerUrl: String? = "",
    val cast: List<CastMember> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val runtime: Int = 0,
    val audienceRating: String = "",
    val streamingPlatforms: List<String> = emptyList()
)

@Serializable
data class Series(
    val tmdbId: Int = 0,
    val imdbId: String? = null,
    val name: String = "",
    val overview: String = "",
    val posterUrl: String = "",
    val firstAirDate: String = "",
    val imdbRating: Double? = 0.0,
    val trailerUrl: String? = "",
    val cast: List<CastMember> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val numberOfSeasons: Int = 0,
    val runtime: Int = 0,
    val audienceRating: String = "",
    val streamingPlatforms: List<String> = emptyList()
)

//@Serializable
//data class Series(
//    @SerialName("id")
//    val tmdbId: Int,
//
//    val imdbId: String?, // From Watchmode
//
//    @SerialName("name")
//    val name: String,
//
//    @SerialName("overview")
//    val overview: String,
//
//    @SerialName("first_air_date")
//    val firstAirDate: String,
//
//    @SerialName("poster_path")
//    val posterUrl: String,
//
//    val genres: List<Genre>,
//
//    @SerialName("number_of_seasons")
//    val numberOfSeasons: Int,
//
//    val audienceRating: String,
//    val trailerUrl: String?,
//    val imdbRating: Double?,
//    val streamingPlatforms: List<String>
//)

//) : EntertainmentItem
