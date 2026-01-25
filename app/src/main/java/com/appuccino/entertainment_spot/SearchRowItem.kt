package com.appuccino.entertainment_spot

/**
 * SearchRowItem
 *
 * What this file is responsible for
 * - Defines the UI-ready model for one row in your Search RecyclerView.
 * - It is intentionally “presentation oriented”:
 *    - already includes displayTitle
 *    - already includes a posterUrl that the adapter can load
 *    - already carries a ContentType so click-navigation can decide:
 *         MOVIE -> movie detail flow
 *         SERIES -> tv show detail flow
 *
 * Why this model is helpful
 * - Prevents your UI from depending directly on Meilisearch JSON shapes.
 * - Lets you change your search backend without changing the RecyclerView binding logic.
 */


data class SearchRowItem(
    val type: ContentType,
    val tmdbId: Int,
    val displayTitle: String,
    val posterUrl: String,
    val imdbRating: Double?
)

enum class ContentType { MOVIE, SERIES }
