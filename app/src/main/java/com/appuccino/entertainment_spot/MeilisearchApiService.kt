package com.appuccino.entertainment_spot

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * MeilisearchApiService
 *
 * What this file is responsible for
 * - Defines the “contract” (endpoints + request/response types) for talking to Meilisearch.
 * - Retrofit reads this interface and generates the actual HTTP implementation at runtime.
 *
 * Typical endpoint(s) you use for title search
 * - POST /indexes/{indexUid}/search
 *   Body includes "q" (query text) and optionally "limit", "offset", "filter", etc.
 *
 * Why this separation is useful
 * - Keeps raw HTTP details (paths, verbs, request objects) out of your Fragment.
 * - Makes Repository code clean: repository just calls api.search(...) and maps the result.
 *
 * How it’s used in your flow
 * Fragment -> UseCase -> Repository -> (calls) MeilisearchApiService -> network -> response
 */


interface MeilisearchApiService {
    @POST("indexes/{indexUid}/search")
    suspend fun search(
        @Path("indexUid") indexUid: String,
        @Body body: MeiliSearchRequest
    ): MeiliSearchResponse
}