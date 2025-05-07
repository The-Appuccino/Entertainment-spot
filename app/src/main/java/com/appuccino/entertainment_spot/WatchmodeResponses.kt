package com.appuccino.entertainment_spot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchmodeSearchResult(
    @SerialName("id") val watchmodeId: Int,
    @SerialName("imdb_id") val imdbId: String?
)

@Serializable
data class WatchmodeSearchResponse(
    val titleResults: List<WatchmodeSearchResult> = emptyList()
)

@Serializable
data class WatchmodeTitleDetailsResponse(
    @SerialName("user_rating") val imdbRating: Double?,
    @SerialName("critic_score") val criticScore: Double?,
    val usRating: String? // MPAA-style rating like PG-13
)

@Serializable
data class WatchmodeSource(
    val name: String,
    val type: String // "sub", "buy", "rent", etc.
)
