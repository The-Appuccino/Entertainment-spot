package com.appuccino.entertainment_spot

/**
 * EntertainmentSeachUseCase
 *
 * What this file is responsible for
 * - Provides a “business logic” layer between UI (Fragment) and data (Repository).
 * - In clean architecture terms: a UseCase represents a single user intent, e.g.:
 *    “Search titles by query text”
 *
 * Why it exists (even if it feels thin right now)
 * - You can add search rules here without touching the Fragment:
 *    - ignore queries shorter than N chars
 *    - debounce / throttle rules (or return a Flow)
 *    - query cleanup (trim, normalize whitespace)
 *    - add ranking tweaks (ex: detect “movie” vs “tv show” keywords)
 *    - later: voice -> text -> parse -> query rewriting (LLM or heuristic parsing)
 * - Keeps the Fragment simple: it just forwards user input and renders results.
 *
 * How it’s used
 * Fragment calls: useCase.searchTitles(query)
 * UseCase decides “should I search?” and then calls repository.searchTitles(query)
 */


class EntertainmentSearchUseCase(
    private val repo: EntertainmentSearchRepository = EntertainmentSearchRepository()
) {
    suspend fun searchByTitle(query: String): List<SearchRowItem> {
        val hits = repo.searchTitle(query)

        return hits.mapNotNull { doc ->
            val type = when (doc.type.lowercase()) {
                "movie" -> ContentType.MOVIE
                "series", "tv", "show" -> ContentType.SERIES
                else -> return@mapNotNull null
            }

            SearchRowItem(
                type = type,
                tmdbId = doc.tmdbId,
                displayTitle = doc.title,
                posterUrl = doc.posterUrl,
                imdbRating = doc.imdbRating
            )
        }
    }

    suspend fun fetchFullItem(row: SearchRowItem): Any? {
        return when (row.type) {
            ContentType.MOVIE -> repo.getMovieByTmdbId(row.tmdbId)
            ContentType.SERIES -> repo.getSeriesByTmdbId(row.tmdbId)
        }
    }
}