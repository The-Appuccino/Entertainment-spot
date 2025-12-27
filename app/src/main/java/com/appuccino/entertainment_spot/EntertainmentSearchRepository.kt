package com.appuccino.entertainment_spot

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * EntertainmentSearchRepository
 *
 * Acts as the **data access layer** for search functionality.
 *
 * This class is responsible for **retrieving search data** from data sources,
 * primarily Firebase Firestore, and returning domain models (`Movie`, `Series`).
 *
 *  Responsibilities:
 *  - Execute Firestore queries for movies and series
 *  - Apply query constraints (where clauses, ordering, limits)
 *  - Map Firestore documents into Kotlin data models
 *
 */

class EntertainmentSearchRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {


    /* ============================
 *  BASIC TITLE SEARCH
 * ============================
 */

    /**
     * Search movies by title.
     *
     * @param query User-entered search string (already normalized)
     * @return List of matching Movie objects
     */
    suspend fun searchMoviesByTitle(query: String): List<Movie> {
        TODO("Firestore title search for movies")
    }

    /**
     * Search TV series by title.
     *
     * @param query User-entered search string (already normalized)
     * @return List of matching Series objects
     */
    suspend fun searchSeriesByTitle(query: String): List<Series> {
        TODO("Firestore title search for series")
    }


//    /* ============================
// *  GENRE-BASED SEARCH
// * ============================
// */
//
//    /**
//     * Search movies by genre name.
//     *
//     * @param genre Genre name (e.g. \"Action\", \"Drama\")
//     */
//    suspend fun searchMoviesByGenre(genre: String): List<Movie> {
//        TODO("Firestore genre search for movies")
//    }
//
//    /**
//     * Search series by genre name.
//     *
//     * @param genre Genre name (e.g. \"Sci-Fi\")
//     */
//    suspend fun searchSeriesByGenre(genre: String): List<Series> {
//        TODO("Firestore genre search for series")
//    }
//
//
//    /* ============================
//     *  YEAR / DATE SEARCH
//     * ============================
//     */
//
//    /**
//     * Search movies released in a specific year.
//     */
//    suspend fun searchMoviesByYear(year: Int): List<Movie> {
//        TODO("Firestore year-based movie search")
//    }
//
//    /**
//     * Search series first aired in a specific year.
//     */
//    suspend fun searchSeriesByYear(year: Int): List<Series> {
//        TODO("Firestore year-based series search")
//    }
//
//
//    /* ============================
//     *  RATING-BASED SEARCH
//     * ============================
//     */
//
//    /**
//     * Search movies with IMDb rating >= minRating.
//     */
//    suspend fun searchMoviesByMinRating(minRating: Double): List<Movie> {
//        TODO("Firestore rating filter for movies")
//    }
//
//    /**
//     * Search series with IMDb rating >= minRating.
//     */
//    suspend fun searchSeriesByMinRating(minRating: Double): List<Series> {
//        TODO("Firestore rating filter for series")
//    }
//
//
//    /* ============================
//     *  PLATFORM SEARCH
//     * ============================
//     */
//
//    /**
//     * Search movies available on a specific streaming platform.
//     *
//     * Example: Netflix, Prime Video, Apple TV
//     */
//    suspend fun searchMoviesByPlatform(platform: String): List<Movie> {
//        TODO("Firestore platform search for movies")
//    }
//
//    /**
//     * Search series available on a specific streaming platform.
//     */
//    suspend fun searchSeriesByPlatform(platform: String): List<Series> {
//        TODO("Firestore platform search for series")
//    }
//
//
//    /* ============================
//     *  COMPOSITE / ADVANCED SEARCH
//     * ============================
//     */
//
//    /**
//     * Execute a composite search using a structured SearchSpec.
//     *
//     * This is the primary method the UseCase will call once
//     * voice/LLM parsing is introduced.
//     */
//    suspend fun searchMovies(spec: SearchSpec): List<Movie> {
//        TODO("Composite Firestore movie search using SearchSpec")
//    }
//
//    /**
//     * Execute a composite search for series using a structured SearchSpec.
//     */
//    suspend fun searchSeries(spec: SearchSpec): List<Series> {
//        TODO("Composite Firestore series search using SearchSpec")
//    }
}