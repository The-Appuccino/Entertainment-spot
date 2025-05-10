package com.appuccino.entertainment_spot

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchmodeApiService {
    @GET("search/")
    suspend fun searchByTmdbId(
        @Query("search_field") field: String,
        @Query("search_value") tmdbId: Int,
        @Query("types") types: String = "movie,tv",
        @Query("apiKey") apiKey: String
    ): WatchmodeSearchResponse

    @GET("title/{watchmode_id}/details/")
    suspend fun getWatchmodeTitleDetails(
        @Path("watchmode_id") watchmodeId: Int,
        @Query("apiKey") apiKey: String
    ): WatchmodeTitleDetailsResponse

    @GET("title/{watchmode_id}/sources/")
    suspend fun getWatchmodeSources(
        @Path("watchmode_id") watchmodeId: Int,
        @Query("apiKey") apiKey: String
    ): List<WatchmodeSource>
}