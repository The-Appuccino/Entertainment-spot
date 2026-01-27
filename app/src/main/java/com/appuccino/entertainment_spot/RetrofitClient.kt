package com.appuccino.entertainment_spot

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Interceptor


/**
 * RetrofitClient
 *
 * Purpose
 * -------
 * This object is the single, centralized networking configuration for the app.
 * It creates and exposes Retrofit service instances for all external APIs used
 * by the project, so the rest of the codebase never has to deal with:
 *  - base URLs
 *  - JSON configuration
 *  - authentication headers
 *  - OkHttp client setup
 *
 * APIs handled here
 * -----------------
 * 1) TMDb (The Movie Database)
 *    - Used for fetching detailed movie and TV show metadata.
 *    - Uses a standard Retrofit setup with Kotlinx Serialization.
 *
 * 2) Watchmode
 *    - Used for availability / streaming provider information.
 *    - Shares the same JSON configuration as TMDb for consistency.
 *
 * 3) Meilisearch (local instance)
 *    - Used for fast, full-text title search across movies and TV series.
 *    - Runs as a local service on the developer machine.
 *    - Android connects either via:
 *        • 10.0.2.2 when using the Android emulator
 *        • a LAN IP when using a physical device
 *
 * Serialization setup
 * -------------------
 * Kotlinx Serialization is configured with:
 *    ignoreUnknownKeys = true
 *
 * This allows the app to safely ignore extra fields returned by APIs
 * without breaking deserialization when APIs evolve or add new fields.
 *
 * Meilisearch authentication
 * --------------------------
 * Meilisearch is started with an API key enabled, so every request must
 * include an Authorization header.
 *
 * An OkHttp interceptor is used to automatically attach:
 *
 *    Authorization: Bearer <MEILI_API_KEY>
 *
 * to every Meilisearch request, keeping authentication logic out of
 * repositories and UI code.
 *
 * Architectural role
 * ------------------
 * RetrofitClient acts as the lowest-level networking layer:
 *
 * Fragment / ViewModel
 *        ↓
 * UseCase
 *        ↓
 * Repository
 *        ↓
 * RetrofitClient (this file)
 *        ↓
 * External API (TMDb / Watchmode / Meilisearch)
 *
 * By centralizing network setup here:
 * - API changes are isolated to one file
 * - security headers are applied consistently
 * - testing and future refactors (e.g. swapping search providers)
 *   become significantly easier
 *
 * Notes / limitations
 * -------------------
 * - The Meilisearch API key is currently embedded for local development.
 *   This is acceptable for a personal, non-published project.
 * - In a production app, this key would be replaced with:
 *     • a search-only key, or
 *     • a backend proxy that hides the master key entirely.
 */

object RetrofitClient {

    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    private const val WATCHMODE_BASE_URL = "https://api.watchmode.com/v1/"

    // Local Meilisearch (Android emulator → your machine):
    // - If running on your host computer, emulator must use 10.0.2.2
    // - If on a real device, use your LAN IP (e.g. http://192.168.1.30:7700/)
    private const val MEILI_BASE_URL = "http://10.0.2.2:7700/"
    //private const val MEILI_BASE_URL = "http://192.168.1.30:7700/"
    private const val MEILI_API_KEY = ""

    private val json = Json { ignoreUnknownKeys = true }

    private val contentType = "application/json".toMediaType()

    val tmdbService: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(TmdbApiService::class.java)
    }

    val watchmodeService: WatchmodeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WATCHMODE_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(WatchmodeApiService::class.java)
    }

    val meiliService: MeilisearchApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $MEILI_API_KEY")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(MEILI_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(MeilisearchApiService::class.java)
    }

}