package com.appuccino.entertainment_spot

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {

    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    private const val WATCHMODE_BASE_URL = "https://api.watchmode.com/v1/"

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
}