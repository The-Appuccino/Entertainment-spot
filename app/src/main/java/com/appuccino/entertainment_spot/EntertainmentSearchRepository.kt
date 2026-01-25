package com.appuccino.entertainment_spot

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * EntertainmentSearchRepository
 *
 * What this file is responsible for
 * - Acts as the single “data access” entry point for searching entertainment titles.
 * - Owns the logic for:
 *    - calling Meilisearch
 *    - translating Meilisearch response ("hits") into app-friendly objects (SearchRowItem)
 *    - handling errors in a consistent way (exceptions, empty results, logging)
 *
 * Why a repository exists (instead of calling Retrofit from the Fragment)
 * - Keeps networking + mapping out of UI code.
 * - Makes it easier to change the data source later (Meilisearch local -> Meilisearch cloud,
 *   or Meilisearch -> another search provider) without rewriting UI.
 * - Gives you one place to normalize “movie vs series” and unify the result list.
 *
 * Inputs / Outputs
 * - Input: raw query text (title search)
 * - Output: List<SearchRowItem> that your RecyclerView can render (movies + series together)
 *
 * Typical internal steps
 * 1) Build request (q, limit, optional params)
 * 2) Call api.search(indexUid, request)
 * 3) Map each hit:
 *    - determine ContentType (MOVIE / SERIES)
 *    - extract tmdbId, title, posterUrl, rating, etc.
 * 4) Return a flat list for the UI
 *
 * Notes for your current local setup
 * - If your Meilisearch index stores both movies + series in the same index, repository just
 *   maps and returns a combined list
 */


class EntertainmentSearchRepository(
    private val meili: MeilisearchApiService = RetrofitClient.meiliService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val indexUid: String = "entertainment",
) {

    suspend fun searchTitle(query: String, limit: Int = 30): List<MeiliEntertainmentDoc> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()
        return meili.search(indexUid, MeiliSearchRequest(q = q, limit = limit)).hits
    }

    suspend fun getMovieByTmdbId(tmdbId: Int): Movie? {
        val snap = db.collection("movies").document(tmdbId.toString()).get().await()
        return snap.toObject(Movie::class.java)
    }

    suspend fun getSeriesByTmdbId(tmdbId: Int): Series? {
        val snap = db.collection("series").document(tmdbId.toString()).get().await()
        return snap.toObject(Series::class.java)
    }
}