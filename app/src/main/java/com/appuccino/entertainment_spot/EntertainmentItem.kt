package com.appuccino.entertainment_spot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Your core data models:
 * TmdbMovieBrief / TmdbSeriesBrief for list results
 * CastMember
 * Full Movie and Series models used in Firestore + detail UI (cast, genres, runtime/seasons, ratings, trailer, streaming platforms).
 *
 **/

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