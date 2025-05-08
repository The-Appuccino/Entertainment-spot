package com.appuccino.entertainment_spot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int = 0,
    val name: String = ""
)

@Serializable
data class MovieBriefListResponse(
    val results: List<TmdbMovieBrief>
)

@Serializable
data class SeriesBriefListResponse(
    val results: List<TmdbSeriesBrief>
)


@Serializable
data class CreditsResponse(
    val cast: List<CastMember>
)

@Serializable
data class VideoResult(
    val key: String,
    val site: String,
    val type: String
)

@Serializable
data class VideoResponse(
    val results: List<VideoResult>
)

@Serializable
data class ReleaseDateResult(
    @SerialName("iso_3166_1") val region: String,
    @SerialName("release_dates") val releaseDates: List<ReleaseDate>
)

@Serializable
data class ReleaseDate(
    val certification: String
)

@Serializable
data class ReleaseDateResponse(
    val results: List<ReleaseDateResult>
)

@Serializable
data class SeriesDetailResponse(
    @SerialName("id") val tmdbId: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterUrl: String = "",
    @SerialName("first_air_date") val firstAirDate: String = "",
    @SerialName("number_of_seasons") val numberOfSeasons: Int = 0,
    val genres: List<Genre> = emptyList()
)


//defined in EnntertainmentItem.kt
typealias MovieDetailResponse = Movie
//typealias SeriesDetailResponse = Series
