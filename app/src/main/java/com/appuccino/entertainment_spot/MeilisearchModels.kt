package com.appuccino.entertainment_spot

import kotlinx.serialization.Serializable

/**
 * MeilisearchModels
 *
 * What this file is responsible for
 * - Holds the Kotlin data classes that represent:
 *    1) what you send to Meilisearch (request body)
 *    2) what you receive back (response payload)
 *
 * Why these models exist
 * - Retrofit + your JSON converter need strongly-typed shapes to serialize/deserialize JSON.
 * - Meilisearch returns a generic "hits" array; your models describe what each hit looks like.
 *
 * Design intent
 * - Keep these models close to the API layer because they mirror Meilisearch JSON.
 * - Do NOT make UI depend directly on these models long-term.
 *   Instead: Repository maps "hit" objects -> app-friendly models (SearchRowItem).
 *
 * Common pattern used here
 * - Request model like: query string + pagination limit
 * - Response model like: hits list + meta (offset/limit/processingTime/query)
 */


@Serializable
data class MeiliSearchRequest(
    val q: String,
    val limit: Int = 30
)

@Serializable
data class MeiliSearchResponse(
    val hits: List<MeiliEntertainmentDoc> = emptyList()
)

@Serializable
data class MeiliEntertainmentDoc(
    val id: String = "",       // "movie_123" or "series_456"
    val type: String = "",     // "movie" or "series"
    val tmdbId: Int = 0,
    val title: String = "",
    val posterUrl: String = "",
    val imdbRating: Double? = null
)
