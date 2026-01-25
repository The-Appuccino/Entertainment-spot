package com.appuccino.entertainment_spot

import kotlinx.serialization.Serializable

/**
 * Defines the data model for the favorite items that are sent/retrieved from firestore.
 */

@Serializable
data class FavoriteItem(
    val type: String = "", // "movie or series"
    val tmdbId: Int = 0,
    val title: String = "",
    val posterUrl: String = "",
    val imdbRating: Double? = 0.0,
    val createdAt: Long = 0L
)
